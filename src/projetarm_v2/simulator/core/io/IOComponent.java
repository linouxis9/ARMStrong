package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

abstract class IOComponent {

	private final RamRegister register;
	private final int shift;
	
	protected IOComponent(RamRegister port, int shift) {
		this.register = port;
		this.shift = shift;
	}
	
	public boolean isOn() {
		return this.register.getBit(shift);
	}
	
	public void set(boolean value) {
		this.register.setBit(shift, value);
	}
	
	public int getShiftValue() {
		return this.shift;
	}
	
	public long getPortAddress() {
		return this.register.getAddress();
	}
	
	public String toString() {
		return "[Adr: 0x" + Long.toHexString(this.register.getAddress()) + ", Bits N°" + this.shift + "]";
	}
}
