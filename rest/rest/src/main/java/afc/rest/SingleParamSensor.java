package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
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

public class SingleParamSensor extends SimplifiedJson {
	
	public SingleParamSensor() {}
	
	private static final Logger log = Logger.getLogger(Server.class);
    
	//	A method that parses a JSON object finding the missing values, and returns a HashSet of all the "key-value" in the root, 
	public static  Set <Entry<String, JsonElement>> parseObject(JsonElement token,  Set<Entry<String, JsonElement>> registryJSON, int counter)  {
		log.debug("Entered SingleParamMethod");
		if (token.isJsonObject()) {
			JsonObject jsonObject= token.getAsJsonObject();
			switch (counter) {
			case 0: 
//				Check if there is more than one observed property
				JsonElement observations = jsonObject.get("observations");
				if (observations!=null&&observations.isJsonArray()) {
					JsonArray obsArray = observations.getAsJsonArray();
				 if	(obsArray.size()!=1) {
					 log.error("The specified resource is not a single-parameter sensor");
				 	throw new WebApplicationException("The specified resource is not a single-parameter sensor", 500);
				 }
			
				
				}


				counter++;
				

			default:
// Fill the EntrySet with all the entries of the object.
				registryJSON.addAll (jsonObject.entrySet());	


			}



			if (!jsonObject.keySet().isEmpty()) {
				for (String key:jsonObject.keySet()) {
					JsonElement localToken = jsonObject.get(key);
					registryJSON=parseObject(localToken, registryJSON, counter);

				}
			}
		}
		else if (token.isJsonArray()) {
			JsonArray jsonArray =token.getAsJsonArray();

			
			for (JsonElement localToken: jsonArray) {

				registryJSON=parseObject(localToken, registryJSON, counter);
			}

		}
		return registryJSON;
	}
	//	A method that parses the entire registration JSON of the received simplified JSON, and iterates over all objects recursively filling the missing values  

	public static JsonObject parseEntireJson(JsonObject missingFields, String registrationJson) throws IOException {
		Gson gson = new Gson();

		JsonObject missingFieldsCopy = gson.fromJson(missingFields , JsonObject.class);
		Set<Entry<String, JsonElement>> registryJSON = new HashSet<Entry<String, JsonElement>>();
		try {
			JsonElement jsonTree=JsonParser.parseString(registrationJson);
			
			//		We need to MANUALLY add the resourceUrn field, since it will substitute the resourceId value
			missingFields.add("resourceUrn", null);       
			registryJSON= parseObject(jsonTree, registryJSON, 0);

			missingFieldsCopy=fillValues(registryJSON,missingFields,missingFieldsCopy, gson);
		
			System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFieldsCopy.toString());

		}

		catch(JsonParseException e) {e.printStackTrace();
		}
		System.out.println("REGISTRY_JSON: "+registryJSON);
		return missingFieldsCopy;

	}
	//	 A method that parses the JSON recursively filling the missing values of the \"missing values\" template, iterating recursively through every object,
	//	 and every object inside an object, and so on...
	public static JsonObject fillValues(Set <Entry<String, JsonElement>> registryJSON, JsonObject missingFields, JsonObject missingFieldsCopy, Gson gson) {

		for(String key:missingFields.keySet()) {

			JsonElement token = missingFields.get(key);

			if (token.isJsonObject()) {
				JsonObject	localMissingFields = token.getAsJsonObject();
				JsonObject	localMissingFieldsCopy = gson.fromJson(localMissingFields , JsonObject.class);
				missingFieldsCopy.remove(key);
				missingFieldsCopy.add(key, fillValues(registryJSON, localMissingFields, localMissingFieldsCopy, gson)); 
                  
			}
			else {
				Iterator<Entry<String, JsonElement>> i =registryJSON.iterator();
				while(i.hasNext()) {
					Entry<String, JsonElement> entry = i.next();
					if (key.equals(entry.getKey())) {


						missingFieldsCopy.add(key,entry.getValue());
					
						//Breaks the operation because the match for the corresponding key has been found. There's no need to continue iterating
						break;	
					}
				}

			}	 

		}
		return missingFieldsCopy;
	}
	// A method to complete the simplified JSON obtaining the missing information from the Assets Registry URI
	public static JsonObject getCompleteJson(JsonObject missingFields, String input, String AR_URL) throws IOException {

		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Cache<String, JsonObject> cache = Cache.getCache(Setup.timeToLive, Setup.cacheTimer, Setup.maxItems);
		String resourceId = getResourceId(input);
		JsonObject inputJson = gson.fromJson(input, JsonObject.class);
		JsonObject missingObject=cache.get(resourceId);
		JsonObject completeJson = null;
		if (missingObject!=null) {
			completeJson = completeFields(missingObject, inputJson, gson );
			completeJson = substituteResourceId(completeJson);
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
			missingObject=parseEntireJson(missingFields,output);
			completeJson = completeFields(missingObject,inputJson,gson);
            completeJson = substituteResourceId(completeJson);





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
	//	 A method that parses the simplified JSON filling the missing values from the \"missing values"\ template, iterating recursively through every object,
	//	 and every object inside an object, and so on...
	public static JsonObject completeFields(JsonObject missingObject, JsonObject inputJson, Gson gson) {

		JsonObject	inputCopy = gson.fromJson(inputJson , JsonObject.class);
		for (String missingKey:missingObject.keySet()) {
			for (String key:inputJson.keySet()) {
				if (missingKey.equals(key)){
					if (inputJson.get(key).isJsonObject()&&missingObject.get(key).isJsonObject()){
						JsonObject localInput = inputJson.get(key).getAsJsonObject();
						JsonObject localMissingObject = missingObject.get(key).getAsJsonObject();
						JsonObject localInputCopy=completeFields(localMissingObject,localInput,gson);
						inputCopy.remove(key);
						inputCopy.add(key, localInputCopy);
						break;
					}



				}
				else {
					if (inputCopy.get(missingKey)==null) {                    
						inputCopy.add(missingKey, missingObject.get(missingKey));
					}
				}
			}
		}
		
		return inputCopy;
	};
	
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
	public static JsonObject substituteResourceId (JsonObject completedJson) {
		    JsonElement resourceUrn = completedJson.get("resourceUrn");
		if (resourceUrn==null) {
           log.debug("The substitution of the 'resourceId' for the 'resourceUrn' wasn't performed");
		}
		else {
			completedJson.remove("resourceId");
			completedJson.add("resourceId",resourceUrn);
			completedJson.remove("resourceUrn");
			}
		return completedJson;
		}

}
