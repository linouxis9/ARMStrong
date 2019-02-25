package projetarm_v2.simulator.core.io;

import java.util.ArrayList;
import java.util.List;

import projetarm_v2.simulator.core.Ram;

public class PORTManager {
	public static final long DEFAULT_PORT_ADDRESS = 0xE000;
	public static final long DEFAULT_DIR_ADDRESS = 0xF000;
	
	private final List<IOx> ports = new ArrayList<>();
	private final Ram ram;
	private long firstPortAddress;
	private long firstDirAddress;
	
	public PORTManager(Ram ram) {
		this.ram = ram;
		this.clear();
	}

	public void clear() {
		this.firstPortAddress = DEFAULT_PORT_ADDRESS;
		this.firstDirAddress = DEFAULT_DIR_ADDRESS;
		this.ports.clear();
		newPort(0);
	}
	
	
	public IOx getNextAvailablePort() {
		return getNextAvailablePort(1);
	}
	
	public IOx getNextAvailablePort(int nbBits) {
		int portNb = 0;
		for (IOx port : ports) {
			if (port.hasNAvailableSlot(nbBits)) {
				return port;
			}
			portNb++;
		}
		newPort(portNb);
		return ports.get(ports.size()-1);
	}
	
	public boolean removeIOComponent(IOComponent component) {
		for (IOx port : ports) {
			if (port.removeComponent(component)) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean remove(IO7Segment component) {
		boolean flag;
		
		for (int i = 0; i < 7; i++) {
			flag = this.removeIOComponent(component.getSegment(i));
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

	public void generateIOComponents(List<IOComponent> components) {
		this.clear();
		int segments = 0;
		
		for (IOComponent component : components) {
			IOx port = this.getPort(component.portNb);
			if (component instanceof IOLed) {
				port.newIOLed(component.shift);
			} else if (component instanceof IOButton) {
				port.newIOButton(component.shift);
			} else if (component instanceof IOSwitch) {
				port.newIOSwitch(component.shift);
			} else if (component instanceof IOSegment) {
				segments++;
				if (segments == 1) {
					port.newIO7Segment(component.shift);
				} else if (segments == 7) {
					segments = 0;
				}
			}
		}
	}

	public List<IOComponent> getComponents() {
		List<IOComponent> components = new ArrayList<>();
		
		for (IOx port : ports) {
			for (IOComponent component : port.getComponents()) {
				if (component != null) {
					components.add(component);
				}
			}
		}
		
		return components;
	}
	
	private IOx getPort(int portNb) {
		while (portNb >= this.ports.size()) {
			newPort(portNb);
		}
		return this.ports.get(portNb);
	}

	private void newPort(int portNb) {
		this.ports.add(new IOx(ram, portNb, firstPortAddress++, firstDirAddress++));
	}
}
