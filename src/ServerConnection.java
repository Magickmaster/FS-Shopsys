
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerConnection {

	private static Connection conn = null;

	private ServerConnection() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static Connection getConnection() throws SQLException {
		synchronized (Manager.class) {
			if (conn == null || conn.isValid(5)) {
				Logger.log("Connecting to database @" + Settings.SqlServerUrl, Logger.Type.SQL);

				try {
					Class.forName(Settings.SqlDriver);
					conn = DriverManager.getConnection(Settings.SqlServerUrl, Settings.SqlUName, Settings.SqlPasswd);
					conn.setAutoCommit(true);
					Logger.log("Connected", Logger.Type.SQL);
				} catch (ClassNotFoundException e) {
					System.err.println("Couldn't load SQL drivers: " + e);
				}
			}

			return conn;
		}
	}

}
