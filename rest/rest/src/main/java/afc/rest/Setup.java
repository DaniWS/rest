package afc.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
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
	//   Configuration parameters
	public static final String configFileName = "config.properties"; //Name of the configuration file
	public static final String ER_URI_prop = "environment_reporter_address"; //Environment Reporter's URI
	public static final String AR_URL_prop = "assets_registry_address"; //Assets Registry's URL
	public static final String timeToLiveProp = "TTL";// Cache's Time to live
	public static final String cacheTimerProp = "cache_timer";// Cache's Time to live
	public static final String maxItemProp = "max_items";// Maximum number of items on cache


	//	 Name of the JSON file that contains the information of the schemas
	public static final String schemasInfo = "SchemasInfo";
	public static final String localSchemasPath = "src/main/resources/localSchemas/";
	//   Assets Registry URL
	public static String AR_URL;
	//   Environment Reporter URI
	public static String ER_URI;
  
	//	CACHE parameters
    public static  long timeToLive;
    public static  long cacheTimer;
    public static  int maxItems;
	
    public static void loadProperties(String path) throws FileNotFoundException, IOException {
    	 try (InputStream input = new FileInputStream( path)) {

             Properties prop = new Properties();
             prop.load(input);
             ER_URI = prop.getProperty(ER_URI_prop);
             AR_URL = prop.get(AR_URL_prop).toString();
             timeToLive = Long.parseLong(prop.getProperty(timeToLiveProp, "72000"));
             cacheTimer = Long.parseLong(prop.getProperty(cacheTimerProp, "72000"));
             maxItems = Integer.parseInt(prop.getProperty(maxItemProp, "200"));
             System.out.println(ER_URI);
             System.out.println(AR_URL);
             
             
             
    }
    }
//    Method to load the JSON with the information about the schemas
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

			if (s.getType().equals("Alarm")) {
				s.setSchema(factory.getJsonSchema(schemaURI+filename));
				SchemaSet.alarmSchemas.add(s);

			}
			else if (!s.getType().equals("Definitions")) {
				s.setSchema(factory.getJsonSchema(schemaURI+filename));
				SchemaSet.telemetrySchemas.add(s);
				//	               SchemaSet.schemas.add(new Schema(factory.getJsonSchema("resource:/localSchemas/"+filename),0,s, null, s));
			}

		}


	};

}