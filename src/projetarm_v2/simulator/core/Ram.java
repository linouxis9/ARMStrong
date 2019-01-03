package projetarm_v2.simulator.core;

import java.util.Collection;
import java.util.HashMap;

import unicorn.ReadHook;
import unicorn.Unicorn;
import unicorn.WriteHook;

public class Ram {

	public static final int CHUNK_SIZE = 32;
	public final HashMap<Long, RamChunk> memory;

	public Ram() {
		this.memory = new HashMap<>();
	}

	public byte getByte(long myAddress) {
		long block = myAddress / CHUNK_SIZE;
		RamChunk chunk = this.memory.get(block);

		if (chunk == null) {
			return 0;
		}

		return chunk.getByte((int) (myAddress % CHUNK_SIZE));
	}

	public void setByte(long myAddress, byte value) {
		long block = myAddress / CHUNK_SIZE;
		RamChunk chunk = this.memory.get(block);

		if (chunk == null) {
			chunk = new RamChunk(myAddress - myAddress % CHUNK_SIZE, CHUNK_SIZE);
			this.memory.put(myAddress / CHUNK_SIZE, chunk);
		}

		chunk.setByte((int) myAddress % CHUNK_SIZE, value);
	}

	/**
	 * Get a 16 bits half-word (Little Endian) from the memory
	 * 
	 * @param myAddress The address where to get the half-word
	 * @return The half-word stored at the specified address
	 */
	public short getHWord(long myAddress) {

		return (short) ((getByte(myAddress + 1) & 0xFF) | (getByte(myAddress) & 0xFF) << 8);
	}

	/**
	 * Set a 16 bits half-word (Little Endian) in the memory
	 * 
	 * @param myAddress The address where to set the half-word
	 * @param myHWord   The half-word to set in the memory
	 */
	public void setHWord(long myAddress, short myHWord) {

		byte[] bytes = new byte[2];

		bytes[0] = (byte) (myHWord & 0xff);
		bytes[1] = (byte) ((myHWord >> 8) & 0xff);

		this.setByte(myAddress, bytes[1]);
		this.setByte(myAddress + 1, bytes[0]);

	}

	public int getValue(long myAddress) {

		return ((getByte(myAddress + 3) & 0xFF) | (getByte(myAddress + 2) & 0xFF) << 8
				| (getByte(myAddress + 1) & 0xFF) << 16 | (getByte(myAddress) & 0xFF) << 24);

	}

	public void setValue(long myAddress, int myWord) {

		byte[] bytes = new byte[4];

		// TODO We could loop the whole method
		// TODO If we implement the usage of Big Endian on the processor, we need to
		// check this here

		bytes[0] = (byte) (myWord);
		bytes[1] = (byte) (myWord >> 8);
		bytes[2] = (byte) (myWord >> 16);
		bytes[3] = (byte) (myWord >> 24);

		this.setByte(myAddress, bytes[3]);
		this.setByte(myAddress + 1, bytes[2]);
		this.setByte(myAddress + 2, bytes[1]);
		this.setByte(myAddress + 3, bytes[0]);

	}

	public Collection<RamChunk> getRamChunks() {
		return this.memory.values();
	}

	public ReadHook getNewReadHook() {
		return new ReadHookRam(this);
	}

	public WriteHook getNewWriteHook() {
		return new WriteHookRam(this);
	}

	public String toString() {
		return this.memory.toString();
	}

	public boolean isSpecialAddress(long myAddress) {
		return myAddress == 0xFFFFFFF;
	}

	private class ReadHookRam implements ReadHook {

		private final Ram ram;

		public ReadHookRam(Ram ram) {
			this.ram = ram;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			System.out.print(String.format(">>> Tracing READ at 0x%x, block size = 0x%x\n", address, size));

			// On intercepte uniquement si c'est une adresse mémoire qui peut se
			// désynchronisée entre sa valeur dans l'objet Ram et la RAM intégrée à Unicorn.
			if (isSpecialAddress(address)) {
				System.out.println("GOTYA!\n");
				byte[] value = { this.ram.getByte(address), this.ram.getByte(address + 1),
						this.ram.getByte(address + 2), this.ram.getByte(address + 3) };
				u.mem_write(address, value);
			}
		}
	}

	private class WriteHookRam implements WriteHook {

		private final Ram ram;

		public WriteHookRam(Ram ram) {
			this.ram = ram;
		}

		public void hook(Unicorn u, long address, int size, long value, Object user_data) {
			System.out.print(String.format(">>> Tracing WRITE at 0x%x, block size = 0x%x.. Saving in virtual RAM...\n", address, size));
			this.ram.setValue(address, Math.toIntExact(value));
		}
	}
}
