package afc.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.apache.log4j.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.glassfish.grizzly.http.server.ServerConfiguration;
//import org.glassfish.grizzly.servlet.ServletRegistration;
//import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.jackson.JacksonFeature;
//import org.glassfish.jersey.grizzly2.*;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.grizzly.servlet.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.swagger.jaxrs.config.BeanConfig;

import java.io.File;
import java.io.IOException;
import java.net.URI;


/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/";
    private static final String JERSEY_SERVLET_CONTEXT_PATH = "";
      public static String regionListSchema; 	
      public static String sensorListSchema; 
      public static String collarListSchema; 
     
      //////// array de schemas ///////
    	
    	
    	public static ArrayList<String> schemas = new ArrayList<String>();
    	
    	
    	//////////////////////////////
    
    
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
   /* 
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in afc.rest package
        final ResourceConfig rc = new ResourceConfig().packages("afc.rest");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
*/
    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = getServerLookup();
        server.start();
        ClassLoader loader = Main.class.getClassLoader();


        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        String log4jConfPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"properties"+File.separator+"log4j.properties";
        String schemaPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator;
        String regionListJSON = schemaPath+File.separator+"regionList.json";
        String sensorListJSON = schemaPath+File.separator+"sensorList.json";
        String collarListJSON = schemaPath+File.separator+"collarList.json";
        
        
        PropertyConfigurator.configure(log4jConfPath);

        docsHandler.setFileCacheEnabled(false);

        ServerConfiguration cfg = server.getServerConfiguration();

        cfg.addHttpHandler(docsHandler, "/docs/");
      
        
        
         regionListSchema= new String(Files.readAllBytes(Paths.get(regionListJSON)));
         sensorListSchema= new String(Files.readAllBytes(Paths.get(sensorListJSON)));
         collarListSchema= new String(Files.readAllBytes(Paths.get(collarListJSON)));
         ///////
        schemas.add(regionListSchema);
        schemas.add(sensorListSchema);
        schemas.add(collarListSchema);
         //////
         
     //  System.out.println(regionListSchema);
     
      /*
        WebappContext context = new WebappContext("WebappContext", JERSEY_SERVLET_CONTEXT_PATH);
        ServletRegistration registration = context.addServlet("ServletContainer", ServletContainer.class);
        registration.setInitParameter(ServletContainer., 
                ResourceConfig.class.getName());
        registration.setInitParameter(ClassNamesResourceConfig.PROPERTY_CLASSNAMES, LolCat.class.getName());
        registration.addMapping("/*");
        context.deploy(httpServer);
        */        
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        
        
        
        System.in.read();
        server.stop();
    }
}

