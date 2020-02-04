package afc.rest;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;

import org.apache.log4j.*;


import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
//import org.glassfish.grizzly.servlet.ServletRegistration;
//import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.jackson.JacksonFeature;
//import org.glassfish.jersey.grizzly2.*;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.grizzly.servlet.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;


import io.swagger.jaxrs.config.BeanConfig;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "https://127.0.0.1:8080/";
    
   
    
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
    	
    	  SSLContextConfigurator sslConfig = new SSLContextConfigurator();
          sslConfig.setKeyStoreFile("src/SSL3/afc_key");
          sslConfig.setKeyStorePass("afc_ssl");
          
    	 
    	return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig, true, new SSLEngineConfigurator(sslConfig).setClientMode(false).setNeedClientAuth(false));

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

        SchemaLoader.loadSchemas(BASE_URI+"schemas/");
        
     
        
       
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        

        
        
        System.in.read();
        server.shutdownNow();
    }
}

