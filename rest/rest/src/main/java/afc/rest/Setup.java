package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;




public class Setup {
     
	 public static String json="{'resourceId':'sensor002','resourceType':'air_sensor','resourceUrn':'urn:afc:AS05:environmentalObservations:ROTECH:sensor002','latitude':44.224593,'longitude':11.941962,'altitude':0.0,'observations':[{'observedProperty':'air_temperature','uom':'http://qudt.org/vocab/unit/DEG_C','accuracy':0.1,'propertyId':94,'min_value':-20.0,'max_value':70.0},{'observedProperty':'air_humidity','uom':'http://qudt.org/vocab/unit/PERCENT','accuracy':1.0,'propertyId':95,'min_value':0.0,'max_value':100.0}],'supportedProtocol':'MQTT','hardwareVersion':'1.0','softwareVersion':'1.0','firmwareVersion':'1.0'}";
	 public static String json2= "[{\"name\": \"Definitions\", \"type\":\"Collar\", \"isSimple\":false},{\"name\": \"CollarListSchema\", \"type\":\"Sensor\", \"isSimple\":true, \"missingFields\": {\"location\":\"\",\"result\":{\"uom\":\"\"}}}, {\"name\": \"CollarSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"GatewayListSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"RegionListSchema\", \"type\":\"Sensor\", \"isSimple\":true}, {\"name\": \"RegionSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Complete\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Simplified\", \"type\":\"Sensor\", \"isSimple\":true} ]";  
	 public static String json3= "[{'name': 'Definitions', 'type':'Collar', 'isSimple':false},{'name': 'CollarListSchema', 'type':'Collar', 'isSimple':true, 'missingFields': {'location':'','result':{'uom':''}, 'resourceUrn':''}}, {'name': 'CollarSchema', 'type':'Sensor', 'isSimple':true},{'name': 'GatewayListSchema', 'type':'Sensor', 'isSimple':true},{'name': 'RegionListSchema', 'type':'Region', 'isSimple':true}, {'name': 'RegionSchema', 'type':'Region', 'isSimple':true},{'name': 'SensorListSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Simplified', 'type':'Sensor', 'isSimple':true,'missingFields': {'location':'','result':{'uom':''}, 'resourceUrn':''} },{'name': 'SensorSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorSchema_Simplified', 'type':'Sensor', 'isSimple':true},{'name': 'MultiSensorListSchema', 'type':'Sensor', 'isSimple':true}  ]";  

	public static void loadSchemasInfo(String input){ 
  
	     
        try {
        	System.out.println(json3);
        	Gson gson = new Gson();
				System.out.println("Cree objects");
			    java.lang.reflect.Type listOfSchemaObject = new TypeToken<ArrayList<Schema>>() {}.getType();
				System.out.println("Antes de deserializar");
			    SchemaSet.schemas = gson.fromJson(json3.toString(), listOfSchemaObject);
			    System.out.println(SchemaSet.schemas.size());
			    System.out.println(SchemaSet.schemas.get(0).getName());
			    System.out.println(SchemaSet.schemas.get(1).getIsSimple());
			    System.out.println(SchemaSet.schemas.get(1).getType());
			    System.out.println(SchemaSet.schemas.get(1).getMissingFields().toString());
		         

				
	}
        catch(JsonParseException e) {
        	e.printStackTrace();
        }
 
	
		}
	 public static  Set <Entry<String, JsonElement>> parseObject(JsonElement token,  Set<Entry<String, JsonElement>> registryJSON) {
		JsonObject jsonObject= token.getAsJsonObject();
		registryJSON.addAll(jsonObject.entrySet());
		 for (String key:jsonObject.keySet()) {
			 JsonElement token2 = jsonObject.get(key);
			 if(token2.isJsonObject()) {
				registryJSON= parseObject(token2, registryJSON);
				
			  }
			
		 }
		 return registryJSON;
	 }
	 public static void parseArray () {}
	 public static void parseEntireJson(JsonObject missingFields, String json) throws IOException {
		 Gson gson = new Gson();
		 
		  JsonObject missingFieldsCopy = gson.fromJson(missingFields , JsonObject.class);
		 Set<Entry<String, JsonElement>> registryJSON = null;
		 try {
			 JsonElement jsonTree=JsonParser.parseString(json);
			registryJSON= parseObject(jsonTree, registryJSON);
			/* if (jsonTree.isJsonObject()) {
				 JsonObject jsonObject =jsonTree.getAsJsonObject();
				 
					 System.out.println("KEY SETS: "+jsonObject.keySet());
					 registryJSON=jsonObject.entrySet();
					 for (String key:jsonObject.keySet()) {
						JsonElement token=jsonObject.get(key); 
					  if(token.isJsonObject()) {
						JsonObject jsonObject2=token.getAsJsonObject();
						 System.out.println("KEY SETS: "+jsonObject2.keySet());
						 registryJSON.addAll(jsonObject2.entrySet());
						 for (String key2:jsonObject2.keySet()) {
								JsonElement token2=jsonObject.get(key2); 
								if (token2.isJsonObject()) {
							    JsonObject jsonObject3=token2.getAsJsonObject();	
							    registryJSON.addAll(jsonObject3.entrySet());

								};
						 

						 }
						  }
					  
						

						  }
					 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! TODOS LOS FIELDS!!!!!!!!!!!!!!!!!!!!!"+registryJSON.toString());
					
				 }*/
			 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! TODOS LOS FIELDS!!!!!!!!!!!!!!!!!!!!!"+registryJSON.toString());
		 for(String key:missingFields.keySet()) {
				 
			 JsonElement token = missingFields.get(key);
			 if (token.isJsonObject()) {
				 JsonObject	missingFields2 = token.getAsJsonObject();
				 JsonObject	missingFields2Copy = gson.fromJson(missingFields2 , JsonObject.class);
				
			


				 for(String key2:missingFields2.keySet()) {
					 Iterator<Entry<String, JsonElement>> i =registryJSON.iterator();
					 while(i.hasNext()) {
						 Entry<String, JsonElement> pair2 = i.next();
						  if (key2.equals(pair2.getKey())) {
                          					 
						 missingFields2Copy.remove(key2);
						 missingFields2Copy.add(pair2.getKey(),pair2.getValue());
						 break;
						 
					 }
				 }	 
					 
				 }	
				 missingFieldsCopy.remove(key);
				 missingFieldsCopy.add(key, missingFields2Copy);
				 
				 }
				 Iterator<Entry<String, JsonElement>> i =registryJSON.iterator();
				 while(i.hasNext()) {
					 Entry<String, JsonElement> pair = i.next();
					  if (key.equals(pair.getKey())) {
				 
					 missingFieldsCopy.remove(key);
					 missingFieldsCopy.add(pair.getKey(),pair.getValue());
					 break;
				 }
			 }	 
				 
			
			
			 }
		 System.out.println("MISSING FIELDS CUMPLIMENTED: "+ missingFieldsCopy.toString());
		 }
		 
		 catch(JsonParseException e) {e.printStackTrace();
		 }
		 
	 }
		 
		/* 
	try {
		
		JsonElement jsonTree = JsonParser.parseString(json);
		if (jsonTree.isJsonObject()) {
		 JsonObject jsonObject =jsonTree.getAsJsonObject();
		
		 while (!jsonObject.keySet().isEmpty()) {
			 System.out.println("KEY SETS: "+jsonObject.keySet());
			 for (String key:jsonObject.keySet()) {
			while (!missingFields.keySet().isEmpty()) {
				System.out.println("KEY SETS DE MISSING: "+missingFields.keySet());
			 for(String keyB:missingFields.keySet()) {
				 
				 JsonElement token = missingFields.get(keyB);
				 if (token.isJsonObject()) {
				JsonObject	missingFields2 = token.getAsJsonObject();
				 
			 
				 for(String keyB2:missingFields2.keySet()) {
					 if (key.equals(keyB2)) {
						 missingFields2.remove(key);
						 missingFields2.add(key, jsonObject.get(key));
					 }
				 }	 
					 
				 }	 
			 if (key.equals(keyB)) {
				 missingFields.remove(key);
				 missingFields.add(key, jsonObject.get(key));
			 }
			
			 }
			 }
			 }
			 }
		 }
		
		
	}
	
	catch(JsonParseException e) {e.printStackTrace();}
	 }
	/* JsonReader jsonReader = new JsonReader(new StringReader(json));
	        jsonReader.setLenient(true);

	        try {
	            	            while (true) 
	            {    System.out.println("HAS NEXT");
	                JsonToken nextToken = jsonReader.peek();
	                System.out.println("next token: "+nextToken);
	             
	           
	                if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
	                	 
	                    jsonReader.beginObject();
	 
	                }
	                else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
	                	 
	                    jsonReader.beginArray();
	 
	                }
	                else if (JsonToken.NAME.equals(nextToken)) {
	 
	                    String name = jsonReader.nextName();
	                    for (String key :missingFields.keySet()) {
	    					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+key);
	    					if (name.equals(key)) {
	    						Gson gson=new Gson();
	    						JsonToken valueToken=jsonReader.peek();
	    						if (JsonToken.STRING.equals(valueToken)) {
	    							 
	    		                    String value = jsonReader.nextString();
	    		                    JsonElement element = gson.toJsonTree(value,String.class);
	    		                  	missingFields.remove(name);
	    		                  	missingFields.add(name, element);
	    		                  	
	    		                    System.out.println("Token Value >>>> " + value);
	    		 
	    		                } else if (JsonToken.NUMBER.equals(valueToken)) {
	    		 
	    		                    double value = jsonReader.nextDouble();
	    		                    JsonElement element = gson.toJsonTree(value,Double.class);
	    		                  	missingFields.remove(name);
	    		                  	missingFields.add(name, element);
	    		                    System.out.println("Token Value >>>> " + value);
	    		 
	    		                } 
	    		                else if (JsonToken.NULL.equals(valueToken)) {
	    		 
	    		                    jsonReader.nextNull();
	    		                    JsonElement element = gson.toJsonTree("",String.class);
	    		                  	missingFields.remove(name);
	    		                  	missingFields.add(name, element);
	    		                    System.out.println("Token Value >>>> null");
	    		                     
	    		                }
	    						
	    					}
	    					System.out.println(missingFields.entrySet().toString());
	                    }
	                
	                    	
	                    for (Entry<String, JsonElement> entry : missingFields.entrySet()) {
	    					System.out.println("Entry Set: "+entry.getKey().toString());
	    					if (name.equals(entry.getKey().toString())) {
	    						JsonToken valueToken=jsonReader.peek();
	    						if (JsonToken.STRING.equals(valueToken)) {
	    							 
	    		                    String value = jsonReader.nextString();
	    		                    missingFields.
	    		                    entry.setValue(value)
	    		                    System.out.println("Token Value >>>> " + value);
	    		 
	    		                } else if (JsonToken.NUMBER.equals(valueToken)) {
	    		 
	    		                    double value = jsonReader.nextDouble();
	    		                    System.out.println("Token Value >>>> " + value);
	    		 
	    		                } 
	    		                else if (JsonToken.NULL.equals(valueToken)) {
	    		 
	    		                    jsonReader.nextNull();
	    		                    System.out.println("Token Value >>>> null");
	    		                     
	    		                }
	    		                
	    					}
    					 Iterator iterator=missingFields.entrySet().iterator();
	    					 while (iterator.hasNext()) {
	    				
	    							 System.out.println("Token KEY >>>> " + name);
	    				}
	                   
                   
	            
	 
	                } else if (JsonToken.STRING.equals(nextToken)) {
	 
	                    String value = jsonReader.nextString();
	                    System.out.println("Token Value >>>> " + value);
	 
	                } else if (JsonToken.NUMBER.equals(nextToken)) {
	 
	                    double value = jsonReader.nextDouble();
	                    System.out.println("Token Value >>>> " + value);
	 
	                } 
	                else if (JsonToken.NULL.equals(nextToken)) {
	 
	                    jsonReader.nextNull();
	                    System.out.println("Token Value >>>> null");
	                     
	                } 
	                
	                else if (JsonToken.END_DOCUMENT.equals(nextToken)){
	                	break;
	                }
	                
	                nextToken = jsonReader.peek();

	           	 
					if (JsonToken.END_OBJECT.equals(nextToken)){
	                	jsonReader.endObject();
	                	
	                }
	               

					 if   (JsonToken.END_ARRAY.equals(nextToken)){
	                	jsonReader.endArray();
	                }
					
	            }
	 } catch (IOException e) {
	        e.printStackTrace();
	    } 

			     finally   {    jsonReader.close();
			     }

        }*/
	 public static void /*HashMap<String, String>*/ completeJson(JsonObject missingFields, String json ) {
		  HashMap<String, String> missingValues = new HashMap<String, String>();
		  try {
		  URL uri = new URL("https://rest.afarcloud.smartarch.cz/storage/rest/registry/getSensor/sensor002");
	        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
        	  System.out.println("Antes de content type");

	        conn.setRequestProperty("Content-Type", "application/json");
        	  System.out.println("Desp de content type");

	        
	        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode());
	        }
		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		        String output = br.lines().collect(Collectors.joining());
		        parseEntireJson(missingFields,output);
		       

		        
		        JsonElement jsonTree = JsonParser.parseString(json);
				if(jsonTree.isJsonObject()){
				JsonObject jsonObject = jsonTree.getAsJsonObject();
				for (Entry<String, JsonElement> a : missingFields.entrySet()) {
					System.out.println("Entry Set: "+a.getKey().toString()+a.getValue().toString());
				}
					}
				
		
				


		        
	
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

	 }
}