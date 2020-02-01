package afc.rest;

import org.glassfish.grizzly.http.server.HttpServer;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import org.glassfish.grizzly.http.server.ServerConfiguration;
//import org.glassfish.grizzly.servlet.ServletRegistration;
//import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.jackson.JacksonFeature;
//import org.glassfish.jersey.grizzly2.*;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.grizzly.servlet.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.swagger.jaxrs.config.BeanConfig;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;


/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://0.0.0.0:8080/";
//	public static JsonSchema AggregationMthroughGatewaySchema_SLS, CollarSchema, CollarSchemaList, Definitions,RegionSchema,RegionSchemaList,SensorAccumulatedMeasurements_Simplified,SimpleMeasurementSchema_Simplified,SimpleMeasurementSchema_SLS,VariousMfromMultiSensorSchema_SLS,VariousMfromSensorSchema_SLS;
	static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
//	private static final String[] schemas = new String [] {"AggregationMthroughGatewaySchema_SLS","CollarSchema","CollarSchemaList","Definitions","RegionSchema","RegionSchemaList","SensorAccumulatedMeasurements_Simplified","SimpleMeasurementSchema_Simplified","SimpleMeasurementSchema_SLS","VariousMfromMultiSensorSchema_SLS","VariousMfromSensorSchema_SLS"};
   private static final  ArrayList<String> schemas = new ArrayList<>(Arrays.asList("AggregationMthroughGatewaySchema_SLS","CollarSchema","CollarSchemaList","Definitions","RegionSchema","RegionSchemaList","SensorAccumulatedMeasurements_Simplified","SimpleMeasurementSchema_Simplified","SimpleMeasurementSchema_SLS","VariousMfromMultiSensorSchema_SLS","VariousMfromSensorSchema_SLS"));
//   private static  ArrayList<JsonSchema> jsonSchemas = new ArrayList<>(List.of(AggregationMthroughGatewaySchema_SLS, CollarSchema, CollarSchemaList, Definitions,RegionSchema,RegionSchemaList,SensorAccumulatedMeasurements_Simplified,SimpleMeasurementSchema_Simplified,SimpleMeasurementSchema_SLS,VariousMfromMultiSensorSchema_SLS,VariousMfromSensorSchema_SLS));
  protected static HashMap<String, JsonSchema> jsonSchemas= new HashMap<>();
   
   
   
    
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer getServerLookup() {

    	String resources = "afc.rest";

    	BeanConfig beanConfig = new BeanConfig();

    	beanConfig.setVersion("1.0.1");

    	beanConfig.setSchemes(new String[] { "http" });

    	beanConfig.setBasePath("");

    	beanConfig.setResourcePackage(resources);

    	beanConfig.setScan(true);

    	final ResourceConfig resourceConfig = new ResourceConfig();

    	resourceConfig.packages(resources);

    	resourceConfig.register(io.swagger.jaxrs.listing.ApiListingResource.class);

    	resourceConfig.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

    	resourceConfig.register(JacksonFeature.class);

    	resourceConfig.register(JacksonJsonProvider.class);
    	 
    	return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);

    	}
  
    /**
     * Main method.
     * @param args
     * @throws IOException
     * @throws ProcessingException 
     */
    public static void main(String[] args) throws IOException, ProcessingException {
        final HttpServer server = getServerLookup();
        server.start();
        ClassLoader loader = Main.class.getClassLoader();

        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        CLStaticHttpHandler schemasHandler = new CLStaticHttpHandler(loader, "schemas/");
        String log4jConfPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"properties"+File.separator+"log4j.properties";

        
        PropertyConfigurator.configure(log4jConfPath);

        docsHandler.setFileCacheEnabled(false);
        schemasHandler.setFileCacheEnabled(true);

        ServerConfiguration cfg = server.getServerConfiguration();

        cfg.addHttpHandler(docsHandler, "/docs/");
        cfg.addHttpHandler(schemasHandler, "/schemas/");
        
        for (String s: schemas) {
        	String filename=s+".json";	
        	FileUtils.copyURLToFile(        		  
        			new URL("http://0.0.0.0:8080/schemas/"+filename), 
        			new File("src/main/resources/localSchemas/"+filename));
        	jsonSchemas.put(s, factory.getJsonSchema("resource:/localSchemas/"+filename));
        	System.out.println(jsonSchemas.get(s));
        	System.out.println(s);
        	

        };
        
       
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        

        
        
        System.in.read();
        server.shutdownNow();
    }
}

