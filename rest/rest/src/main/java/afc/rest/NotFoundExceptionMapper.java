package afc.rest;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


// This class handles the WebApplicationExceptions, in order to append the required header for CORS.

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
	
	
	public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("The resource doesn't exist. Please refer to the API documentation: "+ Main.SERVER_URI+"docs/")
                .build();
    }
}