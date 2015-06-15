import java.text.ParseException;
import java.util.Date;


public class UpdateEntry {
	
	String code = null;
	Date date = null;
	
	public UpdateEntry() {}
	
	public void parseLine(String line) {
		String [] chunks = line.split("\t");
		code = chunks[0];
		try {
			date = Constants.dateISO_8601.parse(chunks[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return code + "\t" + Constants.dateISO_8601.format(date);
	}

}
