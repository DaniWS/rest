package afc.rest;

import java.io.IOException;
import java.util.HashMap;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Alarm extends SimplifiedJson {
	private static final Logger log = Logger.getLogger(Alarm.class);


	//  This method doesn't parse the registration JSON. It checks which kind of sensor it is (multi-param or single-param), and delegates
	//  the process of parsing and building the missing JSON object to the corresponding class method.
	public JsonObject parseRegistryJson(String registrationJson) throws IOException {

		JsonObject missingFields = new JsonObject();

		try {
			JsonElement jsonTree=JsonParser.parseString(registrationJson);
			String sensorType =getSensorType(jsonTree);
			if (sensorType.equals("single-param")){
				missingFields= super.parseRegistryJson(registrationJson);
			}
			else if (sensorType.equals("multi-param")){
				SimplifiedMultiSensor multiSensor = new SimplifiedMultiSensor();
				missingFields= multiSensor.parseRegistryJson(registrationJson);
			}
			return missingFields;
		}

		catch(JsonParseException e) {
			log.error("Could not obtain resource from the Assets Registry: "+e.getMessage());
			throw new WebApplicationException(Server.AR_ParserException);
		}
	}
	//  This method completes the received simplified JSON.
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {
		try{
			inputJson.add("resourceId", missingObject.get("resourceId"));
		}


		catch (ClassCastException e) {

			log.error("Could not obtain resource from the Assets Registry: "+e.getMessage());
			throw new WebApplicationException(Server.AR_ParserException);
		}



		return inputJson;
	}

	//	Determine whether it is a single-param or a multi-param sensor.
	public String getSensorType(JsonElement registrationJson) {
		if (registrationJson.isJsonObject()) {
			JsonObject jsonObject= registrationJson.getAsJsonObject();
			JsonElement observations = jsonObject.get("observations");
			if (observations!=null&&observations.isJsonArray()) {
				JsonArray obsArray = observations.getAsJsonArray();
				if	(obsArray.size()>1) {
					return "multi-param";
				}
				else {
					return "single-param";
				}
			}
		}

		log.error("Assets Registy: Could not obtain resource from the Assets Registry");
		throw new WebApplicationException(Server.AR_RuntimeException);

	}

}
