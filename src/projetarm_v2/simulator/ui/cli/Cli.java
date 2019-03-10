/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.cli;


import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;

import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.boilerplate.InvalidInstructionException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class Cli {

	private Label[] registers;
	private ArmSimulator simulator;
	private MultiWindowTextGUI gui;
	private Window window;
	private LinkedHashMap<Label, Label> memory;
	private TextBox codeEditor;
	private TextBox console;
	private int memoryIndex;
	private AtomicBoolean running;

	public static void main(String[] args) {
		new Cli().startUI();
	}

	public Cli() {
		this.registers = new Label[17];
		this.simulator = new ArmSimulator();
		this.memory = new LinkedHashMap<>();
		this.memoryIndex = 0x1000;
		this.running = new AtomicBoolean(false);

		try {
			Terminal terminal = new DefaultTerminalFactory().createTerminal();
			Screen screen = new TerminalScreen(terminal);
			screen.startScreen();

			TerminalSize size = screen.getTerminalSize();

			gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

			Panel masterPanel = new Panel();
			masterPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

			Panel menuPanel = new Panel();
			menuPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
			masterPanel.addComponent(menuPanel);

			menuPanel.addComponent(new Button("Open", () -> {
				try {
					String path = new FileDialogBuilder().setTitle("Open File").setDescription("Choose a file")
							.setActionLabel("Close").setActionLabel("Open").build().showDialog(gui).getAbsolutePath();
					this.codeEditor.setText(new String(Files.readAllBytes(Paths.get(path)), "UTF-8"));
				} catch (Exception e) {
				}
			}));

			menuPanel.addComponent(new Button("Save", () -> {
				try {
					String path = new FileDialogBuilder().setTitle("Save File").setDescription("Choose a file")
							.setActionLabel("Close").setActionLabel("Save").build().showDialog(gui).getAbsolutePath();
					try (PrintWriter out = new PrintWriter(path)) {
						out.println(this.codeEditor.getText());
					}
				} catch (Exception e) {
				}
			}));

			menuPanel.addComponent(new Label("|"));

			menuPanel.addComponent(new Button("L0ad", () -> {
				try {
					this.codeEditor.setText(this.codeEditor.getText().replaceAll("-> ", ""));
					try {
						this.simulator.setProgram(this.codeEditor.getText().replaceAll("\n", ";"));
					} catch (InvalidInstructionException e) {
						System.out.println(e.getMessage());
						this.showCurrentLine(e.getLine());
						return;
					}
					readMode();
					this.showCurrentLine();
					System.out.println("---");
				} catch (RuntimeException e) {
					System.out.println(e);
					return;
				}
			}));

			menuPanel.addComponent(new Button("Run", () -> {
				if (!this.running.get() && this.codeEditor.isReadOnly()) {
					new Thread(() -> {
						this.running.set(true);
						this.simulator.run();
						this.updateGUI();
						this.running.set(false);
					}).start();
				}
			}));

			menuPanel.addComponent(new Button("Run Step", () -> {
				if (!this.simulator.hasFinished() && !this.running.get() && this.codeEditor.isReadOnly()) {
					this.running.set(true);
					this.simulator.runStep();
					this.showCurrentLine();
					this.updateGUI();
					this.running.set(false);
				}
			}));

			menuPanel.addComponent(new Button("Stop", () -> {
				this.simulator.interruptExecutionFlow();
				writeMode();
				this.simulator.resetRun();
			}));

			menuPanel.addComponent(new Button("Reset", () -> {
				this.simulator.interruptExecutionFlow();
				writeMode();

				this.simulator.resetState();
				this.updateGUI();
			}));

			menuPanel.addComponent(new Label("|"));

			menuPanel.addComponent(new Button("Exit", () -> {
				System.exit(0);
			}));

			Panel bodyPanel = new Panel();
			bodyPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
			masterPanel.addComponent(bodyPanel);

			Panel leftPanel = new Panel(new GridLayout(2));
			bodyPanel.addComponent(leftPanel.withBorder(Borders.singleLine("Registers")));
			IntStream.range(0, 16).forEachOrdered(n -> {
				leftPanel.addComponent(new Label("r" + n));
				this.registers[n] = new Label("0x0");
				leftPanel.addComponent(this.registers[n]);
			});

			leftPanel.addComponent(new Label("Flags"));
			this.registers[16] = new Label("[___]");
			leftPanel.addComponent(this.registers[16]);

			Panel centerPanel = new Panel();
			bodyPanel.addComponent(centerPanel);
			codeEditor = new TextBox(new TerminalSize(size.getColumns() / 2, size.getColumns() / 8));
			centerPanel.addComponent(codeEditor.withBorder(Borders.singleLine("Editor")));
			codeEditor.setVerticalFocusSwitching(true);

			Panel bottomPanel = new Panel(new GridLayout(2));
			bottomPanel.addComponent(new Button("Lowr", () -> {
				this.memoryIndex--;
				if (this.memoryIndex < 0) {
					this.memoryIndex = 0;
				}
				this.updateMemory();
			}));
			bottomPanel.addComponent(new Button("Highr", () -> {
				this.memoryIndex++;
				this.updateMemory();
			}));
			bodyPanel.addComponent(bottomPanel.withBorder(Borders.singleLine("Memory")));
			IntStream.range(0, 16).forEachOrdered(n -> {
				Label key = new Label("0x" + n);
				Label value = new Label("0");
				bottomPanel.addComponent(key);
				bottomPanel.addComponent(value);
				memory.put(key, value);
			});

			this.updateMemory();
			this.console = new TextBox(new TerminalSize(size.getColumns() / 2, 4));
			this.console.setReadOnly(false);
			this.console.setText("Project #@RMStrong");
			console.setCaretWarp(true);
			centerPanel.addComponent(this.console.withBorder(Borders.singleLine("Console")));

			OutputStream consoleOut = new OutputStream() {
				private StringBuffer text = new StringBuffer();
				private int pos = 0;

				public void write(int b) {
					if (b == '\n') {
						pos++;
						console.addLine(text.toString());
						console.setCaretPosition(pos, 0);
						this.text.setLength(0);
						return;
					}
					text.append((char) b);

				}
			};

			new Thread(() -> {
				while (true) {
					while (this.running.get()) {
						this.updateGUI();
					}
				}
			}).start();

			System.setOut(new PrintStream(consoleOut, true));

			window = new BasicWindow();
			window.setComponent(masterPanel.withBorder(Borders.doubleLine("#@RMStrong")));
			window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readMode() {
		this.codeEditor.setReadOnly(true);
		this.codeEditor.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, TextColor.ANSI.GREEN));
	}

	private void writeMode() {
		this.codeEditor.setReadOnly(false);
		this.codeEditor.setTheme(new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLUE));
	}

	public void startUI() {
		gui.addWindowAndWait(window);
	}

	private void updateMemory() {
		int i = 0;
		for (Map.Entry<Label, Label> base : memory.entrySet()) {
			base.getKey().setText("0x" + Integer.toHexString(memoryIndex + i));
			base.getValue().setText("0x" + Integer.toHexString(((int)this.simulator.getRamByte((long) memoryIndex + i)) & 0xFF));
			i++;
		}
	}

	private void updateRegisters() {
		for (int i = 0; i < registers.length - 1; i++) {
			registers[i].setText("0x" + Integer.toHexString(this.simulator.getRegisterValue(i)));
		}
		registers[16].setText(this.simulator.getCpu().getCPSR().toString());
	}

	private void updateGUI() {
		this.updateMemory();
		this.updateRegisters();
	}

	private void showCurrentLine() {
		int line = this.simulator.getCurrentLine();
		showCurrentLine(line);
	}

	private void showCurrentLine(int line) {
		line -= 1;
		String code = this.codeEditor.getText().replaceAll("-> ", "");
		String[] codes = code.split(System.lineSeparator());
		if (line != -1) {
			codes[line] = "-> " + codes[line];
		}
		this.codeEditor.setText(String.join(System.lineSeparator(), codes));
	}

}