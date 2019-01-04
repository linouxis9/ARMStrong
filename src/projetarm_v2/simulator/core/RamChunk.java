package projetarm_v2.simulator.core;

import java.util.Arrays;

public class RamChunk {
	public final long startingAddress;
	private byte[] bytes;
	
	public RamChunk(long startingAddress, int size) {
		this.startingAddress = startingAddress;
		this.bytes = new byte[size];
	}
	
	public byte getByte(int offset) {
		return this.bytes[offset];
	}
	
	public void setByte(int offset, byte value) {
		this.bytes[offset] = value;
	}
	
	public byte[] getChunk() {
		return this.bytes;
	}
	
	@Override
	public String toString() {
		return "RamChunk [startingAddress=0x" + Long.toHexString(startingAddress) +", bytes=" + Arrays.toString(bytes) + "]";
	}
}
