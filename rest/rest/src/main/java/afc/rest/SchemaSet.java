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
 * This class contains the Arrays for loading the Schemas
 */
package afc.rest;

import java.util.ArrayList;

public class SchemaSet {

protected static ArrayList<Schema> schemas= new ArrayList<>();
//JSON Schemas to validate telemetry requests: 
protected static ArrayList<Schema> telemetrySchemas= new ArrayList<>();
//JSON Schemas to validate alarm requests: 
protected static ArrayList<Schema> alarmSchemas= new ArrayList<>();

}
