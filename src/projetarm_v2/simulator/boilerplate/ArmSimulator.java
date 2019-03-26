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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import projetarm_v2.simulator.core.Assembler;
import projetarm_v2.simulator.core.Cpu;
import projetarm_v2.simulator.core.InvalidAssemblyException;
import projetarm_v2.simulator.core.Preprocessor;
import projetarm_v2.simulator.core.Ram;
import projetarm_v2.simulator.core.io.IO8Segment;
import projetarm_v2.simulator.core.io.IOButton;
import projetarm_v2.simulator.core.io.IOComponent;
import projetarm_v2.simulator.core.io.IOLed;
import projetarm_v2.simulator.core.io.IOSegment;
import projetarm_v2.simulator.core.io.IOSwitch;
import projetarm_v2.simulator.core.io.IOx;
import projetarm_v2.simulator.core.io.PORTManager;
import projetarm_v2.simulator.core.routines.CpuConsoleClear;
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
		}
	}

	private final Assembler assembler;
	/**
	 * The cpu to execute the program
	 */
	private Cpu cpu;

	private Ram ram;

	private PORTManager portManager;
	
	private BiMap<Integer, Integer> asmToLine;

	private static final Pattern labelPattern = Pattern.compile("([a-zA-Z]+:)");

	private int startingAddress = Cpu.DEFAULT_STARTING_ADDRESS;
	
	private int ramSize = Ram.DEFAULT_RAM_SIZE;
	
	private CpuConsoleGetString guiConsoleToCpu;

	private ConsoleView consoleView;
	
	private Save save;
	
	private Random random;
	private CpuConsoleClear guiClear;
	
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
		
		this.resetState();
	}

	public void loadSaveFromFile(String path) throws IOException {
		this.save = Save.fromPath(path);
		
		this.portManager.generateIOComponents(this.save.getComponentsAndReset());
		
		for (IOComponent component : this.portManager.getComponents()) {
			this.save.addComponent(component);
		}
		
		this.setProgram(this.save.getProgram());
	}
	
	public String getProgramFromSave() {
		return this.save.getProgram();
	}
	
	public void saveToFile(String path) throws IOException {
		this.save.saveToFile(path);
	}
	
	public int getAddressFromLine(int line) {
		return this.asmToLine.inverse().getOrDefault(line,0);
	}
	
	public void setProgram(String assembly) {
		this.save.setProgram(assembly);
		assembly = Preprocessor.pass1(assembly);
		try {
			fillRamWithAssembly(assembly);
		} catch (InvalidAssemblyException e) {/* This is going to get caught by fillAddressLineMap */}

		fillAddressLineMap(assembly);
	}

	public void setConsoleInput(String input){
		this.guiConsoleToCpu.add(input);
	}

	public void setConsoleView(ConsoleView consoleView){
		this.consoleView = consoleView;
		this.guiClear.setConsoleView(consoleView);
	}
	
	private String fillRamWithAssembly(String assembly) {
		this.asmToLine.clear();

		byte[] binary = (this.assembler.assemble(assembly, startingAddress));

		for (int i = 0; i < binary.length; i++) {
			this.ram.setByte((long)startingAddress + i, binary[i]);
		}
		
		this.cpu.setEndAddress((long)startingAddress + binary.length);
		
		return assembly;
	}

	private void fillAddressLineMap(String assembly) {
		
		int currentLine = 1;
		int currentAddress = (int)this.cpu.getStartingAddress();

		Matcher matcher = labelPattern.matcher(assembly);

		StringBuilder labelsBuilder = new StringBuilder();
		while (matcher.find()) {
			labelsBuilder.append(matcher.group());
		}
		String labels = labelsBuilder.toString();
		
		for (String line : assembly.split(";")) {
			if (line != "") {
				byte[] lineBytes;
				try {
					lineBytes = (this.assembler.assemble(labels + line.substring(Math.abs(line.indexOf(':') + 1)),
						currentAddress));
				} catch (InvalidAssemblyException e) {
					throw new InvalidInstructionException("[ERROR] " + e.getMessage() + " @ Line " + currentLine, currentLine);
				}
				asmToLine.put(currentAddress, currentLine);
				currentAddress += lineBytes.length - (line.contains("=") ? 1 : 0) * 4;
				currentLine += 1;
			}
		}
	}

	public void setRandomPattern() {
		this.ram.setRandomPattern((byte)random.nextInt());
	}
	public void setRandomPattern(byte value) { this.ram.setRandomPattern(value); }
	
	public byte getRandomPattern() {
		return this.ram.getRandomPattern();
	}
	
	/**
	 * Returns the register value corresponding to the given number
	 */
	public int getRegisterValue(int registerNumber) {
		return this.cpu.getRegister(registerNumber).getValue();
	}
	
	public void setRegisterValue(int registerNumber, int myValue) {
		this.cpu.getRegister(registerNumber).setValue(myValue);
	}

	public Ram getRam() {
		return this.ram;
	}

	public Cpu getCpu() {
		return this.cpu;
	}

	/**
	 * Returns a byte(8bits) from the ram corresponding to the given address
	 */
	public byte getRamByte(long address) {
		return this.ram.getByte(address);
	}

	/**
	 * Returns a half-word(16bits) from the ram corresponding to the given address
	 */
	public short getRamHWord(long address) {
		return this.ram.getHWord(address);
	}

	/**
	 * Returns a word(32bits) from the ram corresponding to the given address
	 */
	public int getRamWord(long address) {
		return this.ram.getValue(address);
	}

	/**
	 * Sets a byte(8bits) in the ram at the given address
	 */
	public void setRamByte(long address, byte value) {
		this.ram.setByte(address, value);
	}

	/**
	 * Sets a half-word(16bits) in the ram at the given address
	 */
	public void setRamHWord(long address, short value) {
		this.ram.setHWord(address, value);
	}

	/**
	 * Sets a word(32bits) in the ram at the given address
	 */
	public void setRamWord(long address, int value) {
		this.ram.setValue(address, value);
	}

	/**
	 * Starting the processor to the next break or to the end
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
	 * Staring the processor to execute a single instruction
	 */
	public void runStep() {
		try {
			this.cpu.runStep();
		} catch (UnicornException e) {
			this.handleException(e);
		}
	}

	// TODO Convert to an exception so it can be handled as wished by the UIs
	private void handleException(UnicornException e) {
		System.out.format("[ERROR] %s @ Instruction [Address=0x%x, Line=%d]%n[ERROR] You might have an invalid instruction in RAM at the current address.%n[ERROR] EMULATION ABORTED!%n", e.getMessage(),
				this.getRegisterValue(15), this.getCurrentLine());
	}

	public int getCurrentLine() {
		return this.asmToLine.getOrDefault((int)this.cpu.getCurrentAddress(), 0);
	}

	/**
	 * Resets the execution (clears the current execution point)
	 */
	public void resetRun() {
		this.cpu.getRegister(15).setValue((int)this.cpu.getStartingAddress());
	}

	public void resetState() {
		this.ram.clear();
		this.cpu = new Cpu(ram, this.startingAddress, this.ramSize);
		this.guiConsoleToCpu = new CpuConsoleGetString(cpu);
		this.cpu.registerCpuRoutine(guiConsoleToCpu);
		this.guiClear = new CpuConsoleClear(cpu);
		this.guiClear.setConsoleView(consoleView);
		this.cpu.registerCpuRoutine(guiClear);
		this.portManager = new PORTManager(this.ram);
	}

	public int getStartingAddress() {
		return this.startingAddress;
	}
	
	public void setStartingAddress(int startingAddress) {
		this.cpu.setStartingAddress(startingAddress);
		this.startingAddress = startingAddress;
	}
	
	public int getRamSize() {
		return this.ramSize;
	}
	
	/**
	 * Returns the Negative Flag status
	 */
	public boolean getN() {
		return this.cpu.getCPSR().n();
	}

	/**
	 * Returns the Zero Flag status
	 */
	public boolean getZ() {
		return this.cpu.getCPSR().z();
	}

	/**
	 * Returns the Carry Flag status
	 */
	public boolean getC() {
		return this.cpu.getCPSR().c();
	}

	/**
	 * Returns the oVerflow Flag status
	 */
	public boolean getV() {
		return this.cpu.getCPSR().v();
	}

	public boolean getQ() {
		return this.cpu.getCPSR().q();
	}
	
	
	public void setN(boolean n) {
		this.cpu.getCPSR().setN(n);
	}

	public void setZ(boolean z) {
		this.cpu.getCPSR().setZ(z);
	}

	public void setC(boolean c) {
		this.cpu.getCPSR().setC(c);
	}
	
	public void setV(boolean v) {
		this.cpu.getCPSR().setV(v);
	}

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
		for (int i = 0; i < 7; i++) {
			IOSegment segment = segments.getSegment(i);
			this.save.removeComponent(segment);
			this.portManager.removeIOComponent(segment);
		}
	}
	
	public boolean isWaitingForInput() {
		return this.guiConsoleToCpu.isWaitingForInput();
	}
	
	/**
	 * Returns true if the cpu is halted
	 */
	public boolean isRunning() {
		return this.cpu.isRunning() && !this.cpu.hasFinished();
	}

	public boolean hasFinished() {
		return this.cpu.hasFinished();
	}

	/**
	 * Stops the execution
	 */
	public void interruptExecutionFlow() {
		this.cpu.interruptMe();
	}
}
