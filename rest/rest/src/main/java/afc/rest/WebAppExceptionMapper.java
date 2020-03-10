//package afc.rest;
//
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.ExceptionMapper;
//import javax.ws.rs.ext.Provider;
//
//
//// This class handles the NotFoundExceptions, and returns a response to the client with the API documentation.
//
//@Provider
//public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {
//	
//	
//	public Response toResponse(WebApplicationException exception) {
//        return Response.status(exception.getResponse().getStatus()).entity(exception.getMessage()).header("Access-Control-Allow-Origin", "*").build();  
//               
//    }
//}