package afc.rest;



import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import io.swagger.annotations.Api;



import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.utils.Pair;

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
import com.google.gson.JsonObject;

import afc.rest.ValidationUtils;


@Api( "REST API")
@Path("/")
public class Server {

	private static final Logger log = Logger.getLogger(Server.class);

	protected final String sensorMeasure="sensor/measure";
	protected final String sensorMeasureList="sensor/measureList";
	protected final String regionMeasure="region/measure";
	protected final String regionMeasureList="region/measureList";
	protected final String collarMeasure="collar/measure";
	protected final String collarMeasureList="collar/measureList";
	//  	protected String resourceId;
	private static int i = 0;

	protected static final Response invalidJsonException = Response.status(415).build();
	protected static final Response notaJsonException =  Response.status(400).build();
	protected static final Response AR_RuntimeException = Response.status(500).entity("ERROR: The specified resourceId might not be registered in the Asset Registry").build();
    //  Detailed exceptions for TEST mode
	protected static final Response detInvJsonExc = Response.status(415).build();
	protected static final Response detNotJsonExc =  Response.status(400).build();
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

	//     Method to validate Json.


	private Pair <Boolean, Schema> validateJson (String s, UriInfo uriInfo) throws ProcessingException, IOException {

		//		Reorder the collection attending to the demand.

		i++;
		if(i>=100) 
		{
			Collections.sort(SchemaSet.schemas);

			i=0;
			System.out.println(i + " veces se ha validado!!!!!!!");
			System.out.println("Array ordenado por uso");
			for (int i = 0; i < SchemaSet.schemas.size()-1; i++) {
				System.out.println((i+1) + ". " + SchemaSet.schemas.get(i).getName() + " - Uso: " + SchemaSet.schemas.get(i).getUso());
			}
			SchemaSet.schemas.forEach((n) -> n.setUso(0));

		}

		for (Schema i:SchemaSet.schemas) {
			if (i.getName().equals("Definitions")){
				continue;
			}
			//		Validates against schemas.
			if (ValidationUtils.isJsonValid(i.getSchema(), s))
			{

				i.setUso(i.getUso()+1); 

				//		String category = i.getName();
				return new Pair<Boolean, Schema>(true, i); 
			}	

		}

		return new Pair<Boolean, Schema>(false, null); 
	}	
	//		
	private String getRemoteAddress(Request request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
			ipAddress = request.getRemoteAddr();  
		} 
		return ipAddress;
	}




	//Method to access the log remotely.

	@GET
	@Path("/logs")
	@Produces({MediaType.TEXT_PLAIN})
	public String testServer(@Context UriInfo uriInfo) throws URISyntaxException, IOException{
		String response= new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"logs"+File.separator+"logfile.log")));
		return response;		  

	}

	@POST
	@Path("/telemetry")
	//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON , MediaType.TEXT_PLAIN})
	public Response getMeasure(String telemetry, @Context UriInfo uriInfo,@Context Request request) throws ProcessingException,URISyntaxException, IOException  {
		//              Check for "resourceId"
		/*	 	
    		RegularExpression oRegExt = new RegularExpression();
    		resourceId = oRegExt.extractInformation("\"{\"resourceId\":\"urn:afc:AS04:environmentalObservations:TST:airSensor:airTemperatureSensor0012\",\"sequence number\": 123,\"location\": { \"latitude\": 45.45123,\"longitude\": 25.25456, \"altitude\": 2.10789},\");");
		 */ 	    try {
			 Pair   <Boolean, Schema> response=validateJson(telemetry,uriInfo);
			 Boolean valid=response.getFirst();
			 Schema schema=response.getSecond();
			 if (valid) {
				 String category=schema.getName();
				 JsonObject completeJson=null;
				 if(schema.getIsSimple()) {
					 //		        		Method that completes the JSON, first looking for a match in cache and, if not found, obtaining it from the Assets Registry.
					 completeJson = CompleteJson.getCompleteJson(schema.getMissingFields(), telemetry, Setup.AR_URL);
					 telemetry= completeJson.toString();
					 System.out.println(telemetry);
					 }
				 

         


				 //           	  String text="";
				 //	        	  Checks for the "test" query parameter.
				 if (!uriInfo.getQueryParameters().containsKey("test")) {
					 String URN="measures"; // PROVISIONAL !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
					 log.info("SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Successful request on: "+ category );

					 //	        	  Here goes the code to send the data.
					 //               PROVISIONAL !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    
	 		
	 				 String type = schema.getType().toString();
					 if(type.equals("Collar")) {
						 URN="collar";						 						 
					 }
					 System.out.println(Setup.ER_URI+URN);
					 return CompleteJson.sendTelemetry(telemetry, request, category, Setup.ER_URI+URN);
				 }
				 else {
					 //       	  text= "Test mode: ";	   
					 if (completeJson!=null) {
					 return Response.status(200).entity(telemetry).build();
					 }
					 else {
						 return Response.status(200).build();
					 }
				 }	  

			 }

			 else {
				 if ( (!uriInfo.getQueryParameters().containsKey("test"))) {

					 log.error("SessionID: "+request.getSession().getIdInternal()+request.getSession().getTimestamp()+"  IP: "+ getRemoteAddress(request)+" Not AFarCloud Compliant");
					 throw new WebApplicationException(invalidJsonException);
				 }
				 Response detailedException= Response.status(415).entity("ERROR: Not AFarCloudCompliant").build();
				 throw new WebApplicationException(detailedException);
			 }
		 }
		 catch(JsonParseException ex){
			 if (!uriInfo.getQueryParameters().containsKey("test")) {
				 log.error("SessionID: "+request.getSession().getIdInternal()+" IP: "+ getRemoteAddress(request)+" Not a Json Exception "+ex);
				 throw new WebApplicationException(notaJsonException);
			 }
			Response detailedException= Response.status(400).entity(ex.getMessage()).build();
			 throw new WebApplicationException(detailedException);


		 }
/*		 catch (RuntimeException e) {
			  	
						 
			 return Response.status(500).entity(e.getMessage()).build();
		 }
*/		 
		 catch (MalformedURLException e) {
			  	
			 
			 return Response.status(500).entity(e.getMessage()).build();
		 }


	}


}

