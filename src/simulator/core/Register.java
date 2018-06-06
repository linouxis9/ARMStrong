package simulator.core;

public class Register implements Operand2 {

	private int value;
	
	public Register() {
		this.value = 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int register) {
		this.value = register;
	}

	@Override
	public String toString() {
		return "r = [" + value + "]";
	}
}
