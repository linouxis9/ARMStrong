/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.boilerplate.InvalidInstructionException;
import projetarm_v2.simulator.core.routines.CpuConsoleGetString;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;

public class Interpreter {

	private AnchorPane mainPane;
	private DockNode dockNode;
	private Image dockImage;
	private TextFlow textFlow;
	private TextField textField;
	
	OutputStream output;
	
	private long pc;
	private int startingAddress;
	
	private ArmSimulator simulator;
	
	public Interpreter(ArmSimulator simulator) {
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/resources/ConsoleView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.simulator = simulator;
		
		this.dockNode = new DockNode(mainPane, "Interpreter", new ImageView(dockImage));

		this.mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.dockNode.getStylesheets().add("/resources/style.css");
		ScrollPane scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");

		this.textField = new TextField();
		this.textField.setId("consoleInput");
		this.mainPane.getChildren().add(this.textField);
		AnchorPane.setBottomAnchor(this.textField, (double)0);
		AnchorPane.setLeftAnchor(this.textField, (double)23);
		AnchorPane.setRightAnchor(this.textField, (double)0);


		this.textFlow = new TextFlow();
		this.textFlow.setPadding(new Insets(5));
		this.textFlow.setId("textConsole");

		scrollPane.setContent(this.textFlow);

		output = new OutputStream() {
			private StringBuffer currentLine = new StringBuffer();
			@Override
			public void write(int b) throws IOException {
				this.currentLine.append((char)b);
				if (b == '\n') {
					Platform.runLater(() -> {
						textFlow.getChildren().add(new Text(currentLine.toString()));
						this.currentLine.setLength(0);
						scrollPane.setVvalue(scrollPane.getHmax());
					});
				}
			}
		};
	}
	
	public void initialize() {
		this.redirectToInterpreter();
		this.pc = this.simulator.getCpu().getCurrentAddress();
		this.startingAddress = this.simulator.getStartingAddress();
		this.simulator.setStartingAddress(0);
		System.out.println("Welcome to the ARMStrong Interpreter!\n .reset To reset the interpreter\n Close the interpreter to get back to the usual simulation mode.\n [WARNING] You cannot use directives in Interpreter mode");
	}
	
	public void redirectToInterpreter() {	
		System.setOut(new PrintStream(output));
	}
	
	public void stopInterpreter() {
		this.simulator.getCpu().setCurrentAddress(this.pc);
		this.simulator.setStartingAddress(this.startingAddress);
	}
	
	public DockNode getNode() {
		return dockNode;
	}
	
	public TextField getTextField() {
		return (this.textField);
	}

	public void run() {
		String instruction = this.getTextField().getText();

		if (simulator.isWaitingForInput()) {
			simulator.setConsoleInput(instruction);
			System.out.println("[INFO] Input [" + instruction + "] added to Input queue");
			this.getTextField().clear();
			return;
		}
		
		if (instruction.contentEquals(".reset")) {
			simulator.resetState();
			System.out.println("The CPU has been reset.");
			this.redirectToInterpreter();
			return;
		}

		try {
			simulator.setProgram(instruction);
			System.out.println("[EXEC] " + instruction + " [" + Integer.toHexString(this.simulator.getRamWord(0)) + "]");
			this.simulator.getCpu().setCurrentAddress(0);
			new Thread(() -> simulator.runStep()).start();;
			this.getTextField().clear();
		} catch (InvalidInstructionException e) {
			System.out.println(e.getMessage());
		}
	}
}
