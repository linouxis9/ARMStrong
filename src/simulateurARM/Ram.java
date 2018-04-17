package simulateurARM;

public class Ram implements Memory {

	// TODO write javadoc comment
	/**
	 * 
	 */
	public static final int DEFAULT_SIZE = 10000;

	// TODO write javadoc comment
	/**
	 * 
	 */
	private byte[] memory;


	// TODO write javadoc comment
	/**
	 * 
	 */
	public Ram(int size) {
		this.memory = new byte[size];
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public Ram() {
		this.memory = new byte[DEFAULT_SIZE];
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public byte getByte(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public byte setByte(Address myAddress, byte myByte) throws InvalidMemoryAddressException {
		return 0;

	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public short getWord(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}

	// TODO write javadoc comment
	/**
	 * 
	 */
	public short setWord(Address myAddress, short myWord) throws InvalidMemoryAddressException {
		return 0;

	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public int get(Address myAddress) throws InvalidMemoryAddressException{
		return 0;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public int set(Address myAddress, int value) throws InvalidMemoryAddressException{
		return 0;
	}
	
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void cleanMemory(){

	}
}
