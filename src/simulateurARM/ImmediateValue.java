package simulateurARM;

public class ImmediateValue implements Operand2 {
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	private final int value;
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public ImmediateValue(int value) {
		this.value = value;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		return value;
	}

}
