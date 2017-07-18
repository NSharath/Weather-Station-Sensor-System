package com.sensor_cloud;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
//import java.util.ArrayList;

//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import com.mongodb.util.JSON;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import javax.ws.rs.core.MediaType;

@Path("/user")
public class Users {
	
	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getData(@PathParam("username") String username) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "UsersDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        //boolean auth = db.authenticate(myUserName, myPassword);
	        //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("usersCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			if(cursor.count() == 0){
				//return "{}\n";
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
		
		//return getPrettyJson(outStr);
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	// curl -i http://localhost:8080/MobileSensorCloudEngine/api/user/jsantos03/exist
	@Path("{username}/exist")
	@GET
	@Produces("application/json")
	public Response getExist(@PathParam("username") String username) {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "UsersDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("usersCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			DBCursor cursor = collection.find(whereQuery);
			
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Now connect to your databases
		
		return Response.status(200).entity("").build();
	}
	
	@Path("{username}/{password}/login")
	@GET
	@Produces("application/json")
	public Response getLogin(@PathParam("username") String username, @PathParam("password") String password) {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "UsersDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("usersCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			whereQuery.put("password", password);
			DBCursor cursor = collection.find(whereQuery);
			
			if(cursor.count() == 0){
				System.out.println("Doesn't match.");
				return Response.status(404).entity("").build();
			}
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Now connect to your databases
		
		return Response.status(200).entity("").build();
	}
	
	@Path("{username}")
	@POST
	//\@Produces("application/json")
	@Consumes("application/json")
	@Produces("application/json")
	public Response doPost(InputStream incomingData, @PathParam("username") String username) {
		//String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/" + sid +".json";
		//String jsonResponse = getJsonData(url);
		//String jsonOutput = processConditionData(jsonResponse);
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		//JSONParser parser = new JSONParser();
		//ObjectMapper mapper = new ObjectMapper();
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
	        DB db = mongoClient.getDB( "UsersDB" );
	        System.out.println("UsersDB: Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
	         //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("usersCollection");
	        System.out.println("Creating BasicDBObject for User POST...");
	     	BasicDBObject document = new BasicDBObject();
	     	
	     	BasicDBObject whereQueryCheck = new BasicDBObject();
			whereQueryCheck.put("username", username);
			DBCursor cursorCheck = collection.find(whereQueryCheck);
			if(cursorCheck.count() != 0){
				//return "{}\n";
		        System.out.println("User Already Exist");
				return Response.status(200).entity("").build();
			}
	     	
	     	String first = (String) jsonObject.get("first");
	     	String last = (String) jsonObject.get("last");
	     	String password = (String) jsonObject.get("password");
	     	String email = (String) jsonObject.get("email");
	     	//String username = (String) jsonObject.get("username");
  	 
//	        document.put("first", "Jelson");
//	        document.put("last", "Santos");
//	        document.put("sid", "1234");
	        document.put("first", first);
	        document.put("last", last);
	        document.put("password", password);
	        document.put("email", email);
	        document.put("username", username);
	        collection.insert(document);

	     	DBCursor cursorDoc = collection.find();
	     	 //outStr = collection.toString();
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
	     	//collection.remove(new BasicDBObject());
	     	mongoClient.close();
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }


	
 
		//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n";
		//return Response.status(200).entity(result).build();
		//return result;
		return Response.status(201).entity("").build();

	}
	
	@Path("{username}")
	@DELETE
	@Consumes("application/json")
	public Response deleteRecord(@PathParam("username") String username) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "UsersDB" );
	        System.out.println("AttendanceDB Delete: Connect to database successfully");
	        //boolean auth = db.authenticate(myUserName, myPassword);
	        //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("usersCollection");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("username", username);
			//DBCursor cursor = collection.find(whereQuery);
			collection.remove(whereQuery);
	
//			while(cursor.hasNext()) {
//				outStr += cursor.next().toString();
//			    //System.out.println("Match: " +cursor.next());
//			}
			//System.out.println("Match: " + outStr);
			outStr = "Username: " + username + " was deleted succesfully.";
			System.out.println(outStr);
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Now connect to your databases
		
		
		return Response.status(204).entity(outStr).build();
	}
	
	public String getIncomingData(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		//JSONParser parser = new JSONParser();
		try {
			//InputStreamReader in = new InputStreamReader(incomingData);
			//BufferedReader in = new BufferedReader(inStream);
			//Object obj = parser.parse(new InputStreamReader(incomingData));
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			//JSONObject jsonObject = (JSONObject) obj;

			//String name = (String) jsonObject.get("jsonData");
			//System.out.println("FIRST: " +name);
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println("Line - " +line);
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + strBuilder.toString());
 
		// return HTTP response 200 in case of success
		//return Response.status(200).entity(crunchifyBuilder.toString()).build();
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