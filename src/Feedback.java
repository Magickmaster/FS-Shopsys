import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Feedback {

	Pin pin = RaspiPin.GPIO_29;
	GpioPinDigitalOutput output;
	long time = 200L;

	public Feedback() {
		if (Manager.gpio != null)
			output = Manager.gpio.provisionDigitalOutputPin(pin);
	}

	void buy() {
		if (output != null)
			Utility.executor.execute(() -> output.pulse(1 * time, PinState.HIGH, true));
	}

	void cancel() {
		if (output != null)
			Utility.executor.execute(() -> {
				output.pulse(1 * time, PinState.HIGH, true);
				output.pulse(3 * time, PinState.LOW, true);
				output.pulse(1 * time, PinState.HIGH, true);
			});

	}

	void rebate() {
		if (output != null)
			Utility.executor.execute(() -> {
				output.pulse(1 * time, PinState.HIGH, true);
			});
	}

	void timeout() {
		if (output != null)
			Utility.executor.execute(() -> {
				output.pulse(1 * time, PinState.HIGH, true);
				output.pulse(1 * time, PinState.LOW, true);
				output.pulse(1 * time, PinState.HIGH, true);
				output.pulse(1 * time, PinState.LOW, true);
				output.pulse(1 * time, PinState.HIGH, true);
			});
	}

	void submit() {
		Utility.executor.execute(() -> {
			output.pulse(2 * time, PinState.HIGH, true);
			output.pulse(1 * time, PinState.LOW, true);
			output.pulse(2 * time, PinState.HIGH, true);
		});
	}
}
