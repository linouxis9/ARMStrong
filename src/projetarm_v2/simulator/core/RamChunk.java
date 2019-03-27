/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

import java.util.Arrays;

public class RamChunk {
	public final long startingAddress;
	private byte[] bytes;
	
	public RamChunk(long startingAddress, RamChunk ramChunk) {
		this.startingAddress = startingAddress;
		byte[] pattern = ramChunk.getChunk(); 
		this.bytes = new byte[pattern.length];
		System.arraycopy(pattern, 0, this.bytes, 0, this.bytes.length);
	}
	
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
