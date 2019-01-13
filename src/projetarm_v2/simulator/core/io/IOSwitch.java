package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOSwitch extends IOComponent {

	protected IOSwitch(RamRegister port, int shift) {
		super(port, shift);
	}
	
	public boolean flip() {
		this.set(!this.isOn());
		return this.isOn();
	}
}
