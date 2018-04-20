package simulateurARM;

public class Ram implements Memory {
	/**
	 * The default size of the memory
	 */
	public static final int DEFAULT_SIZE = 10000;
	
	/**
	 * A table of byte that represent the memory
	 */
	private byte[] memory;

	/**
	 * A constructor of a RAM with a specified size
	 * @param size The size of the RAM
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
	 * @param myAddress The address where we get the byte
	 * @return The byte corresponds to the address specified
	 */
	public byte getByte(Address myAddress) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		return this.memory[myAddress.getAddress()];
	}
	
	/**
	 * @param myAddress The address where we set the byte
	 * @param myByte 
	 * @return The byte set in the memory
	 */
	public byte setByte(Address myAddress, byte myByte) throws InvalidMemoryAddressException {
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		this.memory[myAddress.getAddress()] = myByte;
		return this.memory[myAddress.getAddress()];
	}
	
	/**
	 * @param myAddress The address where we get the word
	 * @return The Word corresponds to the address specified
	 */
	public short getWord(Address myAddress) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		short myWord=(short)((this.memory[myAddress.getAddress()+1]&0xFF) | (this.memory[myAddress.getAddress()]&0xFF)<<8 );
		return myWord;
	}
	
	/**
	 * @param myAddress The address where we set the word
	 * @param myWord The word set in the memory
	 */
	public void setWord(Address myAddress, short myWord) throws InvalidMemoryAddressException {
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(myWord & 0xff);
		bytes[1] = (byte)((myWord >> 8) & 0xff);
		
		this.memory[myAddress.getAddress()] = bytes[1];
		this.memory[myAddress.getAddress()+1] = bytes[0];
		
	}
	
	/**
	 * @param
	 */
	public int get(Address myAddress) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		int myValue = this.memory[myAddress.getAddress()+3] << 24+this.memory[myAddress.getAddress()+2] << 16+this.memory[myAddress.getAddress()+1] << 8+this.memory[myAddress.getAddress()];
		return myValue;
	}
	
	/**
	 * @param
	 */
	public void set(Address myAddress, int myValue) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
	}
	
	/**
	 * Put all the byte of the memory to 0.
	 */
	public void cleanMemory(){
		for(int i=0; i < memory.length; i++ ) {
			this.memory[i]=0;
		}
	} 

} 