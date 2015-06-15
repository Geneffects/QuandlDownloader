

import java.text.SimpleDateFormat;


/**
 * 
 * @author Brian Risk
 *
 */
public class Constants {
	
	public static final SimpleDateFormat dateQuandl = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat dateISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");	

	public static final long MINUTE = 1000 * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;

}
