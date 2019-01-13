package projetarm_v2.simulator.core.routines;

import projetarm_v2.simulator.core.Cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CpuConsoleGetString extends CpuRoutine
{
	public static final long ROUTINE_ADDRESS = 0xFF08L;
	private ConcurrentLinkedQueue<String> consoleBuffer;
	
	public CpuConsoleGetString(Cpu cpu) {
		super(cpu);
		this.consoleBuffer = new ConcurrentLinkedQueue<>();
	}
	
	public long getRoutineAddress() {
		return ROUTINE_ADDRESS;
	}
	
	public static boolean shouldBeManuallyAdded() {
		return true;
	}
	
	public boolean add(String line) {
		return this.consoleBuffer.offer(line);
	}
	
	@Override
	protected void primitive()
	{
		while (this.consoleBuffer.peek() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		String searchString = this.consoleBuffer.poll();
		
		if (searchString == null) {
			return;
		}
		
		long address = (long) this.getRegister(0).getValue();
		
		byte[] c = searchString.getBytes();
		
		for(int i = 0; i < c.length ; i++)
		{
			this.getRam().setByte(address,c[i]);
			address++;
		}
	}
}
