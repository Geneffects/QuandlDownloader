import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 
 * @author Brian Risk
 *
 */
public class Settings {
	
	public static String authentication_token = "5dkByeosaGrcyhbUaFAH";
	public static String data_directory = "data";
	
	static {
		load(new File("settings.yml"));
	}
	
	public static void load(File settingsfile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(settingsfile));
			String line = br.readLine();
			while (line != null) {
				// stripping off comments
				if (line.indexOf('#') != -1) {
					line = line.substring(0, line.indexOf('#'));
				}
				line = line.trim();
				boolean parse = true;
				if (line.isEmpty()) parse = false;
				if (line.indexOf(":") == -1) parse = false;
				if (parse) {
					String [] chunks = line.split(":");
					String fieldName = chunks[0].trim();
					String value = chunks[1].trim();
					setProperty(fieldName, value);
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			U.p("Could not find file 'settings.yml'.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void setProperty(String fieldName, String value) throws NoSuchFieldException, IllegalAccessException {
		Class<Settings> theClass = Settings.class;
		Field field = theClass.getDeclaredField(fieldName);
		if (field.getType() == Character.TYPE) {field.set(theClass, value.charAt(0)); return;}
		if (field.getType() == Short.TYPE) {field.set(theClass, Short.parseShort(value)); return;}
		if (field.getType() == Integer.TYPE) {field.set(theClass, Integer.parseInt(value)); return;}
		if (field.getType() == Long.TYPE) {field.set(theClass, Long.parseLong(value)); return;}
		if (field.getType() == Float.TYPE) {field.set(theClass, Float.parseFloat(value)); return;}
		if (field.getType() == Double.TYPE) {field.set(theClass, Double.parseDouble(value)); return;}
		if (field.getType() == Byte.TYPE) {field.set(theClass, Byte.parseByte(value)); return;}
		if (field.getType() == Boolean.TYPE) {field.set(theClass, Boolean.parseBoolean(value)); return;}
		if (field.getType() == File.class) {field.set(theClass, new File(value)); return;}
		field.set(theClass, value);
	}

}
