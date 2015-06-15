
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

/**
 * 
 * @author Brian Risk
 *
 */
public class U {
	
	/**
	 * Shorthand to print to terminal
	 * @param o
	 */
	public static void p(Object o) {
		System.out.println(o);
	}
	
	
	/**
	 * Sleeeeep
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
		    Thread.sleep(millis);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Loads text file as ArrayList of strings, each line an element
	 * @param file
	 * @return
	 */
	public static ArrayList<String> loadFileAsList(File file) {
		ArrayList<String> out = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				if (!line.trim().isEmpty()) {
					out.add(line);
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return out;
	}
	


}
