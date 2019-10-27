
public class Main {

	public static void main(String[] args) {
		Logger.log("Starting", Logger.Type.INFO);
		Manager.feedback.buy(); // Signal readiness
		Utility.keepAlive();
	}

}
