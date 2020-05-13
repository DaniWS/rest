/* Copyright 2018-2021 Universidad Politécnica de Madrid (UPM).
 *
 * Authors:
 *    Daniel Vilela García
 *    José-Fernan Martínez Ortega
 *    Vicente Hernández Díaz
 * 
 * This software is distributed under a dual-license scheme:
 *
 * - For academic uses: Licensed under GNU Affero General Public License as
 *                      published by the Free Software Foundation, either
 *                      version 3 of the License, or (at your option) any
 *                      later version.
 * 
 * - For any other use: Licensed under the Apache License, Version 2.0.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * You can get a copy of the license terms in licences/LICENSE.
 * 
 */
/** 
 * This is the parent class for completing simplified versions of the JSON documents. It contains the common methods to all
 * simplified JSON classes
 */
package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

public class SimplifiedJson {


	private static final Logger log = Logger.getLogger(SimplifiedJson.class);


	// This is the main method of the class, from where the rest of the methods are called.
	public static JsonObject getCompleteJson(String input, String AR_URL, String category) throws IOException {
		//			Checks the category of the request, instantiating the corresponding class.
		SimplifiedJson simpleJson;
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
		case "Alarm":
			simpleJson = new Alarm();  
			break;
		default:
			log.error("Type of simplified JSON not supported");
			throw new WebApplicationException("ERROR: This type of simplified JSON is not supported", 500);		
		}
		Gson gson = new Gson();
		//			Check the if the resource is already cached.
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
			missingObject=simpleJson.parseRegistryJson(output);
			completeJson = simpleJson.completeFields(missingObject,inputJson);





			//Server answer
			while ((output = br.readLine()) != null) {

			}	        
			conn.disconnect();
			//	    Store object in cache
			cache.put(resourceId,missingObject);    
			return completeJson;
		}
		//	     This catch is implemented because otherwise WebApplicationExceptions are treated as RuntimeExceptions,
		//			and processed as such.
		catch (WebApplicationException e) {
			throw new WebApplicationException(Response.status(500).entity(e.getMessage()).build());
		}
		catch (MalformedURLException e) {
			log.error("Could not connect to Assets Registry: "+e.getMessage());
			throw new MalformedURLException("MalformedURL");
		}
		catch (RuntimeException e) {
			log.error("Could not obtain resource from the Assets Registry: "+e.getMessage());
			throw new WebApplicationException(Response.status(500).entity("ERROR: The specified resourceId might not be registered in the Assets Registry").build());


			//				throw new WebApplicationException(Server.AR_RuntimeException);
		}

	}





	//  This method obtain the required information from The Assets Registry
	public JsonObject parseRegistryJson(String registrationJson) throws IOException {
		JsonObject missingFields = new JsonObject();

		HashMap<String, JsonElement> registryJSON = new HashMap<String, JsonElement>();
		try {
			JsonElement jsonTree=JsonParser.parseString(registrationJson);

			//		We need to MANUALLY add the resourceUrn field, since it will substitute the resourceId value
			registryJSON= this.parseObject(jsonTree, registryJSON, 0);

			missingFields=this.buildMissingJson(registryJSON);

			System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFields.toString());

		}

		catch(JsonParseException e) {e.printStackTrace();
		}
		return missingFields;

	}
	//	Parses a JSON object recursively and extract the required fields to a key-value Hash Map.
	public  HashMap <String, JsonElement> parseObject(JsonElement token,  HashMap <String, JsonElement> registryJSON, int counter)  {
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





	//	 A method that build a the missing JSON structure to fill from the 'registryJson' Hash Map.
	//   This is the object to be cached in memory.	

	public JsonObject buildMissingJson(HashMap<String, JsonElement> registryJSON) {
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

	//	This method is overridden by the child classes.

	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
		// TODO Auto-generated method stub
		return null;
	}







	//	A method for obtaining the Resource Id of the asset.
	public static String getResourceId (String json) {
		Gson gson = new Gson();
		JsonObject inputJson = gson.fromJson(json, JsonObject.class);
		if (inputJson.has("resourceId")){
		String resourceId = inputJson.get("resourceId").getAsString();
		return resourceId;
		}
		else {
			Iterator<String> i = inputJson.keySet().iterator();
			while (i.hasNext()) {
				String key=i.next();
				if (inputJson.get(key).isJsonObject()) {
					JsonObject object = inputJson.getAsJsonObject(key);
					
				return	getResourceId(object.toString());
					

			}
				
			}
			log.error("Cannot obtain the \"resourceId\" key");
			throw new WebApplicationException("ERROR: Cannot obtain the \"resourceId\" key", 500);

		
		}
		
	
		}
		
	}

