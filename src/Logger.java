import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	private static File logFile = new File("shop.log");

	public static void log(String message) {
		log(message, Type.INFO);
	}

	public static void log(String message, Type type) {
		String logMsg = type.prefix + message;
		System.out.println(logMsg);
		try (FileWriter writer = new FileWriter(logFile)) {

			writer.append(logMsg + '\n');
			writer.flush();
		} catch (IOException e) {
			System.err.println("Could not write log message to file!");
		}
	}

	public enum Type {
		INFO("[INFO]\t\t"), WARN("[WARNING]\t"), SQL("[SQL]\t\t"), SETUP("[SETUP]\t\t"), GPIO("[GPIO]\t\t"),
		PURCHASE("[PURCHASE] \t");
		String prefix;

		Type(String prefix) {
			this.prefix = prefix;
		}

	}
}
