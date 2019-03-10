/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core;

public interface Register {

	public int getValue();

	public void setValue(int value);

	public default boolean getBit(int shift) {
		return intToBool((this.getValue() >> shift) & 0x1);
	}
	
	public default void setBit(int shift, boolean bit) {
		int value = this.getValue() & ~(1 << shift);
		this.setValue(value | (boolToInt(bit) << shift));
	}
	
	private static  boolean intToBool(int value) {
		return value == 1;
	}
	
	private static int boolToInt(boolean bool) {
		return bool ? 1 : 0;
	}
}