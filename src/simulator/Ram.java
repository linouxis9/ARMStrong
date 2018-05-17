package simulator;

import java.util.Arrays;

/**
 * A class that represents a sort of memory used by the Cpu class
 */
public class Ram implements Memory {
	/**
	 * The default size of the memory (1 MB)
	 */
	public static final int DEFAULT_SIZE = 1000000;

	/**
	 * An array of bytes that represents the memory
	 */
	private byte[] memory;

	/**
	 * Make a Ram instance that can hold the specified size
	 * 
	 * @param size
	 *            The size of the Ram
	 */
	public Ram(int size) {
		this.memory = new byte[size];
	}
	
	/**
	 * Make a Ram instance that can hold the default size
	 */
	public Ram() {
		this.memory = new byte[DEFAULT_SIZE];
	}
	
	/**
	 * Get a byte from the memory
	 * 
	 * @param myAddress
	 *            The address where we retrieve the byte
	 * @return The byte corresponding stored at the specified address
	 */
	public byte getByte(Address myAddress) {
		
		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}
		
		return this.memory[myAddress.getAddress()];
	}
	
	/**
	 * Set a byte in the memory
	 * 
	 * @param myAddress
	 *            The address where we set the byte
	 * @param myByte
	 *            The byte to set at the specified address
	 */
	public void setByte(Address myAddress, byte myByte) {
		
		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}
		
		this.memory[myAddress.getAddress()] = myByte;
	}
	
	/**
	 * Get a 16 bits half-word (Little Endian) from the memory
	 * 
	 * @param myAddress
	 *            The address where to get the half-word
	 * @return The half-word stored at the specified address
	 */
	public short getHWord(Address myAddress) {
		
		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}

		return (short) ((this.memory[myAddress.getAddress() + 1] & 0xFF)
				| (this.memory[myAddress.getAddress()] & 0xFF) << 8);
	}
	
	/**
	 * Set a 16 bits half-word (Little Endian) in the memory
	 * 
	 * @param myAddress
	 *            The address where to set the half-word
	 * @param myHWord
	 *            The half-word to set in the memory
	 */
	public void setHWord(Address myAddress, short myHWord) {

		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}
		
		byte[] bytes = new byte[2];
		
		bytes[0] = (byte)(myHWord & 0xff);
		bytes[1] = (byte)((myHWord >> 8) & 0xff);
		
		this.memory[myAddress.getAddress()] = bytes[1];
		this.memory[myAddress.getAddress()+1] = bytes[0];
		
	}
	
	/**
	 * Get a 32 bits word from the memory
	 * 
	 * @param myAddress
	 *		The address where to get the word
	 * @return The word stored at the specified address
	 */
	public int getValue(Address myAddress) {

		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}

		return  ((this.memory[myAddress.getAddress() + 3] & 0xFF)
				| (this.memory[myAddress.getAddress() + 2] & 0xFF) << 8
				| (this.memory[myAddress.getAddress() + 1] & 0xFF) << 16
				| (this.memory[myAddress.getAddress()] & 0xFF) << 24);
	}
	
	/**
	 * Set a 32 bits half-word (Little Endian) in the memory
	 * 
	 * @param myAddress
	 *            The address where to set the word
	 * @param myWord
	 *            The word to set in the memory
	 */
	public void setValue(Address myAddress, int myWord) {

		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}
		
		byte[] bytes = new byte[4];
		
		bytes[0] = (byte)(myWord);
		bytes[1] = (byte)(myWord >> 8);
		bytes[2] = (byte)(myWord >> 16);
		bytes[3] = (byte)(myWord >> 24);
		
		this.memory[myAddress.getAddress()] = bytes[3];
		this.memory[myAddress.getAddress()+1] = bytes[2];
		this.memory[myAddress.getAddress()+2] = bytes[1];
		this.memory[myAddress.getAddress()+3] = bytes[0];
		
	}
	
	/**
	 * Set all the bytes of the memory to 0.
	 */
	public void cleanMemory() {
		Arrays.fill(this.memory, (byte)0);
	}

	@Override
	public String toString() {
		return "Ram [memory=" + Arrays.toString(memory) + "]";
	}
}
