package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.Register;

abstract class IOComponent {

	private final Register register;
	private final int shift;
	
	protected IOComponent(Register port, int shift) {
		this.register = port;
		this.shift = shift;
	}
	
	protected boolean isOn() {
		return this.register.getBit(shift);
	}
	
	protected void set(boolean value) {
		this.register.setBit(shift, value);
	}
}
