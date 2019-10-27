
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Purchase {

	static final String request = "INSERT INTO " + Settings.tableName + " VALUES(?, ?, ?)";
	double price;
	double fsprice;

	volatile boolean fsPurchase = false;
	String type;
	Timestamp purchaseTime;

	public Purchase(double price, double fsprice, String typeName, Timestamp timestamp) {
		this.type = typeName;
		this.price = price;
		this.fsprice = fsprice;
		this.purchaseTime = timestamp;
	}

	public Purchase(double price, double fsprice, String typeName) {
		this(price, fsprice, typeName, new Timestamp(System.currentTimeMillis()));
	}

	public synchronized void setFsRebate(boolean isRebate) {
		fsPurchase = isRebate;
	}

	public void send() {
		
		if (Settings.SqlEnabled) {
			Connection conn;

			try {
				conn = ServerConnection.getConnection();
			} catch (SQLException e) {
				Logger.log("Could not open SQL server connection!", Logger.Type.SQL);
				return;
			}

			PreparedStatement st = null;
			try {

				st = conn.prepareStatement(request);

				// Fill request data
				st.setTimestamp(1, purchaseTime);
				st.setString(2, type);
				if (!fsPurchase) {
					st.setDouble(3, price);
				} else {
					st.setDouble(3, fsprice);
				}
				st.execute();
				st.close();
				Logger.log("Sent purchase " + this.toString() + " successfully", Logger.Type.SQL);
				// Close the statement
			} catch (SQLException e) {
				System.err.println("Could not send purchase " + this.toString() + ": " + e);
			} finally {
				if (st != null)
					try {
						st.close();
					} catch (SQLException e) {
						Logger.log("What? Could not close SQL statement!", Logger.Type.SQL);
					}
			}
		} else {
			Logger.log("[SQL DISABLED] Performing purchase " + this.toString());
		}
	}

	public double getPrice() {
		if (fsPurchase)
			return fsprice;
		else
			return price;
	}

	@Override
	public String toString() {
		return type + " " + price + " " + purchaseTime;
	}

}
