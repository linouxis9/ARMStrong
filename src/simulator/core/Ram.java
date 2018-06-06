package simulator.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import simulator.core.exceptions.InvalidMemoryAddressException;

/**
 * A class that represents a sort of memory used by the Cpu class
 */
public class Ram implements Memory {
	/**
	 * The default size of the memory (1 MB)
	 */
	public static final int DEFAULT_SIZE = 1000000;

	/**
	 * The default initial capacity
	 */
	public static final int DEFAULT_CAPACITY = 1 << 10;
	
	/**
	 * An array of bytes that represents the memory
	 */
	private Map<Address,Byte> memory;

	/**
	 * Make a Ram instance that can hold the default size
	 */
	public Ram() {
		this.memory = new HashMap<>(DEFAULT_CAPACITY);
	}

	/**
	 * Get a byte from the memory
	 * 
	 * @param myAddress
	 *            The address where we retrieve the byte
	 * @return The byte corresponding stored at the specified address
	 */
	public byte getByte(Address myAddress) {

		return this.memory.getOrDefault(myAddress,(byte)0);
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

		this.memory.put(myAddress,myByte);
	}

	/**
	 * Get a 16 bits half-word (Little Endian) from the memory
	 * 
	 * @param myAddress
	 *            The address where to get the half-word
	 * @return The half-word stored at the specified address
	 */
	public short getHWord(Address myAddress) {

		return (short) ((getByte(myAddress.offset(1)) & 0xFF)
				| (getByte(myAddress) & 0xFF) << 8);
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
		
		byte[] bytes = new byte[2];

		bytes[0] = (byte) (myHWord & 0xff);
		bytes[1] = (byte) ((myHWord >> 8) & 0xff);

		this.memory.put(myAddress,bytes[1]);
		this.memory.put(myAddress.offset(1),bytes[0]);

	}

	/**
	 * Get a 32 bits word from the memory
	 * 
	 * @param myAddress
	 *		The address where to get the word
	 * @return The word stored at the specified address
	 */
	public int getValue(Address myAddress) {

		return  ((getByte(myAddress.offset(3)) & 0xFF)
				| (getByte(myAddress.offset(2)) & 0xFF) << 8
				| (getByte(myAddress.offset(1)) & 0xFF) << 16
				| (getByte(myAddress) & 0xFF) << 24);

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

		byte[] bytes = new byte[4];

		// TODO We could loop the whole method
		
		bytes[0] = (byte) (myWord);
		bytes[1] = (byte) (myWord >> 8);
		bytes[2] = (byte) (myWord >> 16);
		bytes[3] = (byte) (myWord >> 24);

		this.memory.put(myAddress,bytes[3]);
		this.memory.put(myAddress.offset(1),bytes[2]);
		this.memory.put(myAddress.offset(2),bytes[1]);
		this.memory.put(myAddress.offset(3),bytes[0]);

	}

	/**
	 * Set all the bytes of the memory to 0.
	 */
	public void cleanMemory() {
		this.memory.clear();
	}

	@Override
	public String toString() {
		return "Ram [memory=" + memory + "]";
	}
}