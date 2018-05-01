import java.io.PrintStream;
import java.util.Scanner;

import simulator.*;

public class Main {
	public static void main(String[] args) {
			//ArmSimulator jpp = new ArmSimulator();

			Cpu cpuTest = new Cpu();
			Scanner in = new Scanner(System.in);
			
			String test = "";
			while (in.hasNext()) {
				test = test + in.nextLine() + System.lineSeparator();
			}
			
			in.close();

			GUI myIHM = new GUI();
			
			new Thread() {
				public void run() {
					myIHM.startIHM();
				}
			}.start();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Interpretor interpretorTest = new Interpretor(cpuTest,new Program(test));
			try {
				interpretorTest.parseProgram();
				cpuTest.execute();
			} catch (AssemblyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
