package simulateurARM;

public class ImmediateValue implements Operand2 {
	public ImmediateValue(int value) {
		this.value = value;
	}

	private final int value;

	public int getValue() {
		return value;
	}

}
