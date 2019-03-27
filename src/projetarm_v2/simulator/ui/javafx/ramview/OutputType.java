/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx.ramview;

/**
 * represent a output mode
 */
public enum OutputType {
	/**
	 * the hexadecimal representation of ram content
	 */
	HEX,
	/**
	 * the signed decimal representation of ram content
	 */
	SIG_DEC,
	/**
	 * the unsigned decimal representation of ram content
	 */
	UNSIG_DEC,
	/**
	 * the ascii representation of ram content
	 */
	ASCII;
}
