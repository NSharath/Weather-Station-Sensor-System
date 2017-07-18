package com.sensor_cloud;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import com.mongodb.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("/data")
public class SensorDataCollector {
	@GET
	@Produces("application/json")
	public String getData() {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/autoip.json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("userSensors")
	@GET
	@Produces("application/json")
	public String getUserSensors(String userSensors) {
		String outStr = "";
		try {
            
         // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "DataCollectorTest" );
	        System.out.println("Geolookup: Connect to database successfully");
	        DBCollection collection = db.getCollection("sensorColl");
	        System.out.println("Creating BasicDBObject for Geolookup...");
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	     	String userid = "1234";
			whereQuery.put("userId", userid);
			DBCursor cursor = collection.find(whereQuery);
	        
	        if(cursor.count() !=0){
				String outStr1 = "";
				System.out.println("Already Exist");;
				while(cursor.hasNext()) {
					outStr1 += cursor.next().toString();
				}
				return getPrettyJson(outStr1);
			}
	        
	     	BasicDBObject document = new BasicDBObject();
	     	ArrayList<BasicDBObject> arr = new ArrayList<BasicDBObject>();
	     	double[] lats ={37.377316, 37.400707,37.402142,37.391171,37.356911};
	     	double[] lons = {-121.886246,-121.882042,-121.882561,-121.866486,-121.877617};
	     	document.put("userId", "1234");
	     	for(int i=0; i<5; i++){
	     		BasicDBObject document1 = new BasicDBObject();
	     		String name = "Sensor_"+i;
	     		String type = "Type_"+i;
	     		String location = "Location_"+i;
	     		String status = "Status_"+i;
	     		document1.put("name", name);
	     		document1.put("type", type);
	     		document1.put("location", location);
	     		document1.put("status", status);
	     		document1.put("lat", lats[i]);
	     		document1.put("lon", lons[i]);
	     		arr.add(document1);
	     		
	     		
	     	}
	     	document.put("sensors", arr);  
	     	 collection.insert(document);
			cursor = collection.find(whereQuery);
	     	 
			while(cursor.hasNext()) {
				//System.out.println(cursor.next());
				outStr += cursor.next().toString();
			}
	     	System.out.println(outStr);
	     	mongoClient.close();
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
	
		return getPrettyJson(outStr);
	}
 
	@Path("conditions/station/{pws}")
	@GET
	@Produces("application/json")
	public String getStationConditions(@PathParam("pws") String pws) {
		//http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/pws:KCASANJO177.json
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/pws:" + pws +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processConditionData(jsonResponse);

		return jsonOutput;
	}
	
	@Path("forecast/station/{pws}")
	@GET
	@Produces("application/json")
	public String getStationForecast(@PathParam("pws") String pws) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/forecast/q/pws:" + pws +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processForecastData(jsonResponse);

		return jsonOutput;
	}
	
	@Path("geolookup/station/{pws}")
	@GET
	@Produces("application/json")
	public String getStationGeolookup(@PathParam("pws") String pws) {
		//http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/pws:KCASANJO177.json
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/pws:" + pws +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);

		return jsonOutput;
	}
	@Path("conditions/zip/{c}")
	@GET
	@Produces("application/json")
	public String getZipConditions(@PathParam("c") int c) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/" + c +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processConditionData(jsonResponse);

		return jsonOutput;
	}
	
	@Path("forecast/zip/{c}")
	@GET
	@Produces("application/json")
	public String getZipForecast(@PathParam("c") int c) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/forecast/q/" + c +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processForecastData(jsonResponse);

		return jsonOutput;
	}
	
	@Path("geolookup/zip/{c}")
	@GET
	@Produces("application/json")
	public String getZipGeolookup(@PathParam("c") int c) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/" + c +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);

		return jsonOutput;
	}
	
	@Path("conditions/city/{state}/{city}")
	@GET
	@Produces("application/json")
	public String getCityConditions(@PathParam("state") String state, @PathParam("city") String city) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/" + state + "/"+ city +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processConditionData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("forecast/city/{state}/{city}")
	@GET
	@Produces("application/json")
	public String getCityForecast(@PathParam("state") String state, @PathParam("city") String city) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/forecast/q/" + state + "/"+ city +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processForecastData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("geolookup/city/{state}/{city}")
	@GET
	@Produces("application/json")
	public String getCityGeolookup(@PathParam("state") String state, @PathParam("city") String city) {
		System.out.println(city);
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "DataCollectorTest" );
			DBCollection collection = db.getCollection("geolookupColl");
			BasicDBObject whereQuery = new BasicDBObject();
			String strCity = city;
			strCity = strCity.replaceAll("_", " ");
			whereQuery.put("city", strCity);
			whereQuery.put("state", state);
			DBCursor cursor = collection.find(whereQuery);
//			while(cursor.hasNext()) {
//			    System.out.println(cursor.next());
//			}
			if(cursor.count() !=0){
				String outStr = "";
				System.out.println("Already Exist");;
				while(cursor.hasNext()) {
					//System.out.println(cursor.next());
					outStr += cursor.next().toString();
				}
				return getPrettyJson(outStr);
			}
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/" + state + "/"+ city +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("conditions/latitude/{lat}/longitude/{lon}")
	@GET
	@Produces("application/json")
	public String getLatLonConditions(@PathParam("lat") double lat, @PathParam("lon") double lon) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/conditions/q/" + lat + ","+ lon +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processConditionData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("forecast/latitude/{lat}/longitude/{lon}")
	@GET
	@Produces("application/json")
	public String getLatLonForecast(@PathParam("lat") double lat, @PathParam("lon") double lon) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/forecast/q/" + lat + ","+ lon +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processForecastData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("geolookup/latitude/{lat}/longitude/{lon}")
	@GET
	@Produces("application/json")
	public String getLatLonGeolookup(@PathParam("lat") double lat, @PathParam("lon") double lon) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/" + lat + ","+ lon +".json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);
		
		return jsonOutput;
	}
	
	@Path("auto_location")
	@GET
	@Produces("application/json")
	public String getAutolocation(@PathParam("lat") double lat, @PathParam("lon") double lon) {
		String url = "http://api.wunderground.com/api/91e0c260e0e5b86c/geolookup/q/autoip.json";
		String jsonResponse = getJsonData(url);
		String jsonOutput = processGeolookupData(jsonResponse);
		
		return jsonOutput;
	}
	
	public String processGeolookupData(String jsonResponse) {
		String outStr = "";
		JSONParser parser = new JSONParser();
		//ObjectMapper mapper = new ObjectMapper();
		try {
			
            Object obj = parser.parse(jsonResponse);
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject location = (JSONObject)jsonObject.get("location");
            JSONObject nearby_weather_stations = (JSONObject)location.get("nearby_weather_stations");
            //JSONObject simpleforecast = (JSONObject)forecast.get("simpleforecast");
            
            String type = (String) location.get("type");
            String country = (String) location.get("country");
            String state = (String) location.get("state");
            String city = (String) location.get("city");
            String zip = (String) location.get("zip");
            String longitude = (String) location.get("lon");
            String latitude = (String) location.get("lat");
            
         // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "DataCollectorTest" );
	        System.out.println("Geolookup: Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
	         //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("geolookupColl");
	        System.out.println("Creating BasicDBObject for Geolookup...");
	     	BasicDBObject document = new BasicDBObject(); 
  	 
	        document.put("type", type);
	        document.put("country", country);
	        document.put("state", state);
	        document.put("city", city);
	        System.out.println("CITY: "+city);
	        document.put("zip", zip);
	        document.put("longitude", longitude);
	        document.put("latitude", latitude);
	        document.put("nearby_weather_stations", nearby_weather_stations);
	  
	        
	     	 collection.insert(document);
	     	 
	     	BasicDBObject whereQuery = new BasicDBObject();
			String strCity = city;
			strCity = strCity.replaceAll("_", " ");
			whereQuery.put("city", strCity);
			whereQuery.put("state", state);
			DBCursor cursor = collection.find(whereQuery);
	     	 
			while(cursor.hasNext()) {
				//System.out.println(cursor.next());
				outStr += cursor.next().toString();
			}
	     	 
	     	System.out.println(outStr);
	     	//collection.remove(new BasicDBObject());
	     	mongoClient.close();
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		
		//System.out.println(jsonResponse);
	
		return getPrettyJson(outStr);
	}
	
	public String processForecastData(String jsonResponse) {
		String outStr = "";
		JSONParser parser = new JSONParser();
		//ObjectMapper mapper = new ObjectMapper();
		try {
			
            Object obj = parser.parse(jsonResponse);
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject forecast = (JSONObject)jsonObject.get("forecast");
            JSONObject forecast_txt = (JSONObject)forecast.get("txt_forecast");
            JSONObject simpleforecast = (JSONObject)forecast.get("simpleforecast");
            
            // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "DataCollectorTest" );
	        System.out.println("Forecast: Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
	         //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("forecastColl");
	        System.out.println("Creating BasicDBObject for forecast...");
	     	BasicDBObject document = new BasicDBObject();
	            	     	 
	        document.put("txt_forecast", forecast_txt);
	        document.put("simple_forecast", simpleforecast);
	  
	        
	     	 collection.insert(document);

	     	 DBCursor cursorDoc = collection.find();
	     	 //outStr = collection.toString();
	     	 while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		System.out.println(outStr);
	     	 }

	     	collection.remove(new BasicDBObject());
	     	mongoClient.close();
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
	
		return getPrettyJson(outStr);
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
	
	public String processConditionData(String jsonResponse) {
		String outStr = "";
		JSONParser parser = new JSONParser();
		try {
			
            Object obj = parser.parse(jsonResponse);
 
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject current_obv = (JSONObject)jsonObject.get("current_observation");
            JSONObject display_location = (JSONObject)current_obv.get("display_location");

            String state = (String) display_location.get("state");
            String full_name = (String) display_location.get("full");
            String city = (String) display_location.get("city");
            String state_name = (String) display_location.get("state_name");
            String country = (String) display_location.get("country");
            String zip = (String) display_location.get("zip");
            String latitude = (String) display_location.get("latitude");
            String longitude = (String) display_location.get("longitude");
            //JSONArray companyList = (JSONArray) jsonObject.get("Company List");
            
            String station_id = (String) current_obv.get("station_id");
            String weather = (String) current_obv.get("weather");
  
    		String temperature_string = (String) current_obv.get("temperature_string");
            Double temp_f = (Double) current_obv.get("temp_f");
            Double temp_c = (Double) current_obv.get("temp_c");
            String relative_humidity = (String) current_obv.get("relative_humidity");
            String pressure_in = (String) current_obv.get("pressure_in");
            //System.out.println("Name: " + state);
            //System.out.println("Author: " + zip);

            // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	        // Now connect to your databases
	        DB db = mongoClient.getDB( "DataCollectorTest" );
	        System.out.println("Conditions: Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
	         //System.out.println("Authentication: "+auth);
	        DBCollection collection = db.getCollection("conditionsColl");
	        System.out.println("Creating BasicDBObject for conditions...");
	     	BasicDBObject document = new BasicDBObject();
	     	 
	     	document.put("full_name", full_name);
	     	document.put("state", state);
	     	document.put("city", city);
	     	document.put("state_name", state_name);
	     	document.put("country", country);
	     	document.put("zip", zip);
	     	document.put("latitude", latitude);
	     	document.put("longitude", longitude);
	     	document.put("station_id", station_id);
	     	document.put("weather", weather);
	     	document.put("temperature_string", temperature_string);
	     	document.put("temp_f", temp_f);
	     	document.put("temp_c", temp_c);
	     	document.put("relative_humidity", relative_humidity);
	     	document.put("pressure_in", pressure_in);	     	 

	     	 collection.insert(document);

	     	 DBCursor cursorDoc = collection.find();
	     	 //outStr = collection.toString();
	     	 while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		System.out.println(outStr);
	     	 }

	     	//collection.remove(new BasicDBObject());
	     	mongoClient.close();
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		
		return getPrettyJson(outStr);
	}
	
	public String getJsonData(String url) {
		String jsonStr ="";
		  try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(
				url);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(
	                         new InputStreamReader((response.getEntity().getContent())));

			String output;
			
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonStr += output;
				System.out.println(output);
			}

			httpClient.getConnectionManager().shutdown();

		  } catch (ClientProtocolException e) {
		
			e.printStackTrace();

		  } catch (IOException e) {
		
			e.printStackTrace();
		  }
		  
		  return jsonStr;

	}

}