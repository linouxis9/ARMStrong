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
	 * @return A byte corresponds to the address specified
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
	 * @param myAddress
	 * @return a Word 
	 */
	public short getWord(Address myAddress) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		//int myWord = this.memory[myAddress.getAddress()+3] << 24+this.memory[myAddress.getAddress()+2] << 16+this.memory[myAddress.getAddress()+1] << 8+this.memory[myAddress.getAddress()];
		return 0;
	}
	
	/**
	 * @param myAddress
	 * @param myWord
	 * @return a Word 
	 */
	public short setWord(Address myAddress, short myWord) throws InvalidMemoryAddressException {
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		return 0;
		
	}
	
	/**
	 * @param
	 */
	public int get(Address myAddress) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		return 0;
	}
	
	/**
	 * @param
	 */
	public int set(Address myAddress, int value) throws InvalidMemoryAddressException{
		if((myAddress.getAddress()<0)||(myAddress.getAddress()>=memory.length)){
			throw new InvalidMemoryAddressException();
		}
		return 0;
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