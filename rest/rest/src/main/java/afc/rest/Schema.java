package afc.rest;





import com.github.fge.jsonschema.main.JsonSchema;

public class Schema implements Comparable <Schema> {

private JsonSchema schema;
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

	
