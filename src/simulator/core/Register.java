package simulator.core;

public class Register implements Operand2 {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private int value;
	
	public Register() {
		this.value = 0;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		return value;
	}

	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setValue(int register) {
		this.value = register;
	}

	@Override
	public String toString() {
		return "r = [" + value + "]";
	}
}
