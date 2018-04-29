import java.util.Scanner;

import simulator.*;

public class Main {
	public static void main(String[] args) {
			ArmSimulator jpp = new ArmSimulator();

			Cpu cpuTest = new Cpu();
			Scanner in = new Scanner(System.in);
			
			String test = "";
			while (in.hasNext()) {
				test = test + in.nextLine() + System.lineSeparator();
			}
			
			in.close();
			
			System.out.println(test);
			
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
