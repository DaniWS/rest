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
 * Class for the Schema Management
 **/
package afc.rest;


import com.github.fge.jsonschema.main.JsonSchema;

public class Schema implements Comparable <Schema> {

private JsonSchema schema;
//'uso' increases every time a request is made on a specific schema in order to dynamically re-organize
//the order of schemas inside the validation method, thus potentially reducing time.
     private int uso;
     private String name;
     private Boolean isSimple;
     private String type;

     protected Schema(int uso,String name, Boolean isSimple, String type) {

         this.uso = uso; 
         this.name = name;
         this.isSimple = isSimple;
         this.type = type;
         
     }
     protected Schema(JsonSchema schema, int uso, String name, Boolean isSimple, String type) {
         this.schema = schema;
         this.uso = uso;
         this.name = name;
         this.isSimple = isSimple;
         this.type = type;
         
     }
     public JsonSchema getSchema() {
return schema;
}
     public void setSchema(JsonSchema schema) {
    	 this.schema= schema;
    	 }
     public int getUso() {
return uso;
}
     public void setUso(int uso) {
this.uso = uso;
}
public void setName(String name) {
this.name = name;
}
     public String getName() {
return name;
}
@Override
public int compareTo(Schema o) {
if (uso > o.uso) {
               return -1;
           }
           if (uso < o.uso) {
               return 1;
         }
return 0;
}
public Boolean getIsSimple() {
	return isSimple;
}
public void setIsSimple(Boolean isSimple) {
	this.isSimple = isSimple;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

 



}

	
