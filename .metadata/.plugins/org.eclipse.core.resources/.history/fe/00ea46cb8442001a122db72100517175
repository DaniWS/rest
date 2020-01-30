package afc.rest;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.Request;

import com.fasterxml.jackson.core.JsonParseException;
/*
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
*/
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Api(value = "REST API")
@Path("/{parameter: as01|as02|as03|as04|as05|as06|as07|as08|as09|as10|as11}/")
public class Server implements IServer{
	
	private static final Logger log = Logger.getLogger(Server.class);
//	private static final Gson gson = new Gson();
	protected static final URI docsUri=URI.create(Main.BASE_URI+"docs/");
	
  	protected final String sensorMeasure="sensor/measure";
  	protected final String sensorMeasureList="sensor/measureList";
  	protected final String regionMeasure="region/measure";
  	protected final String regionMeasureList="region/measureList";
  	protected final String collarMeasure="collar/measure";
  	protected final String collarMeasureList="collar/measureList";
  	protected String resourceId;
  	
	protected final Response invalidJsonException = Response.status(405).entity("405: \"Invalid input: not AFarCloud-compliant\". For more information, please refer to the API documentation: "+ docsUri).header("Access-Control-Allow-Origin", "*").build();
	protected final Response notaJsonException =  Response.status(415).entity("415: \"Invalid input: not a JSON\". For more information, please refer to the API documentation: "+ docsUri).header("Access-Control-Allow-Origin", "*").build();
    
	
	/*
	@Context ServletContext context;
	
	public void createPublisherMQTT(String s)throws MqttException
	{
  	  /////////////////////////////////////////////////////////
  	  //Storage of information by going through the required components
  	  	String broker   = "ssl://mqtt.afarcloud.smartarch.cz:1883";
        String userName = "upm";
        String password = "vIabMNUMKHypmNLJkv/K6AjMsUfj3IDQ";

        MemoryPersistence persistence = new MemoryPersistence();

        MqttClient sampleClient = new MqttClient(broker, "sample-java-client", persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();

        Properties sslProperties = new Properties();
        sslProperties.setProperty("com.ibm.ssl.protocol", "TLS");
        sslProperties.setProperty("com.ibm.ssl.trustStore", context.getRealPath("WEB-INF/classes/mqttTrustStore.jks"));
        sslProperties.setProperty("com.ibm.ssl.trustStorePassword", "qwerty");
        connOpts.setSSLProperties(sslProperties);

        connOpts.setUserName(userName);
        connOpts.setPassword(password.toCharArray());

        System.out.println("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        System.out.println("Connected");

        String msgContent =s;
        MqttMessage message = new MqttMessage(msgContent.getBytes());
        message.setQos(1);

        System.out.println("Publishing message: " + message);
        sampleClient.publish("test/test", message);
        System.out.println("Message published");

        sampleClient.disconnect();
        sampleClient.close();

        System.out.println("Disconnected"); 
	}
	*/
//	 This method checks which resource is being called by using @Context annotation, and selects...
//	 the corresponding schema in order to validate the body of the request.
	private boolean validateJson(String s, UriInfo uriInfo) throws ProcessingException, IOException {
		
		switch(uriInfo.getPathParameters().getFirst("param")) {
		case sensorMeasure:
		return ValidationUtils.isJsonValid(jsonSensorSchema, s);
		case sensorMeasureList:		
		return ValidationUtils.isJsonValid(jsonSensorSchemaList, s);	
		case regionMeasure:		
		return ValidationUtils.isJsonValid(jsonSchemaRegion, s);
		case regionMeasureList:		
		return ValidationUtils.isJsonValid(Main.regionListSchema, s);
		case collarMeasure:		
		return ValidationUtils.isJsonValid(jsonCollarSchema, s);
		case collarMeasureList:		
		return ValidationUtils.isJsonValid(jsonCollarSchemaList, s);
		default:
		return false;	
		}
	}
		private String getRemoteAddress(Request request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		   if (ipAddress == null) {  
		       ipAddress = request.getRemoteAddr();  
		   } 
		   return ipAddress;
		}
		
		
	
	  
    //This is just to rapidly test the server.
	@Path("/{param:sensor/measure|sensor/measureList|region/measure|region/measureList|collar/measure|collar/measureList}/")
	  @GET
	   @Produces(MediaType.TEXT_PLAIN)
	    public String testServer(@Context UriInfo uriInfo) throws URISyntaxException{
        return "Server is up!";		  
	        
	    }
	@Path("/{param:sensor/measure|sensor/measureList|region/measure|region/measureList|collar/measure|collar/measureList}/")
	@POST
	@Consumes("text/plain")
	public Response getMeasure(String s, @Context UriInfo uriInfo,@Context Request request) throws ProcessingException,URISyntaxException, IOException  {

//              Check for "resourceId"
		 	
    		RegularExpression oRegExt = new RegularExpression();
    		resourceId = oRegExt.extractInformation("\"{\"resourceId\":\"urn:afc:AS04:environmentalObservations:TST:airSensor:airTemperatureSensor0012\",\"sequence number\": 123,\"location\": { \"latitude\": 45.45123,\"longitude\": 25.25456, \"altitude\": 2.10789},\");");
    	   
    	
	         try { if (validateJson(s,uriInfo)) {
	        	  String text="";
//	        	  Checks for the "test" query parameter
	        	  if (!uriInfo.getQueryParameters().containsKey("test")) {
	        	  log.info("resourceId : "+ resourceId+ "SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Successful request on: "+uriInfo.getPathParameters().getFirst("param") );
//	        	  Here goes the code to send the data 
	        	  }
	        	  else {
	        	  text= "Test mode: ";	   
	        	  }	  
	        	return Response.status(200).entity(text+"200: \"Successful operation\". \nFor more information, please refer to the API documentation: "+ docsUri +"\nRequest ID: "+request.getSession().getIdInternal()).header("Access-Control-Allow-Origin", "*").build();
	        	
	          }
	          
	          else if ( (!uriInfo.getQueryParameters().containsKey("test"))) {
	        	  log.error("resourceId : "+"SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Invalid Json Exception");
	          }
	          throw new WebApplicationException(invalidJsonException);
	       
	          }
	         catch(JsonParseException ex){
	        	 if (!uriInfo.getQueryParameters().containsKey("test")) {
	        		 log.error("resourceId : "+"SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Invalid Json Exception "+ex);
	        		 }
	        		
	         final Response detailedException = Response.status(405).entity(invalidJsonException.getEntity().toString()+"\nError: "+ex).build();
	         throw new WebApplicationException(detailedException);
		}
	}
}

		/*catch(com.google.gson.JsonSyntaxException ex)	{
			if (!uriInfo.getQueryParameters().containsKey("test")) {
				log.error("SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Not a JSON "+ex);
			}
			throw new WebApplicationException(notaJsonException);
			}
	    }	
	}
		
	   */