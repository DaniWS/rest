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
 *  This class contains the resources and the REST methods of the server
 */
package afc.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import io.swagger.annotations.Api;



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
import org.glassfish.grizzly.http.server.Session;
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
	private static int i = 0;

	protected static final Response invalidJsonException = Response.status(415).build();
	protected static final Response notaJsonException =  Response.status(400).build();
	protected static final Response AR_RuntimeException = Response.status(500).entity("ERROR: The specified resourceId might not be registered in the Assets Registry").build();
	protected static final Response AR_ParserException = Response.status(500).entity("ERROR: Could not obtain complete version from the Assets Registry").build();

    //  Detailed exceptions for TEST mode
	protected static final Response detInvJsonExc = Response.status(415).build();
	protected static final Response detNotJsonExc =  Response.status(400).build();

	
	//     Method to validate JSON alarm.

	private boolean validateJsonA (String s, UriInfo uriInfo) throws ProcessingException, IOException {
		for (Schema i:SchemaSet.alarmSchemas) {
	/*		if (i.getName().equals("Definitions")){
				continue;
			}
			*/
			//		Validates against schemas.
			if (ValidationUtils.isJsonValid(i.getSchema(), s))
			{
				//		String category = i.getName();
				return true; 
			}	

		}
		return false;
	}
	
	//     Method to validate telemetry JSON.

	private Pair <Boolean, Schema> validateJsonT (String s, UriInfo uriInfo) throws ProcessingException, IOException {

		//		Reorder the collection attending to the demand, after 'i' number of requests.

		i++;
		if(i>=100) 
		{
			Collections.sort(SchemaSet.telemetrySchemas);

			i=0;
			System.out.println(i + " veces se ha validado!!!!!!!");
			System.out.println("Array ordenado por uso");
			for (int i = 0; i < SchemaSet.telemetrySchemas.size()-1; i++) {
				System.out.println((i+1) + ". " + SchemaSet.schemas.get(i).getName() + " - Uso: " + SchemaSet.schemas.get(i).getUso());
			}
			SchemaSet.schemas.forEach((n) -> n.setUso(0));

		}

		for (Schema i:SchemaSet.telemetrySchemas) {
			System.out.println(i.getName());

			if (ValidationUtils.isJsonValid(i.getSchema(), s))
			{

				i.setUso(i.getUso()+1); 
                log.debug("Succesful request on: "+i.getName());
				//		String category = i.getName();
				return new Pair<Boolean, Schema>(true, i); 
			}	

		}

		return new Pair<Boolean, Schema>(false, null); 
	}	
	//		This method obtains the IP address of the resource.
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
	@Path("/alarm")
	//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON , MediaType.TEXT_PLAIN})
	public Response getAlarm(String alarm, @Context UriInfo uriInfo,@Context Request request) throws ProcessingException,URISyntaxException, IOException  {
	
		try {
			 Boolean valid=validateJsonA(alarm,uriInfo);
//			 Valid JSON scenario.
				 if (valid) {
					 String category = "Alarm";
                     alarm=SimplifiedJson.getCompleteJson(alarm, Setup.AR_URL, category).toString();
                     Session session=request.getSession();
                                          
//				        	  Checks for the "test" query parameter.
				 if (!uriInfo.getQueryParameters().containsKey("test")) {
//				        	  Here goes the code to send the data.
					 
					 log.info("SessionID: "+session.getIdInternal()+session.getTimestamp()+" IP: "+ getRemoteAddress(request)+" Successful request from: "+SimplifiedJson.getResourceId(alarm)+" on: "+category );
         			return Response.status(200).entity("{\n\"requestId\": "+session.getIdInternal()+session.getTimestamp()+"\n}").build();

				 }
				 log.info("TEST: IP: "+ getRemoteAddress(request)+" Successful request from: "+SimplifiedJson.getResourceId(alarm)+" on: "+category );

					 return Response.status(200).entity(alarm).build();
					
				 	  

			 }
//          Not AFarCloud compliant scenario. 
			 else {
//				 Check for the 'test' query parameter.
				 if ( (!uriInfo.getQueryParameters().containsKey("test"))) {

					 log.error("Resource ID: "+SimplifiedJson.getResourceId(alarm)+"  IP: "+ getRemoteAddress(request)+" Not AFarCloud Compliant");
					 throw new WebApplicationException(invalidJsonException);
				 }
				 log.error("TEST: Resource ID: "+SimplifiedJson.getResourceId(alarm)+"  IP: "+ getRemoteAddress(request)+" Not AFarCloud Compliant");
				 Response detailedException= Response.status(415).entity("ERROR: Not AFarCloudCompliant").build();
				 throw new WebApplicationException(detailedException);
			 }
		 }
//      Not a JSON document scenario. 
		 catch(JsonParseException ex){
//			 Check for the 'test' query parameter.
			 if (!uriInfo.getQueryParameters().containsKey("test")) {
				 log.error("Resource ID: "+SimplifiedJson.getResourceId(alarm)+" IP: "+ getRemoteAddress(request)+" Not a Json Exception "+ex);
				 throw new WebApplicationException(notaJsonException);
			 }
			 log.error("TEST: Resource ID: "+SimplifiedJson.getResourceId(alarm)+" IP: "+ getRemoteAddress(request)+" Not a Json Exception "+ex);
			Response detailedException= Response.status(400).entity(ex.getMessage()).build();
			 throw new WebApplicationException(detailedException);


		 }
	 
		 catch (MalformedURLException e) {
			  	
			 log.error("ERROR: MalformedURLException"+e.getMessage());
			 return Response.status(500).entity(e.getMessage()).build();
		 }

	}
	@POST
	@Path("/telemetry")
	//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON , MediaType.TEXT_PLAIN})
	public Response getMeasure(String telemetry, @Context UriInfo uriInfo,@Context Request request) throws ProcessingException,URISyntaxException, IOException  {
	 	    try {
			 Pair   <Boolean, Schema> response=validateJsonT(telemetry,uriInfo);
			 Boolean valid=response.getFirst();
			 Schema schema=response.getSecond();
//			 Valid JSON scenario.
			 if (valid) {
				 String category=schema.getName();
				 JsonObject completeJson=null;
//				 Check if the request is a simple JSON.
				 if(schema.getIsSimple()) {
//					 Completes the JSON
                     completeJson=SimplifiedJson.getCompleteJson(telemetry, Setup.AR_URL, category);
					 
					 telemetry= completeJson.toString();
					 }
//				 Check for the 'test' query parameter.
				 if (!uriInfo.getQueryParameters().containsKey("test")) {
//**********************   This block of code is PROVISIONAL, while the component connects directly to InfluxDB. *******************************************************
//					 Build the URI, pointing to the correct path: "/collar" or "/measures".
					 String URN="measures"; 
					 log.info("SessionID: "+request.getSession().getIdInternal()+request.getSession().getTimestamp()+" IP: "+ getRemoteAddress(request)+" Successful request from: "+SimplifiedJson.getResourceId(telemetry)+" on: "+category );


	 				 String type = schema.getType().toString();
					 if(type.equals("Collar")) {
						 URN="collar";						 						 
					 }
					 System.out.println(Setup.ER_URI+URN);
//					 Send data to Environment Reporter.
					 return sendTelemetry(telemetry, request, category, Setup.ER_URI+URN);
				 }
				 else {
//					 Simplified JSON request scenario.
					 if (completeJson!=null) {
					 log.info("TEST: IP: "+ getRemoteAddress(request)+" Successful request from: "+SimplifiedJson.getResourceId(telemetry)+" on: "+category );
					 return Response.status(200).entity(telemetry).build();
					 }
//					 Complete JSON request scenario.
					 else {
						 log.info("TEST: IP: "+ getRemoteAddress(request)+" Successful request from: "+SimplifiedJson.getResourceId(telemetry)+" on: "+category );
 						 return Response.status(200).build();
					 }
				 }	  

			 }
//           Not AFarCloud Compliant scenario.
			 else {
//				 Check for 'test' query parameter.
				 if ( (!uriInfo.getQueryParameters().containsKey("test"))) {
					 log.error("Resource ID: "+SimplifiedJson.getResourceId(telemetry)+"  IP: "+ getRemoteAddress(request)+" Not AFarCloud Compliant");
					 throw new WebApplicationException(invalidJsonException);
				 }
				 log.error("TEST: Resource ID: "+SimplifiedJson.getResourceId(telemetry)+"  IP: "+ getRemoteAddress(request)+" Not AFarCloud Compliant");
				 Response detailedException= Response.status(415).entity("ERROR: Not AFarCloudCompliant").build();
				 throw new WebApplicationException(detailedException);
			 }
		 }
//	 	    Not a JSON document scenario.
		 catch(JsonParseException ex){
			 if (!uriInfo.getQueryParameters().containsKey("test")) {
				 log.error("Resource ID: "+SimplifiedJson.getResourceId(telemetry)+"  IP: "+ getRemoteAddress(request)+" Not a Json Exception "+ex);

				 throw new WebApplicationException(notaJsonException);
			 }
			 log.error("TEST: Resource ID: "+SimplifiedJson.getResourceId(telemetry)+"  IP: "+ getRemoteAddress(request)+" Not a Json Exception "+ex);
			Response detailedException= Response.status(400).entity(ex.getMessage()).build();
			 throw new WebApplicationException(detailedException);


		 }

		 catch (MalformedURLException e) {
			  	
			 log.error("ERROR: MalformedURLException"+e.getMessage());

			 return Response.status(500).entity(e.getMessage()).build();
		 }


	}
//	Method to send the data to the Environment Reporter.
	public static Response sendTelemetry(String json, Request request, String category, String ER_URI) {
		try {

			//				    Environment Reporter URL
			URL uri = new URL(ER_URI);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/json");


			String input = json;


			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			//			        Code block for reading the output from the server
			/*		        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			        String output;

			        //Server answer
			        System.out.println("Output from Server .... \n");
			        while ((output = br.readLine()) != null) {
			            System.out.println(output);

			        }
			 */		        
			conn.disconnect();
			
			Session session=request.getSession();
			return Response.status(200).entity("{\n\"requestId\": "+session.getIdInternal()+session.getTimestamp()+"\n}").build();

		} catch (MalformedURLException e) {
			log.error("Could not connect to Environment Reporter: "+e.getMessage());
			return Response.status(500).entity("Environment Reporter "+e.getMessage()).build();  
		} catch (IOException e) {
			log.error("Could not connect to Environment Reporter: "+e.getMessage());
			return Response.status(500).entity("Environment Reporter "+e.getMessage()).build();  
		}
		catch (RuntimeException e) {
			//		This exception can be caused by a non registered "resourceId" in the Assets Registry
			log.error("Could not connect to Environment Reporter: "+e.getMessage());
			return Response.status(500).entity("Environment Reporter "+e.getMessage()).build();  
		}

	}

}

