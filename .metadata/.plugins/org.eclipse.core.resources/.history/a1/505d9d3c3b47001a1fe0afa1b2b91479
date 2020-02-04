package afc.rest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class SchemaLoader {
	protected static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	protected static ArrayList<String> schemas = new ArrayList<>(Arrays.asList("AggregationMthroughGatewaySchema_SLS","CollarSchema","CollarSchemaList","Definitions","RegionSchema","RegionSchemaList","SensorAccumulatedMeasurements_Simplified","SimpleMeasurementSchema_Simplified","SimpleMeasurementSchema_SLS","VariousMfromMultiSensorSchema_SLS","VariousMfromSensorSchema_SLS"));
	protected static HashMap<String, JsonSchema> jsonSchemas= new HashMap<>();
	
//	Method to load the schemas: takes the schemas URI as an argument.
public static void loadSchemas(String schemaURI) throws MalformedURLException, IOException, ProcessingException {
	   for (String s: schemas) {
       	String filename=s+".json";	
       	FileUtils.copyURLToFile(        		  
       			new URL(schemaURI+filename), 
       			new File("src/main/resources/localSchemas/"+filename));
       	jsonSchemas.put(s, factory.getJsonSchema("resource:/localSchemas/"+filename));
      
       	
 
       
        } 
        

       };	
	
}

