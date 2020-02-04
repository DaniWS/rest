package afc.rest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class Schema implements Comparable <Schema> {
	protected static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	protected static ArrayList<String> schemasName = new ArrayList<>(Arrays.asList("Definitions","AggregationMthroughGatewaySchema_SLS","CollarSchema","CollarSchemaList","RegionSchema","RegionSchemaList","SensorAccumulatedMeasurements_Simplified","SimpleMeasurementSchema_Simplified","SimpleMeasurementSchema_SLS","VariousMfromMultiSensorSchema_SLS","VariousMfromSensorSchema_SLS"));
//	protected static HashMap<String, JsonSchema> jsonSchemas= new HashMap<>();
	protected static ArrayList<Schema> schemas= new ArrayList<>();
	
	 private JsonSchema schema;
     private int uso;
     private String name;
     // Constructor de la clase Schema
     protected Schema(JsonSchema schema, int uso, String name) {
         this.schema = schema;
         this.uso = uso;
         this.name = name;
         
     }
     public JsonSchema getSchema() {
			return schema;
		}
     public int getUso() {
			return uso;
		}
     public void setUso(int uso) {
			this.uso = uso;
		}
		 public void setName(String name) {
			this.name = name;
		}
     public String getName() {
			return name;
		}
		@Override
		public int compareTo(Schema o) {
			 if (uso > o.uso) {
	                return -1;
	            }
	            if (uso < o.uso) {
	                return 1;
	          }
			return 0;
		}
  
	
	
//	Method to load the schemas: takes the schemas URI as an argument.
public static void loadSchemas(String schemaURI) throws MalformedURLException, IOException, ProcessingException {
	   for (String s: schemasName) {
       	String filename=s+".json";	
       	FileUtils.copyURLToFile(        		  
       			new URL(schemaURI+filename), 
       			new File("src/main/resources/localSchemas/"+filename));
       	        if (!s.equals("Definitions")) {
       	        schemas.add(new Schema(factory.getJsonSchema("resource:/localSchemas/"+filename),0,s));
       	        }
              	
 
       
        } 

        System.out.println(schemas.size());

       };	
	
}
