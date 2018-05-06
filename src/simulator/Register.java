package simulator;

public class Register implements Operand2 {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private int value;

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
		return "Register [register=" + value + "]";
	}
}
