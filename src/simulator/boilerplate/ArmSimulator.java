/**
 * Copyright (c) 2018 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package simulator.boilerplate;

import java.util.HashMap;
import java.util.Map;

import simulator.core.*;
import simulator.core.exceptions.InvalidDirectiveException;
import simulator.core.exceptions.InvalidLabelException;
import simulator.core.exceptions.InvalidOperationException;
import simulator.core.exceptions.InvalidRegisterException;
import simulator.core.exceptions.InvalidSyntaxException;
import simulator.core.exceptions.UnknownLabelException;

/**
 * ArmSimulator is class responsible for handling the creation of the main ARM Simulator classes.
 */
public class ArmSimulator {
    /**
     * The current loaded program
     */
	private final Program program;

    /**
     * The interpretor to parse the program
     */
	private final Interpretor interpretor;

    /**
     * The cpu to execute the prorgam
     */
	private final Cpu cpu;

    /**
     * A map to make the link between the instruction and the line number
     */
	private final Map<Instruction, Integer> linesMap;

    /**
     * Creates a arm simulator ready to use, with all the needed components (cpu, program, linesMap, interpretor)
     */
	public ArmSimulator() {	
		this.cpu = new Cpu();
		this.program = new Program();
		this.linesMap = new HashMap<>();
		this.interpretor = new Interpretor(this.cpu, this.program, this.linesMap);
	}

    /**
     * Returns the register value corresponding to the given number
     */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegister(registerNumber).getValue();
	}

    /**
     * Returns a byte(8bits) from the ram corresponding to the given address
     */
	public byte getRamByte(int address) {
		return this.cpu.getRam().getByte(new Address(address));
	}

    /**
     * Returns a half word(16bits) from the ram corresponding to the given address
     */
	public short getRamHWord(int address) {
		return this.cpu.getRam().getHWord(new Address(address));
	}

    /**
     * Returns a word(32bits) from the ram corresponding to the given address
     */
	public int getRamWord(int address) {
		return this.cpu.getRam().getValue(new Address(address));
	}

    /**
     * Starting the processor to the next break or to the end
     */
	public boolean run() {
		this.cpu.execute();
		return this.isHalted();
	}

    /**
     * Staring the processor to execute a single instruction
     */
	public void runStep(){
		this.cpu.executeStep();
	}

    /**
     * Resets the execution (clears the current execution point)
     */
	public void resetRun(){
		this.linesMap.clear();
		this.cpu.reset();
	}

    /**
     * Returns the next line to be executed
     */
	public int getCurrentLine(){
		try {
			return linesMap.get(this.cpu.getInstructions().get(((int) Math.ceil((double) this.cpu.getPc().getValue() / 4))))-1;
		}
		catch (IndexOutOfBoundsException e) {
			return 0;
		}
	}

    /**
     * Returns the Negative Flag status
     */
	public boolean getN() {
		return this.cpu.getCPSR().isN();
	}

    /**
     * Returns the Zero Flag status
     */
	public boolean getZ() {
		return this.cpu.getCPSR().isZ();
	}

    /**
     * Returns the Carry Flag status
     */
	public boolean getC() {
		return this.cpu.getCPSR().isC();
	}

    /**
     * Returns the oVerflow Flag status
     */
	public boolean getV() {
		return this.cpu.getCPSR().isV();
	}

    /**
     * Refresh the Program by a new program given in parameter
     * @param programAsString the sting containing the program typed by the user
     * @throws InvalidSyntaxException
     * @throws InvalidOperationException
     * @throws InvalidRegisterException
     * @throws InvalidLabelException
     * @throws UnknownLabelException
     * @throws InvalidDirectiveException
     */
	public void setProgramString(String programAsString) throws InvalidSyntaxException, InvalidOperationException, InvalidRegisterException, InvalidLabelException, UnknownLabelException, InvalidDirectiveException{
		this.resetRun();
		this.program.setNewProgram(programAsString);
		this.interpretor.parseProgram();
	}

	/**
     * Returns true if the program is totally executed
	 */
	public boolean hasFinished() {
		return this.cpu.hasFinished();
	}

    /**
     * Returns true if the cpu have been interrupted
     */
	public boolean isInterrupted() {
		return this.cpu.isInterrupted();
	}

    /**
     * Returns true if the cpu have been stopped by a breakpoint
     */
	public boolean isBreakpoint() {
		return this.cpu.isBreakpoint();
	}

    /**
     * Returns true if the cpu is halted
     */
	public boolean isHalted() {
		return this.hasFinished() || this.isBreakpoint() || this.isInterrupted();
	}

	/**
     * Stops the execution
     */
	public void interruptExecutionFlow(boolean interrupt) {
		this.cpu.interruptMe(interrupt);
	}
}
