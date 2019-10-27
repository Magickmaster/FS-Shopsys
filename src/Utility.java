
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Utility {

	private Utility() {
		throw new UnsupportedOperationException("Utility Class");
	}

	public static final Executor executor = Executors.newCachedThreadPool();

	public static void keepAlive() {
		// keep program running until user aborts (CTRL-C)
		// probably bad practice but it should be running forever
		for (int i = 0; i != Settings.MAX_RUNTIME; i++) {
			System.out.println("[Live] \t\t" + 10 * i + " seconds");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("[Death]");
	}
}
