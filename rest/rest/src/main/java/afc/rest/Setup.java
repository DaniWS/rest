package afc.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;



public class Setup {
    
	 public static String json2= "[{'name': 'Definitions', 'type':'Collar', 'isSimple':false},{'name': 'CollarListSchema', 'type':'Sensor', 'isSimple':true}, {'name': 'CollarSchema', 'type':'Sensor', 'isSimple':true},{'name': 'GatewayListSchema', 'type':'Sensor', 'isSimple':true},{'name': 'RegionListSchema', 'type':'Sensor', 'isSimple':true}, {'name': 'RegionSchema', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Complete', 'type':'Sensor', 'isSimple':true},{'name': 'SensorListSchema_Simplified', 'type':'Sensor', 'isSimple':true} ]";  
	 
	public static void loadSchemasInfo(String input){ 

	     
        try {
        	Gson gson = new Gson();
				System.out.println("Cree objects");
			    java.lang.reflect.Type listOfSchemaObject = new TypeToken<ArrayList<Schema>>() {}.getType();
				System.out.println("Antes de deserializar");
			    SchemaSet.schemas = gson.fromJson(json2.toString(), listOfSchemaObject);
			    System.out.println(SchemaSet.schemas.size());
			    System.out.println(SchemaSet.schemas.get(0).getName());
			    System.out.println(SchemaSet.schemas.get(1).getIsSimple());
			    System.out.println(SchemaSet.schemas.get(1).getType());


				
	}
        catch(JsonParseException e) {
        	e.printStackTrace();
        }
 
	
		}
        }