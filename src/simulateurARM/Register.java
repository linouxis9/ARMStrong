package simulateurARM;

public class Register implements Operand2 {
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private int register;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		return register;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setValue(int register) {
		this.register = register;
	}
}
