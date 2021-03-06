/* Copyright 2018-2021 Universidad Politécnica de Madrid (UPM).
 *
 * Authors:
 *    Daniel Vilela García
 *    José-Fernan Martínez Ortega
 *    Vicente Hernández Díaz
 * 
 * This software is distributed under a dual-license scheme:
 *
 * - For academic uses: Licensed under GNU Affero General Public License as
 *                      published by the Free Software Foundation, either
 *                      version 3 of the License, or (at your option) any
 *                      later version.
 * 
 * - For any other use: Licensed under the Apache License, Version 2.0.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * You can get a copy of the license terms in licenses/LICENSE.
 * 
 */

/**
 * Class containing the Main method
 */
package afc.rest;

import org.glassfish.grizzly.http.server.HttpServer;
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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "https://0.0.0.0:8080/";
	public static final String SERVER_URI = "https://138.100.51.114:443/";
	protected static final URI DOCS_URI=URI.create(SERVER_URI+"docs/");
	
//	Trust self-signed certificates.
	private static void trustEveryone() { 
	    try { 
	            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){ 
	                    public boolean verify(String hostname, SSLSession session) { 
	                            return true; 
	                    }}); 
	            SSLContext context = SSLContext.getInstance("TLS"); 
	            context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
	                    public void checkClientTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public void checkServerTrusted(X509Certificate[] chain, 
	                                    String authType) throws CertificateException {} 
	                    public X509Certificate[] getAcceptedIssuers() { 
	                            return new X509Certificate[0]; 
	                    }}}, new SecureRandom()); 
	            HttpsURLConnection.setDefaultSSLSocketFactory( 
	                            context.getSocketFactory()); 
	    } catch (Exception e) { // should never happen 
	            e.printStackTrace(); 
	    } 
	} 


    
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
    	
    	resourceConfig.register(new CorsFilter());
    	
    	  SSLContextConfigurator sslConfig = new SSLContextConfigurator();
          sslConfig.setKeyStoreFile("src/SSL/torcos.jks");
          sslConfig.setKeyPass("t0rc0s.etsist.upm.es");
          

    	 
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

//      **** Uncomment 'docsHanler' lines to enable Swagger API at BASE_URI+"/docs/" ****
        
//        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        CLStaticHttpHandler schemasHandler = new CLStaticHttpHandler(loader, "schemas/");
        
        String log4jConfPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"properties"+File.separator+"log4j.properties";

        
        PropertyConfigurator.configure(log4jConfPath);

//        docsHandler.setFileCacheEnabled(false);
        schemasHandler.setFileCacheEnabled(false);
       

        ServerConfiguration cfg = server.getServerConfiguration();

//        cfg.addHttpHandler(docsHandler, "/docs/");
        cfg.addHttpHandler(schemasHandler, "/schemas/");
        
//        Load property files.
        Setup.loadProperties(System.getProperty("user.dir")+File.separator+"src"+File.separator+"properties"+File.separator+Setup.configFileName);

        trustEveryone();

        Setup.loadSchemasInfo(BASE_URI+"schemas/");
        Setup.loadSchemas(BASE_URI+"schemas/");

     
     
     
        
     
        
       
        System.out.println(String.format("REST server started. API documentation available at: "
                + "%s\n", DOCS_URI));
        

//      Uncomment to be able stop the server from the shell        
   /*   System.out.println(String.format("Hit enter to stop the server..."));
        System.in.read();
        server.shutdownNow();
        */
    }
}

