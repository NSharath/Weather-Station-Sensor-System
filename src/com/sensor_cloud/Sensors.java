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

@Path("/sensor")
public class Sensors {
	@Path("{username}/{sensor_id}")
	@GET
	@Produces("application/json")
	public Response getSensor(@PathParam("sensor_id") String sensor_id, @PathParam("username") String username) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("sensorsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sensor_id", sensor_id);
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
			    //System.out.println("Match: " +cursor.next());
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
	//http://localhost:8080/MobileSensorCloudEngine/api/sensor/jsantos7
	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getUserSensors(@PathParam("username") String username) {
		MongoClient mongoClient;
		//String outStr = "";
		String outStr1 = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("sensorsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			
			BasicDBObject document = new BasicDBObject();
	     	document.put("username", username);

			
			ArrayList<DBObject> arr = new ArrayList<DBObject>();
			while(cursor.hasNext()) {
				arr.add(cursor.next());
			}
			document.put("sensors", arr);
			DBCollection collection1 = db.getCollection("usersensorsCollection");
			collection1.insert(document);
			DBCursor cursor1 = collection1.find(whereQuery);
			while(cursor1.hasNext()) {
				outStr1 += cursor1.next().toString();
				System.out.println("Inside: " +outStr1);
			}
			
			System.out.println("Match: " + outStr1);
			db.getCollection("usersensorsCollection").remove(new BasicDBObject());
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).entity(getPrettyJson(outStr1)).build();
	}
	
	
	@Path("{username}/{sensor_id}")
	@POST
	@Consumes("application/json")
	public Response doPost(InputStream incomingData) {
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
	        DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("UsersDB: Connect to database successfully");
	        DBCollection collection = db.getCollection("sensorsCollection");
	        System.out.println("Creating BasicDBObject for Sensor POST...");
	     	BasicDBObject document = new BasicDBObject(); 

	     	String username = (String) jsonObject.get("username");
	     	String sensor_id = (String) jsonObject.get("sensor_id");
	     	String name = (String) jsonObject.get("name");
	     	String state = (String) jsonObject.get("state");
	     	String latitude = (String) jsonObject.get("latitude");
	     	String longitude = (String) jsonObject.get("longitude");
	     	String hub = (String) jsonObject.get("hub");
	     	String sensor_key = (String) jsonObject.get("sensor_key");
	     	Boolean status = (Boolean) jsonObject.get("status");
	     	String city = (String) jsonObject.get("city");
	     	String zip = (String) jsonObject.get("zip");
	     	String country = (String) jsonObject.get("country");
	     	String description = (String) jsonObject.get("description");
	     	String location = city + ", "+state;

  	
	        document.put("username", username);
	        document.put("name", name);
	        document.put("sensor_id", sensor_id);
	        document.put("sensor_key", sensor_key);
	        document.put("state", state);
	        document.put("city", city);
	        document.put("zip", zip);
	        document.put("location", location);
	        document.put("latitude", latitude);
	        document.put("longitude", longitude);
	        document.put("country", country);
	        document.put("description", description);
	        document.put("status", status);
	        document.put("hub", hub);
	        collection.insert(document);

	     	DBCursor cursorDoc = collection.find();
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
	
	@Path("{username}/{sensor_id}/activate")
	@PUT
	@Consumes("application/json")
	public Response doActivate(@PathParam("sensor_id") String sensor_id, @PathParam("username") String username) {
		String outStr = "";

		try {
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("HubDB: Connect to database successfully");
	       
	        DBCollection collection = db.getCollection("sensorsCollection");
	        System.out.println("Creating BasicDBObject for PUT timeIn...");
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sensor_id", sensor_id);
			whereQuery.put("username", username);
	     	BasicDBObject activateDocument = new BasicDBObject();

	     	
	     	activateDocument.append("$set", new BasicDBObject().append("status", true));

	    	collection.update(whereQuery, activateDocument);
	     	DBCursor cursorDoc = collection.find(whereQuery);
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
	
	@Path("{username}/{sensor_id}/deactivate")
	@PUT
	@Consumes("application/json")
	public Response doDeactivate(@PathParam("sensor_id") String sensor_id, @PathParam("username") String username) {
		String outStr = "";

		try {
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("HubDB: Connect to database successfully");
	       
	        DBCollection collection = db.getCollection("sensorsCollection");
	        System.out.println("Creating BasicDBObject for PUT timeIn...");
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sensor_id", sensor_id);
			whereQuery.put("username", username);
	     	BasicDBObject activateDocument = new BasicDBObject();

	     	activateDocument.append("$set", new BasicDBObject().append("status", false));

	    	collection.update(whereQuery, activateDocument);
	     	DBCursor cursorDoc = collection.find(whereQuery);
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
	
	@Path("{username}/{sensor_id}")
	@DELETE
	@Consumes("application/json")
	public Response deleteRecord(@PathParam("sensor_id") String sensor_id, @PathParam("username") String username) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "SensorDB" );
	        System.out.println("AttendanceDB Delete: Connect to database successfully");
	        DBCollection collection = db.getCollection("sensorsCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
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
