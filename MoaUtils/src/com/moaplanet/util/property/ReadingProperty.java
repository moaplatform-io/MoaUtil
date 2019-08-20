package com.moaplanet.util.property;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadingProperty {

	public static void main(String[] args) throws IOException {
		ReadingProperty handleConfig = new ReadingProperty();
		Map<String, String> mapProperty = handleConfig.getKiccInfoFromProperty("KiccConfig.properties");
		System.out.println(mapProperty.get("CERT_FILE"));
		
		System.out.println(handleConfig.getProps("KiccConfig.properties"));
	}
	
	public Map<String, String> getKiccInfoFromProperty(String fileName){
		return getInfoFromProperty(fileName, 
				new PropertyStrategy() {
					
					@Override
					public Map<String, String> makePropertyMap(Properties properties) {
					    Map<String, String> propertyMap = new HashMap<String, String>();
					    propertyMap.put("CERT_FILE", properties.getProperty("CERT_FILE"));
					    propertyMap.put("LOG_DIR", properties.getProperty("LOG_DIR"));
					    propertyMap.put("GW_URL", properties.getProperty("GW_URL"));
					    return propertyMap;
					}
				}
			);
	}
	
	public Map<String, String> getAuthDBInfoFromProperty(String fileName){
			
		return getInfoFromProperty(fileName, 
				new PropertyStrategy() {
				
					@Override
					public Map<String, String> makePropertyMap(Properties properties) {
						System.out.println("after getProps");
					    Map<String, String> propertyMap = new HashMap<String, String>();
					    propertyMap.put("CERT_FILE", properties.getProperty("CERT_FILE"));
					    propertyMap.put("LOG_DIR", properties.getProperty("LOG_DIR"));
					    propertyMap.put("GW_URL", properties.getProperty("GW_URL"));
					    return propertyMap;
					}
				}
			);
	}
	
	public Map<String, String> getInfoFromProperty(String fileName, PropertyStrategy proStgy){
		try {
			Properties mainProperties = getProps(fileName);

			if (mainProperties == null) 
				return new HashMap<>();
			else 
				return proStgy.makePropertyMap(mainProperties);
	    
		} catch (IOException e) {
			System.out.println("FATAL : Payment property file is not vallid(" + fileName + ")");
			return new HashMap<>();
		}
	}

	private Properties getProps(String propertiesFileName) throws IOException {
        return loadProperties(this.getClass().getClassLoader().getResource("META-INF/" + propertiesFileName));

    }
	
	private Properties loadProperties(URL incoming) throws IOException {
		    if (incoming != null) {
		        Properties properties = new Properties();
		        properties.load(incoming.openStream());
		        return properties;
		        
		    } else {
		    	System.out.println("No URL!!");
		    	throw new IOException();
		    }
	}
}
