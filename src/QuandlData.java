
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * 
 * @author Brian Risk
 *
 */
public class QuandlData {
	
	public static HashMap<Integer, String> idToCode = new HashMap<Integer, String>();
	public static HashMap<Integer, String> idToName = new HashMap<Integer, String>();
	public static HashMap<Integer, String> idToDescription = new HashMap<Integer, String>();
	
	public static HashMap<String, Integer> codeToId = new HashMap<String, Integer>();
	public static HashMap<String, Integer> nameToId = new HashMap<String, Integer>();
	public static HashMap<String, Integer> descriptionToId = new HashMap<String, Integer>();
	
	public static void main(String [] args) {
		loadInformation("yc");
	}

	public static String getCodeFromId(Integer id) {
		String code = idToCode.get(id);
		if (code == null) {
			loadInformation(id);
			code = idToCode.get(id);
		}
		if (code == null) code = id.toString();
		return code;
	}
	
	/**
	 * EX:  YC/MEX3Y,Mexican Government 3-Year Bond Yield,2006-08-14,2013-02-15,daily,2014-08-28
	 * @param code
	 * @return
	 */
	public static CSVRecord getDataSetInfo(String code) {
		U.p("getting info for: " + code);
		CSVRecord record = null;
		try {
			URL metadata = new URL("https://www.quandl.com/api/v2/datasets.csv?query=*&source_code=" + code + "&per_page=3&page=1&auth_token=" + Settings.authentication_token);
			URLConnection conn = metadata.openConnection();
			Reader reader = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(reader);
			String line = br.readLine();
			CSVParser parser = CSVParser.parse(line, CSVFormat.EXCEL);
			record = parser.getRecords().get(0);
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return record;
	}
	
	public static void loadInformation(Object identifier) {
		try {
			URL url = new URL("https://www.quandl.com/api/v3/databases/" + identifier + ".json?auth_token=" + Settings.authentication_token);
			JsonReader jsonReader = Json.createReader(url.openStream());
			JsonObject jsonObject = jsonReader.readObject();
			JsonObject database = jsonObject.getJsonObject("database");
			if (database != null) { 
				String code = database.getString("code");
				String name = database.getString("name");
				String description = database.getString("description");
				int id = database.getInt("id");
				idToCode.put(id, code);
				idToName.put(id, name);
				idToDescription.put(id, description);
				codeToId.put(code, id);
				nameToId.put(name, id);
				descriptionToId.put(description, id);
			}
			jsonReader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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