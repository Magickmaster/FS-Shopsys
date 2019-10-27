import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class Manager {

	// Singleton, as handles GPIO and only one gpioController may exist and
	// duplicate button stuff is no bueno
	protected static Feedback feedback;
	protected static GpioController gpio;
	protected static List<PhysicalButton> buttons = new LinkedList<>();

	private static Deque<Purchase> proposedPurchases = new LinkedList<>();
	private static boolean fsrebate = false;

	private Manager() {
		throw new UnsupportedOperationException("Static utility class");
	}

	static {
		try {
			// create gpio controller
			gpio = GpioFactory.getInstance();
		} catch (UnsatisfiedLinkError e) {
			Logger.log("Failed to map GPIO: Probably not running on RasPI. ", Logger.Type.WARN);
		}
		setupButtons();
		feedback = new Feedback();
		// feedback.output.blink(50L);

		Logger.log("Setup finished", Logger.Type.INFO);
	}

	public static void submitPurchase(Purchase purchase) {

		Logger.log("Proposed: " + purchase, Logger.Type.PURCHASE);
		proposedPurchases.push(purchase);
		Utility.executor.execute(() -> waitForTimeout(purchase));
		feedback.buy();
	}

	public static void markFsRebate() {
		Logger.log("[FS-Rabatt] \tReducing prices for this transaction");
		fsrebate = true;
		feedback.rebate();
	}

	private static void waitForTimeout(Purchase purchase) {
		try {
			Thread.sleep(Settings.timeout);
			synchronized (proposedPurchases) {
				if (!proposedPurchases.isEmpty() && proposedPurchases.peek() == purchase) {
					cancelTransaction();
					feedback.timeout();
				}
			}
		} catch (InterruptedException e) {
			Logger.log(e.toString());
			Thread.currentThread().interrupt();
		}

	}

	public synchronized static void cancelTransaction() {
		Logger.log("Cancelled " + proposedPurchases.size() + " purchases after delay", Logger.Type.PURCHASE);
		proposedPurchases.clear();
		fsrebate = false;
		feedback.cancel();
	}

	public static synchronized void submitTransaction() {
		for (Purchase p : proposedPurchases) {
			p.setFsRebate(fsrebate);
			p.send();
		}
		proposedPurchases.clear();
		fsrebate = false;
		feedback.submit();
	}

	private static void setupButtons() {

		File f = new File("/home/pi/javashop/buttons.set");
		Logger.log("Trying to open file... Exists? " + f.exists(), Logger.Type.SETUP);
		if (!f.exists()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (f.exists()) {

			try (Scanner sc = new Scanner(new BufferedReader(new FileReader(f)))) {
				Logger.log("Opened file, reading...", Logger.Type.SETUP);
				while (sc.hasNext()) {

					String[] line = sc.nextLine().split(";");
					if (line.length == 0) {
						continue;
					}

					Pin pin = RaspiPin.getPinByName(line[0]);

					// 3 items: Purchase button

					if (line.length == 4) {
						double price = Double.parseDouble(line[2]);
						double fsprice = Double.parseDouble(line[3]);
						Logger.log("Added purchase button for " + line[1].trim() + ", price " + price + " | " + fsprice
								+ " @ " + pin, Logger.Type.SETUP);
						buttons.add(new PurchaseButton(pin, price, fsprice, line[1].trim()));
					}

					else if (line.length == 2) {
						switch (line[1].trim().toUpperCase()) {
						case "CANCEL":
							Logger.log("[Setup]\t\tAdded CANCEL @ " + pin);
							buttons.add(new CancelButton(pin));
							break;
						case "FSRABATT":
							buttons.add(new FsRebateButton(pin));
							Logger.log("[Setup]\t\tAdded FSREBATE @" + pin);
							break;
						case "SUBMIT":
							buttons.add(new SubmitButton(pin));
							Logger.log("Added SUBMIT @" + pin, Logger.Type.SETUP);
							break;
						default:
							Logger.log("[Error]\t\tMalformed special button:[" + line[1] + "]", Logger.Type.WARN);
						}
					}

				}

			} catch (FileNotFoundException e) {
				Logger.log("Buttons file not found!", Logger.Type.WARN);
			}

		}
		Logger.log("Mapped " + buttons.size() + " buttons", Logger.Type.GPIO);
	}

}
