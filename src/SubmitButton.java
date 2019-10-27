import com.pi4j.io.gpio.Pin;

public class SubmitButton extends PhysicalButton {

	public SubmitButton(Pin pin) {
		super(pin);
	}


	
	@Override
	public void fire() {
		Manager.submitTransaction();

	}

	@Override
	public String getType() {
		return "Submit";
	}

}
