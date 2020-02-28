package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Session;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

// The class that implements the necessary methods for automatically completing simplified JSONs
public class CompleteJson {
	
//	A method that parses a JSON object finding the missing values, and returns a HashSet of all the "key-value" in the root, 
//	and the "key-value" pairs inside all objects and arrays, and so on... recursively 
	public static  Set <Entry<String, JsonElement>> parseObject(JsonElement token,  Set<Entry<String, JsonElement>> registryJSON, int counter) {
		 if (token.isJsonObject()) {
		 JsonObject jsonObject= token.getAsJsonObject();
		switch (counter) {
			case 0: 
   			registryJSON.addAll(jsonObject.entrySet());
   	


				counter++;
				break;
				
			default:
	
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
	
//	A method that parses the entire complete version of the received simplified JSON, and iterates over all objects recursively filling the missing values  

	 public static JsonObject parseEntireJson(JsonObject missingFields, String completeJson) throws IOException {
		 Gson gson = new Gson();

		  JsonObject missingFieldsCopy = gson.fromJson(missingFields , JsonObject.class);
		 Set<Entry<String, JsonElement>> registryJSON = new HashSet<Entry<String, JsonElement>>();
		 try {
			 JsonElement jsonTree=JsonParser.parseString(completeJson);
			 
			registryJSON= parseObject(jsonTree, registryJSON, 0);
		
		
            missingFieldsCopy=fillValues(registryJSON,missingFields,missingFieldsCopy, gson);

            System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFieldsCopy.toString());
           
		 }
		 
		 catch(JsonParseException e) {e.printStackTrace();
		 }
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
// A method to complete the simplified JSON obtaining the missing information from the Assets Registry
	 public static JsonObject getCompleteJson(JsonObject missingFields, String input ) throws RuntimeException, IOException {

		  Gson gson = new Gson();
		  JsonObject inputJson = gson.fromJson(input, JsonObject.class);
		  String resourceId = inputJson.get("resourceId").getAsString();
		  try {
		  URL uri = new URL("https://rest.afarcloud.smartarch.cz/storage/rest/registry/getSensor/"+resourceId);
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
		       JsonObject missingObject=parseEntireJson(missingFields,output);
		       JsonObject completeJson=completeFields(missingObject,inputJson,gson);
		     
			   			

		        
	
	        //Server answer
	        System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	           
	        }	        
	        conn.disconnect();
		return completeJson;
		}
		  
	
		catch (MalformedURLException e) {
						
	    e.printStackTrace();
	    throw new RuntimeException("MalformedURL");
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
		    				   JsonObject localMissinObject = inputJson.get(key).getAsJsonObject();
		    				   JsonObject localInputCopy=completeFields(localMissinObject,localInput,gson);
		    				   inputCopy.remove(key);
		    				   inputCopy.add(key, localInputCopy);
		    				   break;
		    			   }
		    			   
		    				  
		    			   
		    		  }
		    	   		   else {
		    	   	 
		    		 inputCopy.add(missingKey, missingObject.get(missingKey));
		    	   		   }
		    	   }
		       }
	       System.out.println(inputJson);
	       System.out.println(inputCopy);

	       return inputCopy;
	 };
		public Response sendTelemetry(String json, Request request, String category) {
			 try {
				    
			    	//Used for connectivity with the REST server
			        URL uri = new URL("http://10.0.43.139:8080/store/measures");
			        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			        conn.setDoOutput(true);
			        conn.setRequestMethod("POST");
		        	  System.out.println("Antes de content type");
	  
			        conn.setRequestProperty("Content-Type", "application/json");
		        	  System.out.println("Desp de content type");
	    
			      
			        String input = json;
			     
			
			        OutputStream os = conn.getOutputStream();
			        os.write(input.getBytes());
			        os.flush();
			        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			            throw new RuntimeException("Failed : HTTP error code : "
			                    + conn.getResponseCode());
			        }
//			        Code block for reading the output from the server
	/*		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			        String output;
			        
			        //Server answer
			        System.out.println("Output from Server .... \n");
			        while ((output = br.readLine()) != null) {
			            System.out.println(output);
			           
			        }
	*/		        
			        conn.disconnect();
			        Session session=request.getSession();
			    	return Response.status(200).entity(session.getIdInternal()+session.getTimestamp()).build();
			       
			    } catch (MalformedURLException e) {
			    	return Response.status(500).entity(e.getMessage()).build();  
			    } catch (IOException e) {
			    	return Response.status(500).entity(e.getMessage()).build();  
			    }

		}
}
