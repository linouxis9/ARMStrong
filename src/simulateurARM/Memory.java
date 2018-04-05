package simulateurARM;

public interface Memory {
	public int get(Address address) throws InvalidMemoryAddressException;
	public int set(Address address, int value) throws InvalidMemoryAddressException;
	public byte getByte(Address address) throws InvalidMemoryAddressException;
	public byte setByte(Address address, byte value) throws InvalidMemoryAddressException;
	public short getWord(Address address) throws InvalidMemoryAddressException;
	public short setWord(Address address, short value) throws InvalidMemoryAddressException;
}
