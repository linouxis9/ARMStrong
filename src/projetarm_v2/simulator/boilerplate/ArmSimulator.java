/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.boilerplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import projetarm_v2.simulator.core.Assembler;
import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.InvalidAssemblyException;
import projetarm_v2.simulator.core.Preprocessor;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.RamChunk;
import projetarm_v2.simulator.core.io.IO8Segment;
import projetarm_v2.simulator.core.io.IOButton;
import projetarm_v2.simulator.core.io.IOComponent;
import projetarm_v2.simulator.core.io.IOLed;
import projetarm_v2.simulator.core.io.IOSegment;
import projetarm_v2.simulator.core.io.IOSwitch;
import projetarm_v2.simulator.core.io.IOx;
import projetarm_v2.simulator.core.io.PORTManager;
import projetarm_v2.simulator.core.routines.CpuConsoleClear;
import projetarm_v2.simulator.core.routines.CpuConsoleGetChar;
import projetarm_v2.simulator.core.routines.CpuConsoleGetString;
import projetarm_v2.simulator.core.save.Save;
import projetarm_v2.simulator.ui.javafx.ConsoleView;
import projetarm_v2.simulator.utils.NativeJarGetter;
import unicorn.UnicornException;

/**
 * ArmSimulator is class responsible for handling the creation of the main ARM
 * Simulator classes.
 */
public class ArmSimulator {
	
	static {
		try {
			NativeJarGetter.getInstance().loadLibraryFromJar("libunicorn_java");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

    /**
     * The Assembler used to assemble assembly
     */
	private final Assembler assembler;
	
	/**
	 * The CPU used to execute the content in RAM
	 */
	private Cpu cpu;

    /**
     * The Ram holding the content of the micro-processor (The data and the program to be executed)
     */
	private Ram ram;

    /**
     * The PORTManager responsible of creating, removing and managing I/O components such as LED, EightSegmentsDisplay...
     */
	private PORTManager portManager;
	
    /**
     * Ram Address <=> Editor Line map correspondence
     */
	private BiMap<Integer, Integer> asmToLine;

    /**
     * The CPU starts executing binary in Ram at this address
     */
	private int startingAddress = Cpu.DEFAULT_STARTING_ADDRESS;
	
    /**
     * The Ram's size in bytes
     */
	private int ramSize = Ram.DEFAULT_RAM_SIZE;

    /**
     * Save every data related to the current simulation (assembly, I/O components)
     */
	private Save save;

	private ConsoleView consoleView;
	
	private ConcurrentLinkedQueue<Character> consoleBuffer;
	
	private AtomicBoolean waitingForInput;
	
	private CpuConsoleClear guiConsole;
	
	private Random random;
	
	/**
	 * Creates a arm simulator ready to use, with all the needed components (cpu,
	 * program, asmToLine, assembler)
	 */
	public ArmSimulator() {
		this.save = new Save();
		
		this.ram = new Ram();
		
		this.assembler = Assembler.getInstance();
		this.asmToLine = HashBiMap.create();
		this.random = new Random();
		
		this.consoleBuffer = new ConcurrentLinkedQueue<>();
		this.waitingForInput = new AtomicBoolean(false);
		this.portManager = new PORTManager(this.ram);
		
		this.resetState();
	}

	/**
	 * Load Save's content into the ArmSimulator instance.
	 * - Load Save's assembly into Ram
	 * - Recreate the I/O components
	 * @param path Relative or absolute path of the .ARMS save file
	 * @throws IOException
	 */
	public void loadSaveFromFile(String path) throws IOException {
		this.portManager = new PORTManager(this.ram);
		
		this.save = Save.fromPath(path);
		
		this.portManager.generateIOComponents(this.save.getComponentsAndReset());
		
		for (IOComponent component : this.portManager.getComponents()) {
			this.save.addComponent(component);
		}
		
		this.setProgram(this.save.getProgram());
	}
	
	/**
	 * Get saved assembly
	 * @return Saved assembly as String
	 */
	public String getProgramFromSave() {
		return this.save.getProgram();
	}
	
	/**
	 * Save Save's content on disk.
	 * @param path Relative or absolute path of the .ARMS save file
	 * @throws IOException 
	 */
	public void saveToFile(String path) throws IOException {
		this.save.saveToFile(path);
	}
	
	
	/**
	 * Give where the line of assembly is located in the Ram
	 * @param line Line of the assembly's instruction
	 * @return where the line of assembly is located in the Ram or 0 if unavailable
	 */
	public int getAddressFromLine(int line) {
		return this.asmToLine.inverse().getOrDefault(line,0);
	}
	
	
	/**
	 * Assemble and load the given assembly in Ram
	 * @param assembly The assembly to load
	 */
	public void setProgram(String assembly) {
		this.save.setProgram(assembly);
		assembly = Preprocessor.pass1(assembly);
		try {
			fillRamWithAssembly(assembly);
		} catch (InvalidAssemblyException e) {/* This is going to get caught by fillAddressLineMap */}

		fillAddressLineMap(assembly);
	}

	/**
	 * Add a String to the GetChain routine buffer
	 * @param input Add input to the CpuRoutine buffer
	 */
	public void setConsoleInput(String input){
		for (Character ch : input.toCharArray()) {
			this.consoleBuffer.add(ch);
		}
	}

	public void setConsoleView(ConsoleView consoleView){
		this.consoleView = consoleView;
		this.guiConsole.setConsoleView(consoleView);
	}
	
	/**
	 * Assemble and load the given Assembly into Ram
	 * @param assembly The assembly to load into Ram
	 */
	private void fillRamWithAssembly(String assembly) {
		this.asmToLine.clear();

		byte[] binary = (this.assembler.assemble(assembly, startingAddress));

		for (int i = 0; i < binary.length; i++) {
			this.ram.setByte((long)startingAddress + i, binary[i]);
		}
		
		this.cpu.setEndAddress((long)startingAddress + binary.length);
	}

	/**
	 * Create the Ram Address <=> Editor Line map
	 * @param assembly The exact same assembly that was previously loaded in Ram
	 */
	private void fillAddressLineMap(String assembly) {
		
		int currentLine = 0;
		int currentAddress = (int)this.cpu.getStartingAddress();

		Matcher matcher = Preprocessor.labelPattern.matcher(assembly.replaceAll(";", "\n"));

		StringBuilder labelsBuilder = new StringBuilder();
		while (matcher.find()) {
			labelsBuilder.append(matcher.group());
		}
		String labels = labelsBuilder.toString();

		for (String line : assembly.split(";")) {
			currentLine++;

			matcher = Preprocessor.emptyLabelPattern.matcher(line);
			if (matcher.find()) {
				continue;
			}
			line = line.replaceAll(Preprocessor.LABEL_PATTERN, "");
			if (!line.trim().isEmpty()) {
				byte[] lineBytes;
				try {
					lineBytes = (this.assembler.assemble(labels + line, currentAddress));
				} catch (InvalidAssemblyException e) {
					throw new InvalidInstructionException("[ERROR] Line " + currentLine + " \"" + line.trim() + "\": " + e.getMessage(), currentLine);
				}
				if (lineBytes.length == 1 && lineBytes[0] == 0 || line.trim().equals(".word 0")) {
					continue;
				}
				asmToLine.put(currentAddress, currentLine);
				currentAddress += lineBytes.length - (line.contains("=") ? 1 : 0) * 4;
			}
		}
	}

	/**
	 * Set the byte pattern shown in the Ram when the Ram is not initialized
	 * @param value The byte pattern
	 */
	public void setRandomPattern(byte value) { /*this.ram.setRandomPattern(value); */}
	
	/**
	 * Set the byte pattern shown in the Ram when the Ram is not initialized to something random
	 */
	public void setRandomPattern() {
		RamChunk ramChunk = new RamChunk(0, Ram.CHUNK_SIZE);
		
		for (int i = 0; i < Ram.CHUNK_SIZE; i++) {
			ramChunk.setByte(i, (byte)random.nextInt());
		}
		this.ram.setRandomPattern(ramChunk);
	}
	
	/**
	 * Set the byte pattern shown in the Ram when the Ram is not initialized to 0
	 */
	public void removeRandomPattern() {
		RamChunk ramChunk = new RamChunk(0, Ram.CHUNK_SIZE);
		
		this.ram.setRandomPattern(ramChunk);
	}
	
	
	/**
	 * Get the byte pattern shown in the Ram when the Ram is not initialized
	 * @return The byte pattern
	 */
	public RamChunk getRandomPattern() {
		return this.ram.getRandomPattern();
	}
	
	
	/**
	 * Returns the register value corresponding to the given number
	 * @param registerNumber The register number (0 <= registerNumber <= 15)
	 * @return The register value
	 */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegister(registerNumber).getValue();
	}
	/**
	 * Set the register value to the given number
	 * @param registerNumber The register number (0 <= egisterNumber <= 15)
	 * @param myValue The value to set into the register
	 */
	public void setRegisterValue(int registerNumber, int myValue) {
		this.cpu.getRegister(registerNumber).setValue(myValue);
	}

	/**
	 * @return The Simulator's Ram
	 */
	public Ram getRam() {
		return this.ram;
	}

	/**
	 * @return The Simulator's Cpu
	 */
	public Cpu getCpu() {
		return this.cpu;
	}

	/**
	 * Returns a byte (8 bits) from the ram corresponding to the given address
	 */
	public byte getRamByte(long address) {
		return this.ram.getByte(address);
	}

	/**
	 * Returns a half-word (16 bits) from the ram corresponding to the given address
	 */
	public short getRamHWord(long address) {
		return this.ram.getHWord(address);
	}

	/**
	 * Returns a word (32 bits) from the ram corresponding to the given address
	 */
	public int getRamWord(long address) {
		return this.ram.getValue(address);
	}

	/**
	 * Sets a byte (8 bits) in the ram at the given address
	 */
	public void setRamByte(long address, byte value) {
		this.ram.setByte(address, value);
	}

	/**
	 * Sets a half-word (16 bits) in the ram at the given address
	 */
	public void setRamHWord(long address, short value) {
		this.ram.setHWord(address, value);
	}

	/**
	 * Sets a word (32 bits) in the ram at the given address
	 */
	public void setRamWord(long address, int value) {
		this.ram.setValue(address, value);
	}

	/**
	 * Execute instructions in the Ram at the currentAddress until the Cpu hits an empty word, a breakpoint, a stop or that an exception occurs.
	 */
	public void run() {
		long address = this.cpu.getCurrentAddress();
		try {
			this.cpu.runAllAtOnce();
		} catch (UnicornException e) {
			this.handleException(e);
		}
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		if (address == this.cpu.getCurrentAddress() && this.cpu.hasFinished()) {
			System.out.println("[INFO] Nothing happened, maybe you forgot to reload?");
		}
		
	}

	/**
	 * Execute a single instruction at the currentAddress
	 */
	public void runStep() {
		try {
			this.cpu.runStep();
		} catch (UnicornException e) {
			this.handleException(e);
		}
	}

	private void handleException(UnicornException e) {
		System.out.format("[ERROR] %s @ Instruction [Address=0x%x, Line=%d]%n[ERROR] You might have an invalid instruction in RAM at the current address.%n[ERROR] EMULATION ABORTED!%n", e.getMessage(),
				this.getRegisterValue(15), this.getCurrentLine());
	}

	/**
	 * Get the currentLine of the instruction in assembly that is the currentAddress in Ram
	 */
	public int getCurrentLine() {
		return this.asmToLine.getOrDefault((int)this.cpu.getCurrentAddress(), 0);
	}

	/**
	 * Resets the execution (clears the current execution point)
	 */
	public void resetRun() {
		this.cpu.getRegister(15).setValue((int)this.cpu.getStartingAddress());
	}

	/**
	 * Resets the processor state
	 */
	public void resetState() {
		this.ram.clear();
		this.cpu = new Cpu(ram, this.startingAddress, this.ramSize);
		this.cpu.registerCpuRoutine(new CpuConsoleGetString(cpu, consoleBuffer, waitingForInput));
		this.cpu.registerCpuRoutine(new CpuConsoleGetChar(cpu, consoleBuffer, waitingForInput));
		this.guiConsole = new CpuConsoleClear(cpu);
		this.guiConsole.setConsoleView(consoleView);
		this.cpu.registerCpuRoutine(guiConsole);
	}

	/**
	 * @return Gets the address where the processor will begin or began executing assembly
	 */
	public int getStartingAddress() {
		return this.startingAddress;
	}
	
	/**
	 * @param startingAddress Change the adress where the processor will begin executing assembly at this address
	 */
	public void setStartingAddress(int startingAddress) {
		this.cpu.setStartingAddress(startingAddress);
		this.startingAddress = startingAddress;
	}
	
	/**
	 * @return The ramSize in bytes
	 */
	public int getRamSize() {
		return this.ramSize;
	}
	
	/**
	 * @return the Negative Flag status
	 */
	public boolean getN() {
		return this.cpu.getCPSR().n();
	}

	/**
	 * @return the Zero Flag status
	 */
	public boolean getZ() {
		return this.cpu.getCPSR().z();
	}

	/**
	 * @return the Carry Flag status
	 */
	public boolean getC() {
		return this.cpu.getCPSR().c();
	}

	/**
	 * @return the oVerflow Flag status
	 */
	public boolean getV() {
		return this.cpu.getCPSR().v();
	}

	/**
	 * @return the Saturation Flag status
	 */
	public boolean getQ() {
		return this.cpu.getCPSR().q();
	}
	
	
	/**
	 * @param n Set the N flag to this boolean
	 */
	public void setN(boolean n) {
		this.cpu.getCPSR().setN(n);
	}

	/**
	 * @param z Set the Z flag to this boolean
	 */
	public void setZ(boolean z) {
		this.cpu.getCPSR().setZ(z);
	}

	/**
	 * @param c Set the C flag to this boolean
	 */
	public void setC(boolean c) {
		this.cpu.getCPSR().setC(c);
	}
	
	/**
	 * @param v Set the V flag to this boolean
	 */
	public void setV(boolean v) {
		this.cpu.getCPSR().setV(v);
	}
	
	/**
	 * @return Get the PORTManager of this ArmSimulator's instance
	 */
	public PORTManager getPortManager() {
		return this.portManager;
	}
	
	public IOLed newIOLed() {
		IOLed component = this.portManager.newIOLed();
		
		this.save.addComponent(component);
		
		return component;
	}

	public IOButton newIOButton() {
		IOButton component = this.portManager.newIOButton();
		
		this.save.addComponent(component);
		
		return component;
	}
	
	public IOSwitch newIOSwitch() {
		IOSwitch component = this.portManager.newIOSwitch();
		
		this.save.addComponent(component);
		
		return component;
	}
	
	public IO8Segment newIO8Segment() {
		IO8Segment segments = this.portManager.newIO8Segment();
		
		for (int i = 0; i < IOx.NUMBER_OF_SEGMENTS; i++) {
			this.save.addComponent(segments.getSegment(i));
		}
		
		return segments;
	}
	
	public void removeIOComponent(IOComponent component) {
		this.portManager.removeIOComponent(component);
		this.save.removeComponent(component);
	}
	
	public void removeIOComponent(IO8Segment segments) {
		for (int i = 0; i < IOx.NUMBER_OF_SEGMENTS; i++) {
			IOSegment segment = segments.getSegment(i);
			this.save.removeComponent(segment);
			this.portManager.removeIOComponent(segment);
		}
	}
	
	public boolean isWaitingForInput() {
		return this.waitingForInput.get();
	}
	
	/**
	 * @return true if the cpu is running, false otherwise
	 */
	public boolean isRunning() {
		return this.cpu.isRunning() && !this.cpu.hasFinished();
	}

	/**
	 * @return true if the cpu has hitten an empty word, false otherwise
	 */
	public boolean hasFinished() {
		return this.cpu.hasFinished();
	}

	/**
	 * Interrupt the flow of execution of the Cpu
	 */
	public void interruptExecutionFlow() {
		this.cpu.interruptMe();
	}
}
