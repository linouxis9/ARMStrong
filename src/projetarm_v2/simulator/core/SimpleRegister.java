package projetarm_v2.simulator.core;

public class SimpleRegister implements Register {
	private long value = 0;
	
	public SimpleRegister() {}

	public SimpleRegister(long value) {
		this.value = value;
	}
	
	public int getValue() {
		return ((Long)this.value).intValue();
	}

	@Override
	public void setValue(long value) {
		this.value = value;
	}

}
