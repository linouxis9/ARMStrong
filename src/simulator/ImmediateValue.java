package simulator;

/**
 * An immediate value is a piece of data that is stored as part of the instruction itself instead of being in a memory
 * location or a register. Immediate values are typically used in instructions that load a value or performs an
 * arithmetic or a logical operation on a constant.
 */
public class ImmediateValue implements Operand2 {

	/**
	 * An immediate value
	 */
	private final int value;

	/**
	 * Initializes the immediate value
	 */
	public ImmediateValue(int value) {
		this.value = value;
	}

	/**
	 * Returns an immediate value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "#" + value;
	}

}
