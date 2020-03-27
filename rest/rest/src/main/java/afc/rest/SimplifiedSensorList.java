package afc.rest;


import com.google.gson.JsonObject;

public class SimplifiedSensorList extends SimplifiedJson  {





	//	 A method that parses the JSON filling the missing values from the "missing values" object.

	@Override
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {


		for(String key: missingObject.keySet()) {

			inputJson.add(key, missingObject.get(key));					    	
		}

		return inputJson;

	}
}
