
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public abstract class PhysicalButton {
	
	Pin pin;
	
	public PhysicalButton(Pin pin) {
		this.pin = pin;
		setupPin();
	}

	public abstract void fire();

	public abstract String getType();

	

	public final Pin getPin() {
		return pin;
	}

	protected final void setupPin() {
		if (Manager.gpio == null) {
			Logger.log("GPIO manager is null", Logger.Type.WARN);
			return;
		}
		
		// gpio pin as an input pin with its internal pull down resistor enabled
		final GpioPinDigitalInput pin = Manager.gpio.provisionDigitalInputPin(getPin(), getType(),
				PinPullResistance.PULL_DOWN);

		pin.setShutdownOptions(true, PinState.LOW);
		pin.setDebounce(10);
		// create and register gpio pin listener

		pin.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				if (event.getState() == PinState.HIGH) {
					fire();
				}
			}
		});
	}

}
