package projetarm_v2.simulator.core;

import java.util.Arrays;

public class RamChunk {
	private byte[] bytes;
	public final long startingAddress;
	
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
		return "RamChunk [bytes=" + Arrays.toString(bytes) + ", startingAddress=" + startingAddress + "]";
	}
}
