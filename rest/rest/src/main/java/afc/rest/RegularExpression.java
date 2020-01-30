/*package afc.rest;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {
	
	private final HashMap<String, String> hRegularExpression = new HashMap<String, String>();
	
	public RegularExpression(){
		
		// expresiones regulares a evaluar
		// 1 => resourceId con nombre exacto
		hRegularExpression.put("resourceId", ".*\"resourceId\"\\s*:\\s*\"([^\"]+)\".*");

		// 2 => todas las propiedades
//		hRegularExpression.put("properties", "\"([^\"]+)\"\\s*:\\s*\"?([^,{\"]+)[,\"}]");
		
	}
	
	public String extractInformation(String sJsonFragment){
		String a="";
		if (sJsonFragment.trim().length()>0){
			
			Pattern patt;
			Matcher m;
			
			
			for (String sK : hRegularExpression.keySet()){
				System.out.println ("\nevaluando: " + sK);
				
				patt = Pattern.compile(hRegularExpression.get(sK), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				m= patt.matcher(sJsonFragment);

					while (m.find()){
						System.out.println ("\nmach: " + m.groupCount());
						
						
						    a=m.group();
					        
//							System.out.format("\n\t%s ----> %s", sK, m.group(i));												
					
					
		
					}
			}
			}
		return a;
		}
}
			
		
	
	*/
  

	/**
	 * @param args
	 */