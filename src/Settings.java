public class Settings {
	// Runtime in tens of seconds. -1 = forever
	public static final int MAX_RUNTIME = -1;

	public static final boolean isDebug = false; // debug mode, currently basically useless
	public static final long timeout = 5000; // Timeout for cancelling mis-purchases

	public static final String SqlDriver = "org.sqlite.JDBC"; // org.gjt.mm.mysql.Driver
	public static final String SqlUName = "shop";
	public static final String SqlPasswd = "passwd";
	public static final String SqlServerUrl = "jdbc:sqlite:/home/pi/javashop/purchases.db";
	public static final String tableName = "PURCHASES";
	public static final boolean SqlEnabled = true; // If db is not yet there

	public static String getSqldriver() {
		return SqlDriver;
	}

	public static String getSqluname() {
		return SqlUName;
	}

	public static String getSqlpasswd() {
		return SqlPasswd;
	}

	public static String getSqlserverurl() {
		return SqlServerUrl;
	}

	public static String getTablename() {
		return tableName;
	}

	public static boolean isSqlenabled() {
		return SqlEnabled;
	}

}
