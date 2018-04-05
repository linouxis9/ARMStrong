package simulateurARM;

public class Pointer implements Operand2 {
	private Memory mem;
	private Address address;
	
	public Pointer(Memory mem, Address address) {
		this.mem = mem;
		this.address = address;
	}
	
	public int getValue() {
		int value;
		try {
			value = this.mem.get(address);
		}
		catch (Exception e) {
			value = 0;
		}
		return value;
	}
	
	public byte getByte() {
		byte value;
		try {
			value = this.mem.getByte(address);
		}
		catch (Exception e) {
			value = 0;
		}
		return value;
	}
	
	public short getWord() {
		short value;
		try {
			value = this.mem.getWord(address);
		}
		catch (Exception e) {
			value = 0;
		}
		return value;
	}
}
