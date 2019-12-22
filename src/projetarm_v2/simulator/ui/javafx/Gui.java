/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package projetarm_v2.simulator.ui.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.boilerplate.InvalidInstructionException;
import projetarm_v2.simulator.core.routines.CpuConsoleClear;
import projetarm_v2.simulator.core.routines.CpuConsoleGetString;
import projetarm_v2.simulator.ui.javafx.ramview.RamView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The main gui class
 */
public class Gui extends Application {

	private Scene scene;

	private ArrayList<RegistersView> registersViews;
	private ArrayList<RamView> ramViews;
	private ArrayList<LedView> ledViews;
	private ArrayList<EightSegmentDisplay> eightSegmentDisplays;

	private CodeEditor codeEditor;
	private ConsoleView consoleView;

	private ArmMenuBar armMenuBar;
	private ArmToolBar armToolBar;

	private DockPane dockPane;

	private File currentProgramPath;
	private boolean executionMode;

	private ArmSimulator simulator;
	private AtomicBoolean running;

	private Stage stage;

	private Interpreter interpreter;
	
	private boolean isInterpreterMode;
	
	private AtomicBoolean interfaceBeingUpdated;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.simulator = new ArmSimulator();
		this.simulator.setRandomPattern();
		this.executionMode = false;
		this.running = new AtomicBoolean(false);
		this.interfaceBeingUpdated = new AtomicBoolean(false);

		this.stage = primaryStage;

		primaryStage.setTitle("#@RMStrong");
		Image applicationIcon = new Image(Gui.class.getResource("/resources/logo.png").toExternalForm());
		primaryStage.getIcons().add(applicationIcon);
		
		this.dockPane = new DockPane();

		// load an image to caption the dock nodes
		// Image dockImage = new
		// Image(Gui.class.getResource("docknode.png").toExternalForm());

		// creating the nodes
		this.registersViews = new ArrayList<>();
		this.ramViews = new ArrayList<>();
		this.ledViews = new ArrayList<>();
		this.eightSegmentDisplays = new ArrayList<>();
		
		this.codeEditor = new CodeEditor(simulator);
		this.codeEditor.getNode().dock(dockPane, DockPos.LEFT);

		this.registersViews.add(new RegistersView(this.simulator));
		this.registersViews.get(0).getNode().setMaxWidth(220);
		this.registersViews.get(0).getNode().dock(dockPane, DockPos.LEFT);

		this.ramViews.add(new RamView(simulator));
		this.ramViews.get(0).getNode().dock(dockPane, DockPos.RIGHT);


		this.consoleView = new ConsoleView();
		this.consoleView.getNode().dock(dockPane, DockPos.BOTTOM);

		this.consoleView.redirectToConsole();
		
		this.simulator.setConsoleView(this.consoleView);
		
		this.interpreter = new Interpreter(this.simulator);
		this.isInterpreterMode = false;
		
		this.armMenuBar = new ArmMenuBar(this.getHostServices());
		this.armToolBar = new ArmToolBar();

		VBox vbox = new VBox();
		vbox.getChildren().addAll(this.armMenuBar.getNode(), this.armToolBar.getNode(), dockPane);
		VBox.setVgrow(dockPane, Priority.ALWAYS);

		this.scene = new Scene(vbox, 1000, 1000);
		this.stage.setMaximized(true);
		this.stage.setResizable(true);

		primaryStage.setScene(this.scene);
		primaryStage.sizeToScene();

		setButtonEvents();
		setExecutionMode();

		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

		newUpdateThread(this.running);
		
		primaryStage.show();
		primaryStage.setOnCloseRequest((WindowEvent event) -> System.exit(0));
		DockPane.initializeDefaultUserAgentStylesheet();
		
		vbox.getStylesheets().add("/resources/style.css");
		
		System.out.println("Welcome to #@RMStrong Simulator made proudly at the Institute of Technology of Valence in 2018-2019 by fellow students under the guidance of Dr. Philippe Objois!");
		System.out.println("Licensed under the MPL 2.0 License");
		System.out.println("Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi");
	}

	/**
	 * set the simulator in simulation mode
	 */
	private void setExecutionMode() {
		this.setEditable(this.executionMode);
		
		this.codeEditor.setExecutionMode(this.executionMode);
		this.armMenuBar.setExecutionMode(this.executionMode);
		this.armToolBar.setExecutionMode(this.executionMode);
	}

	/**
	 * control the editions of ram and resisters by the user
	 * @param editable sets ram and resisters to editable state
	 */
	private void setEditable(boolean editable) {
		Platform.runLater(() -> {
			for (RegistersView registerView : this.registersViews) {
				registerView.setEditable(editable);
			}
			for (RamView ramView : this.ramViews) {
				ramView.setEditable(editable);
			}
		});
	}

	/**
	 * defining the events of the buttons in the menu bar, tool bar and the keyboard shortcuts
	 */
	private void setButtonEvents() {
		//MENU BAR
		//file
		this.armMenuBar.getNeW().setOnAction(actionEvent -> {
			if(!this.codeEditor.getProgramAsString().equals("")){
				warningPopup("All unsaved work will be lost", eventHandler -> this.codeEditor.setProgramAsString(""));
			}
			this.stage.setTitle("#@RMStrong");
		});
		this.armMenuBar.getOpenFile().setOnAction(actionEvent -> {
			if(!this.codeEditor.getProgramAsString().equals("")) {
				warningPopup("All unsaved work will be lost", eventHandler -> openProgram());
			}
			else {
				openProgram();
			}
		});
		this.armMenuBar.getSave().setOnAction(actionEvent -> {
			if(this.currentProgramPath == null){
				this.armMenuBar.getSaveAs().fire();
			} else {
				saveFile(codeEditor.getProgramAsString(), currentProgramPath);
			}
		});
		this.armMenuBar.getSaveAs().setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save assembly program");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ARMStrong Files", "*.ARMS"), new FileChooser.ExtensionFilter("Assembly Files", "*.S"));
			File chosenFile = fileChooser.showSaveDialog(stage);
			if (chosenFile != null) {
				if (!chosenFile.getAbsolutePath().endsWith(".ARMS") && !chosenFile.getAbsolutePath().endsWith(".S")) {
					chosenFile = new File(chosenFile.getAbsolutePath() + ".ARMS");
				}
				saveFile(codeEditor.getProgramAsString(), chosenFile);
				this.currentProgramPath = chosenFile;
			}

		});
		//window
		this.armMenuBar.getNewMemoryWindow().setOnAction(actionEvent -> {
			RamView moreRamView = new RamView(simulator);
			this.ramViews.add(moreRamView);
			moreRamView.getNode().dock(dockPane, DockPos.RIGHT);
		});
		this.armMenuBar.getNewInterpreterWindow().setOnAction(actionEvent -> {
			armMenuBar.getStopMenuItem().fire();
			if (!this.isInterpreterMode) {
				this.isInterpreterMode = true;
				this.interpreter.getNode().dock(dockPane, DockPos.BOTTOM);
				this.interpreter.getNode().setVisible(true);
				this.interpreter.initialize();
				
				if (this.consoleView.getNode().isDocked())
					this.consoleView.getNode().undock();
				this.consoleView.getNode().setVisible(false);
				
				this.interpreter.getNode().getDockTitleBar().getCloseButton().setOnAction(_event -> {
					if (this.interpreter.getNode().isDocked())
						this.interpreter.getNode().undock();
					this.interpreter.getNode().setVisible(false);
					this.consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
					this.interpreter.stopInterpreter();
					this.consoleView.redirectToConsole();
					this.setEditable(executionMode);
					this.armMenuBar.setExecutionMode(executionMode);
					this.armToolBar.setExecutionMode(executionMode);
					this.armMenuBar.getSwitchMode().setDisable(false);
					this.armToolBar.getSwitchButton().setDisable(false);
					this.running.set(false);
					this.consoleView.getNode().setVisible(true);
					this.isInterpreterMode = false;
				});
				
				this.running.set(true);
				
				this.interpreter.getTextField().setOnKeyPressed((KeyEvent ke) -> {
					if (ke.getCode().equals(KeyCode.ENTER)) {
						this.interpreter.run();
					}
				});
				
				this.armMenuBar.setExecutionMode(false);
				this.armToolBar.setExecutionMode(false);
				this.armMenuBar.getSwitchMode().setDisable(true);
				this.armToolBar.getSwitchButton().setDisable(true);
			}
		});

		//WINDOWS
		this.armMenuBar.getNewRegistersWindow().setOnAction(actionEvent -> {
			RegistersView moreRegistersView = new RegistersView(simulator);
			this.registersViews.add(moreRegistersView);
			moreRegistersView.getNode().setMaxWidth(220);
			moreRegistersView.getNode().dock(dockPane, DockPos.LEFT);
		});
		this.armMenuBar.getNewLedGame().setOnAction(actionEvent -> {
			LedView moreLedView = new LedView(this.simulator);
			this.ledViews.add(moreLedView);
			moreLedView.getNode().dock(dockPane, DockPos.RIGHT);
			moreLedView.getNode().getDockTitleBar().getCloseButton().setOnAction(actionEvent1 -> moreLedView.close());
		});
		this.armMenuBar.getNewEightSegmentDisplayWindow().setOnAction(actionEvent -> {
			EightSegmentDisplay moreSegment = new EightSegmentDisplay(this.simulator);
			this.eightSegmentDisplays.add(moreSegment);
			moreSegment.getNode().setMaxHeight(150);
			moreSegment.getNode().dock(dockPane, DockPos.TOP);
			moreSegment.getNode().getDockTitleBar().getCloseButton().setOnAction(actionEvent1 -> moreSegment.close());
		});


		this.armMenuBar.getPreferences().setOnAction(actionEvent -> new Preferences(simulator, this));

		//Run
		this.armMenuBar.getSwitchMode().setOnAction(actionEvent -> {
			if (!running.get()) {
				executionMode = !executionMode;
				if (executionMode) {
					try {
						simulator.resetState();
						simulator.setProgram(codeEditor.getProgramAsString());
						this.updateUI();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						executionMode = !executionMode;
					}
				}
				setExecutionMode();
			}
		});
		this.armMenuBar.getRunMenuItem().setOnAction(actionEvent -> {
			if (executionMode && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);
					
					this.setEditable(false);
					this.simulator.run();
					this.setEditable(true);
					
					this.running.set(false);

					updateUI();
				}).start();
			}
		});
		this.armMenuBar.getRunStepMenuItem().setOnAction(actionEvent -> {
			if (executionMode && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);
					
					this.setEditable(false);
					this.simulator.runStep();
					this.setEditable(true);
					
					this.running.set(false);

					updateUI();
				}).start();
			}
		});
		this.armMenuBar.getStopMenuItem().setOnAction(actionEvent -> {
			simulator.interruptExecutionFlow();
			updateUI();
			this.setEditable(false);
			this.running.set(false);
		});
		this.armMenuBar.getReloadMenuItem().setOnAction(actionEvent -> {
			if (!running.get()) {
				this.simulator.resetState();
				this.simulator.setProgram(codeEditor.getProgramAsString());
				updateUI();
			}
		});


		//TOOL BAR
		this.armToolBar.getSwitchButton().setOnAction(actionEvent -> armMenuBar.getSwitchMode().fire());
		this.armToolBar.getRunButton().setOnAction(actionEvent -> armMenuBar.getRunMenuItem().fire());
		this.armToolBar.getStepByStepButton().setOnAction(actionEvent -> armMenuBar.getRunStepMenuItem().fire());
		this.armToolBar.getReloadButton().setOnAction(actionEvent -> armMenuBar.getReloadMenuItem().fire());
		this.armToolBar.getStopButton().setOnAction(actionEvent -> armMenuBar.getStopMenuItem().fire());

		// the console
		this.consoleView.getTextField().setOnKeyPressed((KeyEvent ke) -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				if (simulator.isWaitingForInput()) {
					simulator.setConsoleInput(this.consoleView.getTextField().getText());
					System.out.println("[INFO] Input [" + this.consoleView.getTextField().getText() + "] added to Input queue");
				} else {
					System.out.println("[INFO] Input discarded (The CPU was not waiting for INPUT and you aren't in Interpreter mode)");
					System.out.println("[INFO] Maybe you wanted to use CpuConsoleGetString @ 0x" + Long.toHexString(CpuConsoleGetString.ROUTINE_ADDRESS) + " in your assembly?");
				}
				this.consoleView.getTextField().clear();
			}
		});

		//the keyboard shortcuts
		this.scene.setOnKeyPressed((KeyEvent ke) -> {
			if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(ke)) {
				this.armMenuBar.getSave().fire();
			}
			if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN).match(ke)) {
				this.armMenuBar.getSaveAs().fire();
			}
			if (new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN).match(ke)) {
				this.armMenuBar.getOpenFile().fire();
			}
			if (new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN).match(ke)) {
				this.armMenuBar.getReloadMenuItem().fire();
			}
			if (new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN).match(ke)) {
				this.armMenuBar.getNeW().fire();
			}
			if (new KeyCodeCombination(KeyCode.F5).match(ke)) {
				this.armMenuBar.getRunMenuItem().fire();
			}
			if (new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN).match(ke)) {
				this.armMenuBar.getSwitchMode().fire();
			}
			if (new KeyCodeCombination(KeyCode.F11).match(ke)) {
				this.armMenuBar.getRunStepMenuItem().fire();
			}
		});
	}

	public ArmMenuBar getArmMenuBar() {
		return this.armMenuBar;
	}
	
	/**
	 * opens a program in .S or .ARMS format
	 */
	private void openProgram() {
		FileChooser fileChooser = new FileChooser();
		if(this.currentProgramPath != null){
			fileChooser.setInitialDirectory(currentProgramPath.getParentFile());
		}
		fileChooser.setTitle("Open Program");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ARMStrong, Assembly Files (*.ARMS, *.S)", "*.ARMS", "*.S", "*.s"));

		File chosenFile = fileChooser.showOpenDialog(this.stage);
		if (chosenFile != null) {
			try {
				if (chosenFile.getAbsolutePath().endsWith(".ARMS")){
					try {
						simulator.loadSaveFromFile(chosenFile.getAbsolutePath());
						this.codeEditor.setProgramAsString(simulator.getProgramFromSave());
					} catch (InvalidInstructionException e) {
						warningPopup("You are opening a file containing syntax errors.", (_event) -> this.codeEditor.setProgramAsString(simulator.getProgramFromSave()));								
					}
				}
				else{
					this.codeEditor.setProgramAsString(Files.readString(Paths.get(chosenFile.getAbsolutePath())));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.currentProgramPath = chosenFile;
			this.stage.setTitle("#@RMStrong - " + currentProgramPath.getAbsolutePath());
		}
	}

	/**
	 * display a warning popup
	 * @param message the message to display
	 * @param okEvent an event fired when the user presses the ok button
	 */
	public static void warningPopup(String message, EventHandler<ActionEvent> okEvent) {
		final Stage warningStage = new Stage();
		warningStage.setTitle("Warning");

		warningStage.initModality(Modality.APPLICATION_MODAL);
		warningStage.setResizable(false);

		try {
			Pane main = FXMLLoader.load(Gui.class.getResource("/resources/warning.fxml"));
			warningStage.setScene(new Scene(main, 500, 280));

			Text messageText = (Text) main.lookup("#message");
			messageText.setText(message);

			ImageView image = (ImageView) main.lookup("#image");
			image.setImage(new Image(Gui.class.getResource("/resources/warning.png").toExternalForm()));

			Button okButton = (Button) main.lookup("#ok");
			Button cancelButton = (Button) main.lookup("#cancel");
			okButton.setOnAction(okEvent);
			okButton.setOnMouseReleased(mouseEvent -> warningStage.close());
			okButton.setOnKeyPressed(keyEvent -> warningStage.close());

			cancelButton.setOnAction(actionEvent1 -> warningStage.close());

		} catch (IOException e) {
			e.printStackTrace();
		}
		warningStage.show();
	}

	/**
	 * update all the ui
	 */
	private void updateUI() {
		updateUIFast();
		updateUISlow();
	}

	/**
	 * updates the leds, the 8segments and the selected line in simulation mode
	 */
	private void updateUIFast() {
		Platform.runLater(() -> {
			for (LedView ledView : this.ledViews) {
				ledView.refresh();
			}

			if (this.executionMode && !this.isInterpreterMode)
				this.codeEditor.highlightLine(this.simulator.getCurrentLine());

			for (EightSegmentDisplay eightSeg : this.eightSegmentDisplays){
				eightSeg.refresh();
			}
		});
	}

	/**
	 * updates the ram and registers
	 */
	private void updateUISlow() {
		Platform.runLater(() -> {
			if (this.interfaceBeingUpdated.get()) {
				return;
			}
			
			this.interfaceBeingUpdated.set(true);
			
			for (RegistersView registerView : this.registersViews) {
				registerView.updateRegisters();
			}
			
			for (RamView ramView : this.ramViews) {
				ramView.refresh();
			}

			this.interfaceBeingUpdated.set(false);
		});
	}


	private void newUpdateThread(AtomicBoolean runningFlag) {
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				if (!runningFlag.get()) {
					continue;
				}
				
				if (!this.interfaceBeingUpdated.get())
					updateUISlow();
				else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}).start();
		
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				if (!executionMode) {
					continue;
				}

				updateUIFast();
			}
		}).start();
	}

	/**
	 * saves the file in .arms or .S
	 * @param content the code as text
	 * @param theFile the path to the file to save
	 */
	private void saveFile(String content, File theFile) {
		if (theFile.getAbsolutePath().endsWith(".ARMS")){
			try {
				this.simulator.setProgram(this.codeEditor.getProgramAsString());
				this.simulator.saveToFile(theFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidInstructionException e) {
				warningPopup("You are saving a file containing syntax errors.", (_event) -> {try {
					simulator.saveToFile(theFile.getAbsolutePath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}});
			}
		} else {
			try {
				Files.writeString(theFile.toPath(), content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * parses a int in hexadecimal, decimal or binary
	 * @param input the string to parse
	 * @return the parsed int
	 * @throws FormatException the sring is not parsable
	 */
	public static int parseUserAdress(String input) {
		int address = 0;
		input = input.trim();
		try {
			if (input.startsWith("0x") || input.startsWith("0X")) {
				address = Integer.parseUnsignedInt(input.substring(2), 16); // parsing a int in base 16, the 2
				// first chars of the string are
				// removed (0x)
			} else if (input.startsWith("0b") || input.startsWith("0B")) {
				address = Integer.parseUnsignedInt(input.substring(2), 2);
			} else if (input.startsWith("0d") || input.startsWith("0D")) {
				address = Integer.parseUnsignedInt(input.substring(2));
			} else {
				address = Integer.parseInt(input);
			}
		} catch (NumberFormatException e) {
			Gui.warningPopup(e.getMessage(), ActionEvent -> {});
		}
		return address;
	}
}
