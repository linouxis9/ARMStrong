package projetarm_v2.simulator.core;

public class RamRegister extends Register {

	private final Ram ram;
	private final long myAddress;

	public RamRegister(Ram ram, long address) {
		this.ram = ram;
		this.myAddress = address;
	}
	
	@Override
	public int getValue() {
		return this.ram.getValue(this.myAddress);
	}

	@Override
	public void setValue(int value) {
		this.ram.setValue(this.myAddress, value);
	}
}
