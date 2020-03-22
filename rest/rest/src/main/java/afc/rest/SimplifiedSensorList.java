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

public class SimplifiedSensorList extends SimplifiedJson  {
	
	
    


	//	 A method that parses the JSON recursively filling the missing values of the "missing values" object.

@Override
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {

		
					    for(String key: missingObject.keySet()) {
					   
					    	inputJson.add(key, missingObject.get(key));					    	
					    }
					    					       
					    return inputJson;
			    
	}
}
