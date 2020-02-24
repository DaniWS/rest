package afc.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;



public class Setup {
    
	 public static String json2= "[{\"name\": \"Definitions\", \"type\":\"Collar\", \"isSimple\":false},{\"name\": \"CollarListSchema\", \"type\":\"Sensor\", \"isSimple\":true, \"missingFields\": {\"location\":\"\",\"result\":{\"uom\":\"\"}}}, {\"name\": \"CollarSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"GatewayListSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"RegionListSchema\", \"type\":\"Sensor\", \"isSimple\":true}, {\"name\": \"RegionSchema\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Complete\", \"type\":\"Sensor\", \"isSimple\":true},{\"name\": \"SensorListSchema_Simplified\", \"type\":\"Sensor\", \"isSimple\":true} ]";  
	 public static String json3= "[{'name': 'Definitions', 'type':'Collar', 'isSimple':false},{'name': 'CollarListSchema', 'type':'Sensor', 'isSimple':true, 'missingFields': {'location':'','result':{'uom':''}, 'resourceUrn':''}}, {'name': 'CollarSchema', 'type':'Sensor', 'isSimple':true},{'name': 'GatewayListSchema', 'type':'Sensor', 'isSimple':true},{'name': 'RegionListSchema', 'type':'Sensor', 'isSimple':true}, {'name': 'RegionSchema', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Simplified', 'type':'Sensor', 'isSimple':true} ]";  

	public static void loadSchemasInfo(String input){ 

	     
        try {
        	System.out.println(json2);
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
	 public static void parseEntireJson(String json) throws IOException {
		 JsonReader jsonReader = new JsonReader(new StringReader(json));
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
	                    System.out.println("Token KEY >>>> " + name);
	                    if JsonToken.NAME.equals(other)
	 
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

        }