
import com.pi4j.io.gpio.Pin;

public class PurchaseButton extends PhysicalButton {

	private double price = 1;
	private double fsprice = 1;
	private String typeName = "Error";

	public PurchaseButton(Pin gpioPin, double price, double fsprice, String typeName) {
		super(gpioPin);

		this.price = price;
		this.fsprice = fsprice;
		this.typeName = typeName;
		
	}

	@Override
	public void fire() {
		Manager.submitPurchase(new Purchase(price, fsprice, typeName));
	}

	public String toString() {
		return pin + "-" + typeName + " Price: " + price;
	}

	public String getType() {
		return typeName;
	}
}
