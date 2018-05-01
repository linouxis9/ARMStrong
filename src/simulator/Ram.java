package simulator;

import java.util.Arrays;

/**
 * The class that represents the RAM used by the CPU
 */
public class Ram implements Memory {
	/**
	 * The default size of the memory
	 */
	public static final int DEFAULT_SIZE = 1000000;

	/**
	 * A table of bytes that represents the memory
	 */
	private byte[] memory;

	/**
	 * A constructor of a RAM instance with a specified size
	 * 
	 * @param size
	 */
	public Ram(int size) {
		this.memory = new byte[size];
	}
	
	/**
	 * A constructor of a RAM with the default size
	 * @param size The size of the RAM
	 */
	public Ram() {
		this.memory = new byte[DEFAULT_SIZE];
	}
	
	/**
	 * Get a byte from the memory
	 * 
	 * @param myAddress
	 *            The address where we retrieve the byte
	 * @return The byte corresponding to the address specified
	 */
	public byte getByte(Address myAddress) throws InvalidMemoryAddressException{
		
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
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
	 *            The byte to set at the address specified
	 */
	public void setByte(Address myAddress, byte myByte) throws InvalidMemoryAddressException {

		if ((myAddress.getAddress() < 0) || (myAddress.getAddress() >= memory.length)) {
			throw new InvalidMemoryAddressException();
		}
		
		this.memory[myAddress.getAddress()] = myByte;
	}
	
	/**
	 * We convert the 2 bytes of the specified address to short
	 * We make a shift of 8 bits (1 byte) for the second byte
	 * @param myAddress The address where we get the half word
	 * @return The half Word corresponds to the address specified
	 */
	public short getHWord(Address myAddress) throws InvalidMemoryAddressException{
		
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		
		short myWord=(short)((this.memory[myAddress.getAddress()+1]&0xFF) | (this.memory[myAddress.getAddress()]&0xFF)<<8 );
		return myWord;
	}
	
	/**
	 * We convert a short to a byte array
	 * We put each byte in the memory by invert the byte order (Little endian)
	 * @param myAddress The address where we set the half word
	 * @param myWord The half word set in the memory
	 */
	public void setHWord(Address myAddress, short myHWord) throws InvalidMemoryAddressException {
		
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		
		byte[] bytes = new byte[2];
		
		bytes[0] = (byte)(myHWord & 0xff);
		bytes[1] = (byte)((myHWord >> 8) & 0xff);
		
		this.memory[myAddress.getAddress()] = bytes[1];
		this.memory[myAddress.getAddress()+1] = bytes[0];
		
	}
	
	/**
	 * We convert the 4 bytes of the specified address to int
	 * We make a shift of 8 bits (1 byte) to each byte (8,16,24)
	 * @param The address where we get the word
	 * @return The Word corresponds to the address specified
	 */
	public int getValue(Address myAddress) throws InvalidMemoryAddressException{
		
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		
		int myWord=(int)((this.memory[myAddress.getAddress()+3] & 0xFF) | (this.memory[myAddress.getAddress()+2] & 0xFF)<<8 | (this.memory[myAddress.getAddress()+1] & 0xFF)<<16 | (this.memory[myAddress.getAddress()] & 0xFF)<<24);
		return myWord;
		
	}
	
	/**
	 * We convert an int to a byte array
	 * We put each byte in the memory by invert the byte order (Little endian)
	 * @param myAddress The address where we set the word
	 * @param myValue The word set in the memory
	 */
	public void setValue(Address myAddress, int myWord) throws InvalidMemoryAddressException{
		
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
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
