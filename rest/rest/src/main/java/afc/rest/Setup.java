package afc.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.grizzly.http.server.Request;

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
	 
}