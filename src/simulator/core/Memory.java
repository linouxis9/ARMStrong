package simulator.core;

import simulator.core.exceptions.InvalidMemoryAddressException;

//TODO JAVADOOC
/**
 *
 */
public interface Memory {

	/**
	 * Get the byte at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public byte getByte(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a byte at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted byte 
	 */
	public void setByte(Address address, byte value) throws InvalidMemoryAddressException;

	/**
	 * Get the half word (16bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public short getHWord(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a half word (16bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted half word (16bit value)
	 */
	public void setHWord(Address address, short value) throws InvalidMemoryAddressException;

	/**
	 * Get the word (32bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 */
	public int getValue(Address address) throws InvalidMemoryAddressException;

	/**
	 * Set a word (32bit value) at the given address in the memory
	 * @param address
	 * 		the address targeted
	 * @param value
	 * 		the new value of the targeted word (32bit value)
	 */
	public void setValue(Address address, int value) throws InvalidMemoryAddressException;
}
