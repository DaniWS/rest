package afc.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;




public class Setup {
     
//	 public static String json="{'resourceId':'sensor002','resourceType':'air_sensor','resourceUrn':'urn:afc:AS05:environmentalObservations:ROTECH:sensor002','latitude':44.224593,'longitude':11.941962,'altitude':0.0,'observations':[{'observedProperty':'air_temperature','uom':'http://qudt.org/vocab/unit/DEG_C','accuracy':0.1,'propertyId':94,'min_value':-20.0,'max_value':70.0},{'observedProperty':'air_humidity','uom':'http://qudt.org/vocab/unit/PERCENT','accuracy':1.0,'propertyId':95,'min_value':0.0,'max_value':100.0}],'supportedProtocol':'MQTT','hardwareVersion':'1.0','softwareVersion':'1.0','firmwareVersion':'1.0'}";
//	 public static String json2= "[{\"name\": \"Definitions\", \"type\":\"Collar\", \"isSimple\":false},{\"name\": \"CollarListSchema\", \"type\":\"Sensor\", \"isSimple\":true, \"missingFields\": {\"location\":\"\",\"result\":{\"uom\":\"\"}}}, {\"name\": \"CollarSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"GatewayListSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"RegionListSchema\", \"type\":\"Sensor\", \"isSimple\":true}, {\"name\": \"RegionSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Complete\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Simplified\", \"type\":\"Sensor\", \"isSimple\":true} ]";  
//	 public static String json3= "[{'name': 'Definitions', 'type':'Collar', 'isSimple':false},{'name': 'CollarListSchema', 'type':'Collar', 'isSimple':true, 'missingFields': {'location':'','result':{'uom':''}, 'resourceUrn':''}}, {'name': 'CollarSchema', 'type':'Sensor', 'isSimple':true},{'name': 'GatewayListSchema', 'type':'Sensor', 'isSimple':true},{'name': 'RegionListSchema', 'type':'Region', 'isSimple':true}, {'name': 'RegionSchema', 'type':'Region', 'isSimple':true},{'name': 'SensorListSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Simplified', 'type':'Sensor', 'isSimple':true,'missingFields': {'location':{'longitude':'','latitude':'','altitude':2},'result':{'uom':''}, 'resourceUrn':''} },{'name': 'SensorSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorSchema_Simplified', 'type':'Sensor', 'isSimple':true},{'name': 'MultiSensorListSchema', 'type':'Sensor', 'isSimple':true}  ]";  
     public static final String schemasInfo = "SchemasInfo";
     public static final String localSchemasPath = "src/main/resources/localSchemas/";
	public static void loadSchemasInfo(String schemaURI) throws MalformedURLException, IOException{ 
  
	     
        try {
        	  Gson gson = new Gson();
        	  FileUtils.copyURLToFile(          
        		        new URL(schemaURI+schemasInfo+".json"),
        		        new File(localSchemasPath+schemasInfo+".json"));
        	  BufferedReader bufferedReader = new BufferedReader(new FileReader(localSchemasPath+schemasInfo+".json"));

              JsonArray json = gson.fromJson(bufferedReader, JsonArray.class);
                 
			    java.lang.reflect.Type listOfSchemaObject = new TypeToken<ArrayList<Schema>>() {}.getType();
			    SchemaSet.schemas = gson.fromJson(json, listOfSchemaObject);
		
		

				
	}
        catch(JsonParseException e) {
        	e.printStackTrace();
        }
 
	
		}
	// Method to load the schemas: takes the schemas URI as an argument.
	public static void loadSchemas(String schemaURI) throws MalformedURLException, IOException, ProcessingException {
	  JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	  for (Schema s: SchemaSet.schemas) {		    
	        String filename=s.getName()+".json";
	       
	        FileUtils.copyURLToFile(          
	        new URL(schemaURI+filename),
	        new File(localSchemasPath+filename));
//	       Avoids loading Definition as a schema to prevent false validations.
	       
	        if (!s.getName().equals("Definitions")) {
	               s.setSchema(factory.getJsonSchema("resource:/localSchemas/"+filename));
//	               SchemaSet.schemas.add(new Schema(factory.getJsonSchema("resource:/localSchemas/"+filename),0,s, null, s));
	               }
	       
	        }


	       };
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
// Breaks the operation because the match for the corresponding key has been found. There's no need to continue iterating
						break;	
						  }
					 }
						  
				 }	 
				 
			}
			return missingFieldsCopy;
		}

	 public static JsonObject completeJson(JsonObject missingFields, String input ) {
//		  HashMap<String, String> missingValues = new HashMap<String, String>();
		  Gson gson = new Gson();
		  JsonObject inputJson = gson.fromJson(input, JsonObject.class);
		  String resourceId = inputJson.get("resourceId").getAsString();
		  try {
		  URL uri = new URL("https://rest.afarcloud.smartarch.cz/storage/rest/registry/getSensor/"+resourceId);
	        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");

	        conn.setRequestProperty("Content-Type", "application/json");

	        
	        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode());
	        }
		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        String output = br.lines().collect(Collectors.joining());
		       JsonObject missingObject=parseEntireJson(missingFields,output);
		       JsonObject completedJson=completeFields(missingFields,inputJson);
		     
		

		        
/*		        JsonElement jsonTree = JsonParser.parseString(json);
				if(jsonTree.isJsonObject()){
				JsonObject jsonObject = jsonTree.getAsJsonObject();
				for (Entry<String, JsonElement> a : missingFields.entrySet()) {
					System.out.println("Entry Set: "+a.getKey().toString()+a.getValue().toString());
				}
					}
*/				
		
				


		        
	
	        //Server answer
	        System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	           
	        }	        
	        conn.disconnect();
		
		}
		catch (MalformedURLException e) {
	    e.printStackTrace();  
	    } catch (IOException e) {
e.printStackTrace();	    }
		return inputJson;

	 }
	 public static JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
	        System.out.println(inputJson.toString()+"!!2!!2!!!!!22!!2!2!2!!!!!!!!!!!!!!!!!");		
	        System.out.println(inputJson.keySet().toString()+" keys");		

		 for (String missingKey:missingObject.keySet()) {
		       for (String key:inputJson.keySet()) {
		    	   		   if (missingKey.equals(key)){
		    			   if (inputJson.get(key).isJsonObject()&&missingObject.get(key).isJsonObject()){
		    				   JsonObject localInputObject = inputJson.get(key).getAsJsonObject();
		    				   JsonObject localMissinObject = inputJson.get(key).getAsJsonObject();
		    				   inputJson=completeFields(localInputObject, localMissinObject);
		    				   break;
		    			   }
		    			   
		    				  
		    			   
		    		  }
		    	   		   else {
		    		 inputJson.add(missingKey, missingObject.get(missingKey));
		    	   		   }
		    	   }
		       }
	       System.out.println(inputJson);
	       return inputJson;
	 };
}