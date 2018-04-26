package simulator;

public class Pointer implements Operand2 {

	// TODO write javadoc comment
	/**
	 * 
	 */
	private Memory mem;
	private Address address;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Pointer(Memory mem, Address address) {
		this.mem = mem;
		this.address = address;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int getValue() {
		int value;
		try {
			value = this.mem.get(address);
		} catch (Exception e) {
			value = 0;
		}
		return value;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public byte getByte() {
		byte value;
		try {
			value = this.mem.getByte(address);
		} catch (Exception e) {
			value = 0;
		}
		return value;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public short getHWord() {
		short value;
		try {
			value = this.mem.getHWord(address);
		} catch (Exception e) {
			value = 0;
		}
		return value;
	}
}