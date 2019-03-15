/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import unicorn.ReadHook;
import unicorn.Unicorn;
import unicorn.WriteHook;

public class Ram {

	public static final int CHUNK_SIZE = 32;
	public static final int DEFAULT_RAM_SIZE = 2 * 1024 * 1024; // 2 MB
	public final Map<Long, RamChunk> memory;

	public byte randomPattern = 0;
	
	public Ram() {
		this.memory = new HashMap<>();
	}

	public void clear() {
		this.memory.clear();
	}
	
	public void setRandomPattern(byte pattern) {
		this.randomPattern = pattern;
	}
	
	public byte getRandomPattern() {
		return this.randomPattern;
	}
	
	public byte getByte(long myAddress) {
		long block = myAddress / CHUNK_SIZE;
		RamChunk chunk = this.memory.get(block);

		if (chunk == null || myAddress < 0) {
			return randomPattern;
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

		return (short) ((getByte(myAddress) & 0xFF) | (getByte(myAddress + 1) & 0xFF) << 8);
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

		this.setByte(myAddress + 1, bytes[1]);
		this.setByte(myAddress, bytes[0]);

	}

	public int getValue(long myAddress) {

		return ((getByte(myAddress) & 0xFF) | (getByte(myAddress + 1) & 0xFF) << 8
				| (getByte(myAddress + 2) & 0xFF) << 16 | (getByte(myAddress + 3) & 0xFF) << 24);

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

		this.setByte(myAddress + 3, bytes[3]);
		this.setByte(myAddress + 2, bytes[2]);
		this.setByte(myAddress + 1, bytes[1]);
		this.setByte(myAddress, bytes[0]);

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

	private class ReadHookRam implements ReadHook {

		private final Ram ram;

		public ReadHookRam(Ram ram) {
			this.ram = ram;
		}

		public void hook(Unicorn u, long address, int size, Object user_data) {
			byte[] value = new byte[size];

			for (int i = 0; i < size; i++) {
				value[i] = this.ram.getByte(address + i);
			}

			u.mem_write(address, value);
		}
	}

	private class WriteHookRam implements WriteHook {

		private final Ram ram;

		public WriteHookRam(Ram ram) {
			this.ram = ram;
		}

		public void hook(Unicorn u, long address, int size, long value, Object user_data) {
			for (int i = 0; i < size; i++) {
				this.ram.setByte(address+i, (byte)((value >> i*2) & 0xFF));
			}
		}
	}
}
