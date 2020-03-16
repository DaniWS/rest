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

import com.github.fge.jsonschema.keyword.validator.draftv4.OneOfValidator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class SimplifiedMultiSensor extends CompleteJson{

	private static final Logger log = Logger.getLogger(Server.class);
	
	@Override
	public  HashMap <String, JsonElement> parseObject(JsonElement token, HashMap<String, JsonElement> registryJSON, int counter)  {
		log.debug("Entered MultiParamMethod");
		if (token.isJsonObject()) {
			JsonObject jsonObject= token.getAsJsonObject();
               switch (counter) {
               case 0: // Is the root object.
            	   
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
				 if	(obsArray.size()<=1) {
					 log.error("The specified resource is not a multi-parameter sensor");
				 	throw new WebApplicationException("The specified resource is not a multi-parameter sensor", 500);
				 }
                    counter++;
					for (JsonElement localToken: obsArray) {

						registryJSON=parseObject(localToken, registryJSON, counter);
				
				}
			}    
			
					
				else {
					log.error("Assets Registry: 'observations' must be a not-null array");
					throw new WebApplicationException("ERROR: Assets Registry: 'observations' must be a not-null array", 500);
					}
		         	      
                 break;
                 default: //Not the root object.
                   if (jsonObject.has("observedProperty")&&jsonObject.has("uom")) {
                	   registryJSON.put(jsonObject.get("observedProperty").getAsString(), jsonObject.get("uom"));
                	   return registryJSON;
                   }
                    

               }
               
		    }
		return registryJSON;
	}
    @Override
	public JsonObject parseEntireJson(String registrationJson) throws IOException {
		log.debug("Entered Multi Sensor Class");

		Gson gson = new Gson();
		JsonObject missingFields= new JsonObject();
		
		   HashMap<String, JsonElement> registryJSON = new HashMap<String, JsonElement>();
		try {
			JsonElement jsonTree=JsonParser.parseString(registrationJson); 
			
			registryJSON= parseObject(jsonTree, registryJSON, 0);
			System.out.println("REGISTRY_JSON: "+registryJSON);
			missingFields=buildMissingJson(registryJSON, gson);
		
			System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFields.toString());

		}

		catch(JsonParseException e) {e.printStackTrace();
		}
		return missingFields;

	}@Override
	//	 A method that parses the JSON recursively filling the missing values of the "missing values" object.
	public JsonObject buildMissingJson(HashMap<String, JsonElement> registryJSON, Gson gson) {
		log.debug("Entered Multi Sensor Class");

//		Check that the Map contains all required keys  
		  JsonObject missingFields= new JsonObject();
			JsonObject location = new JsonObject();	
		  JsonArray observations= new JsonArray();
		    Iterator<Map.Entry<String, JsonElement>> i =registryJSON.entrySet().iterator();
			while(i.hasNext()) {
				Entry<String, JsonElement> entry = i.next();
				String key = entry.getKey();
				if(key.matches("longitude||latitude||altitude")) {
				location.add(key, entry.getValue());
				}
				else if(key.matches("resourceId")) {
					missingFields.add(key, entry.getValue());
					}
				
				else {
					JsonObject obsProp = new JsonObject();
					obsProp.addProperty("observedProperty", key);
					obsProp.add("uom", entry.getValue());
					observations.add(obsProp);
				}
			}
			missingFields.add("observations", observations);
			missingFields.add("location", location);
			return missingFields;
			}
	
	/*
	// A method to complete the simplified JSON obtaining the missing information from the Assets Registry URI
	public JsonObject getCompleteJson(String input, String AR_URL) throws IOException {

		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Cache<String, JsonObject> cache = Cache.getCache(Setup.timeToLive, Setup.cacheTimer, Setup.maxItems);
		String resourceId = getResourceId(input);
		JsonObject inputJson = gson.fromJson(input, JsonObject.class);
		JsonObject missingObject=cache.get(resourceId);
		JsonObject completeJson = null;
		if (missingObject!=null) {
			completeJson = completeFields(missingObject, inputJson, gson );
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
			JsonObject missingFields = new JsonObject();       
			missingObject=this.parseEntireJson(output);
			completeJson = completeFields(missingObject,inputJson,gson);





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
	*/
	//	 A method that parses the simplified JSON filling the missing values from the \"missing values"\ template.

	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
		log.debug("Entered Multi Sensor Class");

		inputJson.add("location", missingObject.get("location"));
		inputJson.add("resourceId", missingObject.get("resourceId"));
		Iterator<JsonElement> j = inputJson.get("observations").getAsJsonArray().iterator();
		while(j.hasNext()){		
			JsonObject obsToFill = j.next().getAsJsonObject();
			Iterator<JsonElement> i = missingObject.get("observations").getAsJsonArray().iterator();
			while (i.hasNext()) {
				JsonObject obsMissing = i.next().getAsJsonObject();
                JsonElement obsPropToFill = obsToFill.get("observedProperty");
                JsonElement obsPropMissing = obsMissing.get("observedProperty");
                 if (obsPropToFill.equals(obsPropMissing)){
                	 obsToFill.get("result").getAsJsonObject().add("uom",obsMissing.get("uom"));
                	 break;
                 }
                
			}
		}	
                    
				return inputJson;
		}
		
		
}
