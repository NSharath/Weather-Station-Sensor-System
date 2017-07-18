package com.sensor_cloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.mongodb.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/hub")
public class Hubs {
	
	@Path("{username}/exist")
	@GET
	@Produces("application/json")
	public Response getExist(@PathParam("username") String username) {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("hubsCollection");
	        
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			System.out.println("User already exist"+cursor.count());
			
			if(cursor.count() == 0){
				System.out.println("User already exist");
				return Response.status(404).entity("").build();
			}
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return Response.status(200).entity("").build();
	}
	
	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getData(@PathParam("username") String username) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("hubsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
			}
			System.out.println("Match: " + outStr);
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Now connect to your databases
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("{username}/{hub_id}")
	@GET
	@Produces("application/json")
	public Response getData(@PathParam("username") String username, @PathParam("hub_id") String hub_id) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("hubsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			BasicDBObject fields = new BasicDBObject();
			fields.put(hub_id, 1);
			DBCursor cursor = collection.find(whereQuery, fields);
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
			}
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("{username}/{hub_id}")
	@PUT
	@Consumes("application/json")
	public Response doPut(InputStream incomingData) {
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) obj;
			
			System.out.println("json Obj: "+ jsonObject);
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB: Connect to database successfully");
	        DBCollection collection = db.getCollection("hubsCollection");
	        System.out.println("Creating BasicDBObject for Hub POST...");
	     	BasicDBObject hubDocument = new BasicDBObject();		
            ArrayList<BasicDBObject> sensor_list = new ArrayList<BasicDBObject>();

	     	String username = (String) jsonObject.get("username");
	     	String hub_id = (String) jsonObject.get("hub_id");
	     	String name = (String) jsonObject.get("name");
	     	String description = (String) jsonObject.get("description");

	        hubDocument.put("hub_id", hub_id);
	        hubDocument.put("name", name);
	        hubDocument.put("description", description);
	        hubDocument.put("sensors", sensor_list);

	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
	        
	        BasicDBObject newDocument = new BasicDBObject();
	    	newDocument.append("$set", new BasicDBObject().append(hub_id, hubDocument));

	    	collection.update(whereQuery, newDocument);
	        
	     	DBCursor cursorDoc = collection.find(whereQuery);
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     	}
	     	System.out.println(outStr);
	     	mongoClient.close();
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		return Response.status(201).entity("").build();

	}

	@SuppressWarnings("unchecked")
	@Path("{username}/{hub_id}/add_sensor/{sensor_id}")
	@PUT
	@Consumes("application/json")
	public Response doPutSensors(@PathParam("username") String username,@PathParam("hub_id") String hub_id,@PathParam("sensor_id") String sensor_id) {
		//String result1;
		String outStr = "";
		try {
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB: Connect to database successfully");

	        DBCollection collection = db.getCollection("hubsCollection");
	        System.out.println("Creating BasicDBObject for Hub POST...");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			BasicDBObject fields = new BasicDBObject();
			fields.put(hub_id, 1);
			DBCursor cursorDoc = collection.find(whereQuery, fields);
			if(cursorDoc.count() == 0){
				return Response.status(404).entity("").build();
			}
	     	BasicDBObject hubDocument = new BasicDBObject();		
            ArrayList<String> sensor_list = new ArrayList<String>();

	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
	     	JSONParser parser = new JSONParser();
			Object obj = parser.parse(outStr);
			JSONObject jsonObject = (JSONObject) obj; 
			JSONObject doc = (JSONObject) jsonObject.get(hub_id);
			sensor_list = (ArrayList<String>) doc.get("sensors");
			sensor_list.add(sensor_id);

	     	String name = (String) doc.get("name");
	     	String description = (String) doc.get("description");
	     	
	        hubDocument.put("hub_id", hub_id);
	        hubDocument.put("name", name);
	        hubDocument.put("description", description);
	        hubDocument.put("sensors", sensor_list);

	        System.out.println(sensor_list.get(0));
	        BasicDBObject whereQuery1 = new BasicDBObject();
			whereQuery1.put("username", username);
	        
	        BasicDBObject newDocument = new BasicDBObject();
	    	newDocument.append("$set", new BasicDBObject().append(hub_id, hubDocument));

	    	collection.update(whereQuery1, newDocument);
	    	mongoClient.close();

        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }

		return Response.status(201).entity("").build();

	}
	
	@SuppressWarnings("unchecked")
	@Path("{username}/{hub_id}/delete_sensor/{sensor_id}")
	@PUT
	@Consumes("application/json")
	public Response doDeleteSensors(@PathParam("username") String username,@PathParam("hub_id") String hub_id,@PathParam("sensor_id") String sensor_id) {
		//String result1;
		String outStr = "";
		try {
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB: Connect to database successfully");

	        DBCollection collection = db.getCollection("hubsCollection");
	        System.out.println("Creating BasicDBObject for Hub POST...");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			BasicDBObject fields = new BasicDBObject();
			fields.put(hub_id, 1);
			DBCursor cursorDoc = collection.find(whereQuery, fields);
			if(cursorDoc.count() == 0){
				return Response.status(404).entity("").build();
			}
	     	BasicDBObject hubDocument = new BasicDBObject();		
            ArrayList<String> sensor_list = new ArrayList<String>();

	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
	     	JSONParser parser = new JSONParser();
			Object obj = parser.parse(outStr);
			JSONObject jsonObject = (JSONObject) obj; 
			JSONObject doc = (JSONObject) jsonObject.get(hub_id);
			sensor_list = (ArrayList<String>) doc.get("sensors");
			sensor_list.remove(sensor_id);

	     	String name = (String) doc.get("name");
	     	String description = (String) doc.get("description");
	     	
	        hubDocument.put("hub_id", hub_id);
	        hubDocument.put("name", name);
	        hubDocument.put("description", description);
	        hubDocument.put("sensors", sensor_list);

	        System.out.println(sensor_list.get(0));
	        BasicDBObject whereQuery1 = new BasicDBObject();
			whereQuery1.put("username", username);
	        
	        BasicDBObject newDocument = new BasicDBObject();
	    	newDocument.append("$set", new BasicDBObject().append(hub_id, hubDocument));

	    	collection.update(whereQuery1, newDocument);
	    	mongoClient.close();

        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }

		return Response.status(201).entity("").build();

	}
	
	@Path("{username}/{hub_id}")
	@POST
	@Consumes("application/json")
	public Response doPost(InputStream incomingData) {
		//String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/" + sid +".json";
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) obj;
			
			System.out.println("json Obj: "+ jsonObject);
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "HubDB" );
	        System.out.println("HubDB: Connect to database successfully");
	        DBCollection collection = db.getCollection("hubsCollection");
	        System.out.println("Creating BasicDBObject for Hub POST...");
	     	BasicDBObject document = new BasicDBObject(); 
	     	BasicDBObject hubDocument = new BasicDBObject();		
	     	ArrayList<String> sensor_list = new ArrayList<String>();

	     	String username = (String) jsonObject.get("username");
	     	String hub_id = (String) jsonObject.get("hub_id");
	     	String name = (String) jsonObject.get("name");
	     	String description = (String) jsonObject.get("description");

	        document.put("username", username);
	        
	        hubDocument.put("hub_id", hub_id);
	        hubDocument.put("name", name);
	        hubDocument.put("description", description);
	        hubDocument.put("sensors", sensor_list);
	        document.put(hub_id, hubDocument);
	        collection.insert(document);
	        
	     	DBCursor cursorDoc = collection.find();
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
	     	mongoClient.close();

        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }

		return Response.status(201).entity("").build();

	}
	
	@Path("{sensor_id}")
	@DELETE
	@Consumes("application/json")
	public Response deleteRecord(@PathParam("sensor_id") String sensor_id) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("AttendanceDB Delete: Connect to database successfully");

	        DBCollection collection = db.getCollection("sensorsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sensor_id", sensor_id);
			collection.remove(whereQuery);

			outStr = "Username: " + sensor_id + " was deleted succesfully.";
			System.out.println(outStr);
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(204).entity(outStr).build();
	}
	
	
	public String getIncomingData(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println("Line - " +line);
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + strBuilder.toString());
		return strBuilder.toString();
	}

	
	public String getPrettyJson(String jsonStr){
		ObjectMapper mapper = new ObjectMapper();
		String prettyJson ="";
		try {
			Object jsonTest = mapper.readValue(jsonStr, Object.class);
			prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTest);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prettyJson;
	}

	

}
