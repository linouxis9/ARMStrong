package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOLed extends IOComponent {

	public IOLed(RamRegister register, int shift) {
		super(register, shift);
	}
}
