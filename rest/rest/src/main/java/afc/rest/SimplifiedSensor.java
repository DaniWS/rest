package afc.rest;





import com.google.gson.JsonObject;

public class SimplifiedSensor extends SimplifiedJson{




	@Override
	//	 A method that parses the simplified JSON filling the missing values from the "missing values" object.
	public JsonObject completeFields(JsonObject missingObject, JsonObject inputJson) {


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