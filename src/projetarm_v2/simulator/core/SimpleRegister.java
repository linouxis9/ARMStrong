package projetarm_v2.simulator.core;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleRegister implements Register {
	private AtomicInteger value = new AtomicInteger(0);
	
	public SimpleRegister() {}

	public SimpleRegister(int value) {
		this.value.set(value);
	}
	
	public int getValue() {
		return this.value.get();
	}

	@Override
	public void setValue(int value) {
		this.value.set(value);
	}

}
