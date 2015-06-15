import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * 
 * @author Brian Risk
 *
 */
public class QuandlDatabases {
	
	public static HashMap<Integer, String> codes = new HashMap<Integer, String>();
	public static HashMap<Integer, String> names = new HashMap<Integer, String>();
	public static HashMap<Integer, String> descriptions = new HashMap<Integer, String>();

	public static String getCodeFromId(int id) {
		String database_code = codes.get(id);
		if (database_code == null) {
			URL url;
			try {
				url = new URL("https://www.quandl.com/api/v3/databases/" + id + ".json");
				JsonReader jsonReader = Json.createReader(url.openStream());
				JsonObject jsonObject = jsonReader.readObject();
				JsonObject database = jsonObject.getJsonObject("database");
				database_code = database.getString("database_code");
				String name = database.getString("name");
				String description = database.getString("description");
				codes.put(id, database_code);
				names.put(id, name);
				descriptions.put(id, description);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return database_code;
	}
	
	public static ArrayList<String> getCodesFromIds(ArrayList<Integer> ids) {
		ArrayList<String> out = new ArrayList<String>();
		for (int id: ids) {
			String code = getCodeFromId(id);
			if (code != null) out.add(code);
		}
		return out;
	}
	
	
}