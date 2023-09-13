package JSONAPI_Testing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropReader {

	public Properties properties = new Properties();
    FileInputStream inputStream = null;
    
    PropReader(String fName){
    	try {
	    	String filePath = "configs//"+fName+".properties";
			inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			inputStream.close();
    	}
    	catch(IOException ie) {
    	}
    }

	public Properties getProperties() {
		return properties;
	}

}
