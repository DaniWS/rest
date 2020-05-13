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
 * This class completes the JSONs for multiple sensor measurements
 */

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
