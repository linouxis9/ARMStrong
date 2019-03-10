/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.boilerplate;

public class InvalidInstructionException extends RuntimeException {
	
	private static final long serialVersionUID = 5458289081653091341L;
	private final int line;

	public InvalidInstructionException(String message, int line) {
		super(message);
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}
