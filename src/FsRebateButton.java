
import com.pi4j.io.gpio.Pin;

public class FsRebateButton extends PhysicalButton {

	public FsRebateButton(Pin pin) {
		super(pin);
	}

	@Override
	public void fire() {
		Manager.markFsRebate();
	}

	@Override
	public String getType() {
		return "Rebate Button";
	}

}
