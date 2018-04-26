package simulator;

//TODO write javadoc comment
/**
 * 
 */
public interface Memory {

	// TODO write javadoc comment
	/**
	 * 
	 */
	public byte getByte(Address address) throws InvalidMemoryAddressException;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public byte setByte(Address address, byte value) throws InvalidMemoryAddressException;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public short getHWord(Address address) throws InvalidMemoryAddressException;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void setHWord(Address address, short value) throws InvalidMemoryAddressException;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public int get(Address address) throws InvalidMemoryAddressException;

	// TODO write javadoc comment
	/**
	 * 
	 */
	public void set(Address address, int value) throws InvalidMemoryAddressException;
}
