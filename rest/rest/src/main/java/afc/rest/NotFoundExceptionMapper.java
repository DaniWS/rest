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
 * You can get a copy of the license terms in licences/LICENSE.
 * 
 */

/** 
 * Filter for handling "not found" exceptions
 */
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