package projetarm_v2.simulator.core;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleRegister extends Register {
	private AtomicLong value = new AtomicLong(0);
	
	public SimpleRegister() {}

	public SimpleRegister(int value) {
		this.value.set(value);
	}
	
	public int getValue() {
		Long value = this.value.get();
		return value.intValue();
	}

	@Override
	public void setValue(int value) {
		this.value.set(value);
	}

}
