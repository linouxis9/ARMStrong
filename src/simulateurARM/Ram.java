package simulateurARM;

public class Ram implements Memory {

	public static final int DEFAULT_SIZE = 10000;

	private byte[] memory;


	public Ram(int size) {
		this.memory = new byte[size];
	}

	public Ram() {
		this.memory = new byte[DEFAULT_SIZE];
	}

	public byte getByte(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}

	public byte setByte(Address myAddress, byte myByte) throws InvalidMemoryAddressException {
		return 0;

	}
	
	public short getWord(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}

	public short setWord(Address myAddress, short myWord) throws InvalidMemoryAddressException {
		return 0;

	}
	
	public int get(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}
	
	public int set(Address myAddress, int value) throws InvalidMemoryAddressException{
		return 0;
	}
	public void cleanMemory(){

	}

}
