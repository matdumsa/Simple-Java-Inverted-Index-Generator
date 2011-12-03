package info.mathieusavard.technicalservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

public class Property {
	
	
	private static Properties properties;
	
	static {
		properties = new Properties();
		try {
			InputStream fis = Property.class.getClassLoader().getResourceAsStream("ParserConfig.properties");
		    properties.load(fis);
		} catch (IOException e) {
			System.err.println("Error reading property file");
		}
	}
	
	public static String get(String prop) {
		if (prop.equalsIgnoreCase("basepath"))
			return basepath();
		return properties.getProperty(prop);
	}
	
	private static String basepath() {
		Preferences prefs = Preferences.userRoot().node("/comp479/finalproject");
		// Is there a system preference for basepath?
		if (prefs.get("basepath", null) != null && prefs.get("basepath", null).length() >0)
			return prefs.get("basepath", null);
		else {
			//Nope, set it to default found in property file.
			prefs.put("basepath", properties.getProperty("basepath"));
			return properties.getProperty("basepath");
		}

	}
	
	public static int getInt(String prop) {
		return Integer.parseInt(properties.getProperty(prop));
	}

	public static boolean getBoolean(String prop) {
		return Boolean.parseBoolean(properties.getProperty(prop));
	}
}
