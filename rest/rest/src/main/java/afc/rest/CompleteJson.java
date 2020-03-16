package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class CompleteJson {
	
	
	private static final Logger log = Logger.getLogger(Server.class);
    
	//	A method that parses a JSON object finding the missing values, and returns a HashMap containing the missing fields.
	public  HashMap <String, JsonElement> parseObject(JsonElement token,  HashMap <String, JsonElement> registryJSON, int counter)  {
		log.debug("Entered Parent Class");
		if (token.isJsonObject()) {
			JsonObject jsonObject= token.getAsJsonObject();
			switch (counter) {
			case 0: 
//				Check if there is more than one observed property
				if(jsonObject.has("longitude")&&jsonObject.has("latitude") /*(...)
	           			  (...)*/ &&jsonObject.has("altitude")&&jsonObject.has("resourceUrn")) {
	           						registryJSON.put("longitude", jsonObject.get("longitude"));
	           						registryJSON.put("latitude",  jsonObject.get("latitude"));
	           						registryJSON.put("altitude", jsonObject.get("altitude"));
	           						registryJSON.put("resourceId", jsonObject.get("resourceUrn"));

	           					}
			
				else {
           			log.error("Assets Registry: Missing fields required");
           		    throw new WebApplicationException("ERROR: Assets Registry: Missing fields required", 500);	
           		}
//				Check if there is more than one observed property
				JsonElement observations = jsonObject.get("observations");
				if (observations!=null&&observations.isJsonArray()) {
					JsonArray obsArray = observations.getAsJsonArray();
				 if	(obsArray.size()>1) {
					 log.error("The specified resource is not a single-parameter sensor");
				 	throw new WebApplicationException("The specified resource is not a single-parameter sensor", 500);
				 }


				counter++;
				registryJSON=parseObject(obsArray.get(0), registryJSON, counter);
				}
				else {
					log.error("Assets Registry: 'observations' must be a not-null array");
					throw new WebApplicationException("ERROR: Assets Registry: 'observations' must be a not-null array", 500);
					}
		    break;
			default: //Not the root object
				if (jsonObject.has("uom")&&jsonObject.has("observedProperty")){
             	   registryJSON.put("uom", jsonObject.get("uom"));
             	   registryJSON.put("observedProperty", jsonObject.get("observedProperty"));
             	   return registryJSON;
                }
				  else {
               	   log.error("Assets Registry: Missing fields required");
             		    throw new WebApplicationException("ERROR: Assets Registry: Missing fields required", 500);	

               	   
               	    }
			}
			}
		   return registryJSON;
	}			



		
	//	A method that parses the entire registration JSON of the received simplified JSON, and iterates over all objects recursively filling the missing values  

	public JsonObject parseEntireJson(String registrationJson) throws IOException {
		Gson gson = new Gson();
		JsonObject missingFields = new JsonObject();

		HashMap<String, JsonElement> registryJSON = new HashMap<String, JsonElement>();
		try {
			JsonElement jsonTree=JsonParser.parseString(registrationJson);
			
			//		We need to MANUALLY add the resourceUrn field, since it will substitute the resourceId value
			registryJSON= this.parseObject(jsonTree, registryJSON, 0);
       
		    missingFields=this.buildMissingJson(registryJSON, gson);
		
			System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFields.toString());

		}

		catch(JsonParseException e) {e.printStackTrace();
		}
		System.out.println("REGISTRY_JSON: "+registryJSON);
		return missingFields;

	}
	//	 A method that parses the JSON recursively filling the missing values of the \"missing values\" object

	public JsonObject buildMissingJson(HashMap<String, JsonElement> registryJSON, Gson gson) {
//		Check that the Map contains all required keys  
		  JsonObject missingFields= new JsonObject();
			JsonObject location = new JsonObject();	
		    Iterator<Map.Entry<String, JsonElement>> i =registryJSON.entrySet().iterator();
			while(i.hasNext()) {
				Entry<String, JsonElement> entry = i.next();
				String key = entry.getKey();
				if(key.matches("longitude||latitude||altitude")) {
				location.add(key, entry.getValue());
				}
				else if(key.matches("resourceId||observedProperty||uom")) {
					missingFields.add(key, entry.getValue());
					}
				}
			missingFields.add("location", location);
			return missingFields;
			}
	
	// A method to complete the simplified JSON obtaining the missing information from the Assets Registry URI
	public static JsonObject getCompleteJson(String input, String AR_URL, String category) throws IOException {
        CompleteJson simpleJson;
		switch(category) {
       case "SensorListSchema_Simplified":
    	 simpleJson = new SimplifiedSensorList();
    	 break;
       case "MultiSensorListSchema_Simplified":
       	 simpleJson = new SimplifiedMultiSensor();
    	 break; 
       case "SensorSchema_Simplified":
      	 simpleJson = new SimplifiedSensor();
      	 break;
       default:
    	   log.error("Type of simplified JSON not supported");
           throw new WebApplicationException("ERROR: This type of simplified JSON is not supported", 500);		
		}
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Cache<String, JsonObject> cache = Cache.getCache(Setup.timeToLive, Setup.cacheTimer, Setup.maxItems);
		String resourceId = getResourceId(input);
		JsonObject inputJson = gson.fromJson(input, JsonObject.class);
		JsonObject missingObject=cache.get(resourceId);
		JsonObject completeJson = null;
		if (missingObject!=null) {
			completeJson = simpleJson.completeFields(missingObject, inputJson);
			log.debug("Complete JSON for this resource obtained from cache");
			return completeJson;
			

		}
		log.debug("Complete JSON for this resource not in cache");
		
		try {
			URL uri = new URL(AR_URL+resourceId);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");


			conn.setRequestProperty("Accept", "application/json");

			int code=conn.getResponseCode();
			if (code != HttpURLConnection.HTTP_OK) {

				throw new RuntimeException(Integer.toString(code));

			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = br.lines().collect(Collectors.joining());
			missingObject=simpleJson.parseEntireJson(output);
			completeJson = simpleJson.completeFields(missingObject,inputJson);





			//Server answer
			while ((output = br.readLine()) != null) {

			}	        
			conn.disconnect();
			//	    Store object in cache
			cache.put(resourceId,missingObject);    
			return completeJson;
		}
//     This catch is implemented because otherwise WebApplicationExceptions are treated as RuntimeExceptions,
//		and processed as such.
		catch (WebApplicationException e) {
			throw new WebApplicationException(Response.status(500).entity(e.getMessage()).build());
		}
		catch (MalformedURLException e) {
			log.error("Could not connect to Assets Registry: "+e.getMessage());
			e.printStackTrace();
			throw new MalformedURLException("MalformedURL");
		}
		catch (RuntimeException e) {
			e.printStackTrace();
			log.error("Could not obtain resource from the Assets Registry: "+e.getMessage());
			throw new WebApplicationException(Server.AR_RuntimeException);
		}

	}
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
		// TODO Auto-generated method stub
		return null;
	}





	

	
	public static String getResourceId (String json) {
		Gson gson = new Gson();
		JsonObject inputJson = gson.fromJson(json, JsonObject.class);
		String resourceId = inputJson.get("resourceId").getAsString();
		if (resourceId.equals(null)) {
			log.error("The JSON document does not have the \"resourceId\" key in its root");
		}
		return resourceId;
	}
//	A method for MANUALLY substituting the resourceId with the resourceUrn value.
	
}
