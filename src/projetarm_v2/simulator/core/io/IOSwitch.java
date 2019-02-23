package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOSwitch extends IOComponent {

	protected IOSwitch(RamRegister port, int shift, int portNb) {
		super(port, shift, portNb);
	}
	
	public boolean flip() {
		this.set(!this.isOn());
		return this.isOn();
	}
}
