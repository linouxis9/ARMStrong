/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.core.io;

import projetarm_v2.simulator.core.RamRegister;

public class IOButton extends IOComponent {

	protected IOButton(RamRegister port, int shift, int portNb) {
		super(port, shift, portNb);
	}
	
	public synchronized void push() {
		new Thread(() -> {
			this.set(true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			this.set(false);
		}).start();
	}
}
