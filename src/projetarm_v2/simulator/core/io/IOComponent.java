package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

abstract class IOComponent {

	private final RamRegister register;
	private final int shift;
	
	protected IOComponent(RamRegister port, int shift) {
		this.register = port;
		this.shift = shift;
	}
	
	protected boolean isOn() {
		return this.register.getBit(shift);
	}
	
	protected void set(boolean value) {
		this.register.setBit(shift, value);
	}
	
	public String toString() {
		return "[Adr:" + this.register.getAddress() + ", Bits N°" + this.shift + "]";
	}
}
