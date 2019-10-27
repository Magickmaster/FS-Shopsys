
import com.pi4j.io.gpio.Pin;

public class CancelButton extends PhysicalButton {

	public CancelButton(Pin pin) {
		super(pin);
	}

	@Override
	public void fire() {
		Manager.cancelTransaction();
	}

	@Override
	public String getType() {
		return "Cancel Button";
	}

}
