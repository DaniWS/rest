package afc.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class SimplifiedSensor extends CompleteJson{
	
	
	private static final Logger log = Logger.getLogger(Server.class);
    

		@Override
	//	 A method that parses the simplified JSON filling the missing values from the "missing values" object.
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
		log.debug("Entered Simplified Sensor Class");

		
					    for(String key: missingObject.keySet()) {
					
					    if(!key.equals("uom")) {
						inputJson.add(key, missingObject.get(key));
						}
					    else {
					    	
	                    inputJson.get("result").getAsJsonObject().add(key, missingObject.get(key));	
					    }
					    					    }   
					    return inputJson;
			    
	}


}