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
