package simulator;

public class Register implements Operand2 {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private int value;
	private int id;
	
	
	
	public Register(int id) {
		this.value = 0;
		this.id = id;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		return value;
	}

	public int getId() {
		return id;
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
		return "r" + id;
	}
}
