package info.mathieusavard.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
	private static Properties properties;
	static {
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream("ParserConfig.properties");
		    properties.load(fis);
		} catch (IOException e) {
			System.err.println("Error reading property file");
		}
	}
	
	public static String get(String prop) {
		return properties.getProperty(prop);
	}
	
	public static int getInt(String prop) {
		return Integer.parseInt(properties.getProperty(prop));
	}

	public static boolean getBoolean(String prop) {
		return Boolean.parseBoolean(properties.getProperty(prop));
	}
}
