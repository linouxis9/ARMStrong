package projetarm_v2.simulator.core.io;

import java.util.ArrayList;
import java.util.List;

import projetarm_v2.simulator.core.Ram;

public class PORTManager {
	public static final long DEFAULT_PORT_ADDRESS = 0x0;
	public static final long DEFAULT_DIR_ADDRESS = 0x100;
	
	private final List<IOx> ports = new ArrayList<>();
	private final Ram ram;
	private long firstPortAddress;
	private long firstDirAddress;
	
	public PORTManager(Ram ram, long firstPortAddress, long firstDirAddress) {
		this.ram = ram;
		this.firstPortAddress = firstPortAddress;
		this.firstDirAddress = firstDirAddress;
		this.ports.add(new IOx(ram, this.firstPortAddress++, this.firstDirAddress++));
	}

	private IOx getNextAvailablePort() {
		return getNextAvailablePort(1);
	}
	
	private IOx getNextAvailablePort(int nbBits) {
		for (IOx port : ports) {
			if (port.hasNAvailableSlot(nbBits)) {
				return port;
			}
		}
		
		IOx newPort = new IOx(ram, firstPortAddress++, firstDirAddress++);
		return newPort;
	}
	
	public boolean remove(IOComponent component) {
		boolean flag;
		
		for (IOx port : ports) {
			flag = port.removeComponent(component);
			if (flag) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean remove(IO7Segment component) {
		boolean flag;
		
		for (int i = 0; i < 7; i++) {
			flag = this.remove(component.getSegment(i));
			if (flag) {
				return true;
			}
		}
		
		return false;
	}
	
	public IOLed newIOLed() {
		return this.getNextAvailablePort().newIOLed();
	}

	public IOButton newIOButton() {
		return this.getNextAvailablePort().newIOButton();
	}
	
	public IOSwitch newIOSwitch() {
		return this.getNextAvailablePort().newIOSwitch();
	}
	
	public IO7Segment newIO7Segment() {
		return this.getNextAvailablePort(7).newIO7Segment();
	}
}
