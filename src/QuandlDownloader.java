import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * 
 * @author Brian Risk
 *
 */
public class QuandlDownloader {

	static int minutesToWait = 30;
	
	public static void main(String[] args) {
		// load existing download information
		ArrayList<UpdateEntry> updates = loadUpdateInformation();
		HashMap<String, UpdateEntry> updateMap = new HashMap<String, UpdateEntry>();
		for (UpdateEntry update: updates) {
			updateMap.put(update.code, update);
		}
		
		// main program loop; do it until the process is halted
		while (true) {
			
			// load a user's subscriptions
			ArrayList<Integer> subscriptionIds = getSubscriptionIds();
			ArrayList<String> subscriptionCodes = QuandlData.getCodesFromIds(subscriptionIds);

			// if any new subscriptions are found
			for (String code: subscriptionCodes) {
				if (updateMap.get(code) == null) {
					UpdateEntry entry = new UpdateEntry();
					entry.code = code;
					updates.add(entry);
					U.p("New subscription detected: " + code);
				}
			}

			// download the subscriptions
			for (UpdateEntry entry: updates) {
				downloadDatabase(entry);
			}

			// update the log
			saveUpdateInformation(updates);
			
			// wait
			U.sleep(Constants.MINUTE * minutesToWait);
		}
	}
	
	public static ArrayList<UpdateEntry> loadUpdateInformation() {
		ArrayList<UpdateEntry> entries = new ArrayList<UpdateEntry>();
		File logFile = new File (Settings.data_directory + "/updates.txt");
		if (logFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(logFile));
				String line = br.readLine();
				while (line != null) {
					UpdateEntry entry = new UpdateEntry();
					entry.parseLine(line);
					entries.add(entry);
					line = br.readLine();
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return entries;
	}
	
	public static void saveUpdateInformation(ArrayList<UpdateEntry> entries) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(Settings.data_directory + "/updates.txt"));
			for (UpdateEntry entry: entries) {
				pw.println(entry);
			}
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<Integer> getSubscriptionIds() {
		ArrayList<Integer> out = new ArrayList<Integer>();
		URL url;
		try {
			url = new URL("https://www.quandl.com/api/v3/subscriptions.json?auth_token=" + Settings.authentication_token);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			InputStream inputStream = conn.getInputStream();
			JsonReader jsonReader = Json.createReader(inputStream);
			JsonObject jsonObject = jsonReader.readObject();
			JsonArray subscriptions = jsonObject.getJsonArray("subscriptions");
			List<JsonObject> subList = subscriptions.getValuesAs(JsonObject.class);
			for (JsonObject sub: subList) {
				out.add(sub.getInt("database_id"));
			}
			inputStream.close();
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static boolean downloadDatabase(UpdateEntry entry) {
		boolean isUpdated = false;
		File dir = new File(Settings.data_directory);
		dir.mkdirs();
		String batchString = "https://www.quandl.com/api/v3/databases/" + entry.code + "/data?auth_token=" + Settings.authentication_token;
		try {
			URL url = new URL(batchString);
			
			// determining if we should perform an update
			Date updateDate = FileUtilities.getModifiedDateOfUrl(url);
			boolean performUpdate = false;
			if (entry.date == null) {
				performUpdate = true;
			} else {
				if (updateDate.after(entry.date)) performUpdate = true;
			}
			
			if (performUpdate) {
				isUpdated = true;
				entry.date = updateDate;
				
				// download and unzip
				File updatedDatabase = FileUtilities.unzipUrl(url, dir);
				
				// delete old file if exists
				if (updatedDatabase != null) {
					File oldDatabase = new File(dir, entry.code + ".csv");
					if (oldDatabase.exists()) oldDatabase.delete();
					updatedDatabase.renameTo(oldDatabase);
				}
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

}
;