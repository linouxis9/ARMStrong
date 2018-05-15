import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import simulator.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * GUI is the class responsible of handling the JaveFX Graphical User Interface.
 */
public class GUI extends Application {

	private ArmSimulator theArmSimulator;

	private Scene scene;
	private Stage stage;

	//the registers
	private List<Text> hexadecimalRegisterText;
	private List<Text> decimalRegisterText;
	private List<Text> signedDecimalRegisterText;

	//the memory
	private List<Text> MemoryAddressViewList;
	private List<Text> MemoryContentList;
	private int memoryViewFirstAddress;
	private int memoryDisplayMode;

	//the code editor
	private TextArea codingTextArea;
	private TextFlow executionModeTextFlow;
	private boolean executionMode;
	private File programFilePath;

	private List<MenuItem> menusToDisableInExecMode;

	public void startGUI() {
		launch(null);
	}

	@Override
	public void start(Stage stage) throws Exception {

		theArmSimulator = new ArmSimulator();
		programFilePath = null;
		this.executionMode = false;
		menusToDisableInExecMode = new ArrayList<MenuItem>();

		//setting static elements

		Parent root;

		this.stage = stage;

		root = FXMLLoader.load(getClass().getResource("ihm_#@rm.fxml"));
		scene = new Scene(root, 700, 275);

		stage.setMaximized(true);
		stage.setMinHeight(800);
		stage.setMinWidth(800);
		stage.setTitle("#@RM");
		stage.setScene(scene);

		//Add the CSS file
		String css = getClass().getResource("css.css").toExternalForm();
		scene.getStylesheets().addAll(css);

		//Change the icon of the application
		Image applicationIcon = new Image("file:logo.png");
		stage.getIcons().add(applicationIcon);

		stage.show();

		//THE MENU BAR
		MenuBar theMenuBar = (MenuBar) scene.lookup("#theMenuBar");

		Menu fileMenu 	= new Menu("File");
		Menu editMenu 	= new Menu("Edit");
		Menu runMenu 	= new Menu("Run");
		Menu helpMenu 	= new Menu("Help");

		MenuItem newMenuItem 	= new MenuItem("New");
		MenuItem openMenuItem 	= new MenuItem("Open file");
		MenuItem saveMenuItem 	= new MenuItem("Save");
		MenuItem saveAsMenuItem = new MenuItem("Save as");
		MenuItem exitMenuItem 	= new MenuItem("Exit");

		MenuItem enterExecutionModeMenuItem = new MenuItem("Enter in execution mode");
		MenuItem exitExecutionModeMenuItem 	= new MenuItem("Exit the execution mode");
		MenuItem runMenuItem 				= new MenuItem("Run");
		MenuItem runSingleMenuItem 			= new MenuItem("Run a single instruction");

		theMenuBar.getMenus().addAll(fileMenu, editMenu, runMenu, helpMenu);

		fileMenu.getItems().addAll(newMenuItem,openMenuItem,saveMenuItem, saveAsMenuItem, exitMenuItem);
		editMenu.getItems().addAll();
		runMenu.getItems().addAll(enterExecutionModeMenuItem,exitExecutionModeMenuItem, runMenuItem, runSingleMenuItem);
		helpMenu.getItems().addAll();

		menusToDisableInExecMode.add(newMenuItem);
		menusToDisableInExecMode.add(openMenuItem);
		menusToDisableInExecMode.add(saveMenuItem);
		menusToDisableInExecMode.add(saveAsMenuItem);
		menusToDisableInExecMode.add(enterExecutionModeMenuItem);

		//FETCHING THE ELEMENTS

		//the coding area

		codingTextArea = (TextArea) scene.lookup("#codeTexArea");
		executionModeTextFlow = (TextFlow) scene.lookup("#executionModeTextFlow");

		//the fields

		TextField goToAddressField = (TextField) scene.lookup("#goToAddressField");
		//TextField consoleUserInput = (TextField) scene.lookup("#"); //TODO add an id

		//the buttons

		Button button8bitView 	= (Button) scene.lookup("#button8Bit");
		Button button16bitView 	= (Button) scene.lookup("#button16Bit");
		Button button32bitView 	= (Button) scene.lookup("#button32Bit");

		Button memoryButtonUp  	= (Button) scene.lookup("#memoryButtonUp");
		Button memoryButtonDown = (Button) scene.lookup("#memoryButtonDown");

		Button goToAddressButton = (Button) scene.lookup("#goToAddressButton");

		//Button runAllButton = (Button) scene.lookup("#"); //TODO add an id
		//Button runStepByStepButton = (Button) scene.lookup("#"); //TODO add an id

		//the text display

			//the register view

		this.hexadecimalRegisterText 	= new ArrayList<Text>();
		this.decimalRegisterText 		= new ArrayList<Text>();
		this.signedDecimalRegisterText 	= new ArrayList<Text>();

		for(int register=0; register<16; register++){
			this.hexadecimalRegisterText.	add((Text) this.scene.lookup("#register"+register+"Hex"));
			this.decimalRegisterText.		add((Text) this.scene.lookup("#register"+register+"Dec"));
			this.signedDecimalRegisterText.	add((Text) this.scene.lookup("#register"+register+"SigDec"));
		}

			//the memory view

		this.MemoryAddressViewList 	= new ArrayList<Text>();
		this.MemoryContentList 		= new ArrayList<Text>();

		for(int address=1; address<9; address++){
			MemoryAddressViewList	.add((Text) scene.lookup("#addresslMemoryView"+address));
			MemoryContentList		.add((Text) scene.lookup("#contentMemoryView"+address));
		}


		//THE ACTION EVENTS

		button8bitView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				memoryDisplayMode=8;
				updateMemoryView();
			}
		});
		button16bitView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				memoryDisplayMode = 16;
				updateMemoryView();
			}
		});
		button32bitView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				memoryDisplayMode = 32;
				updateMemoryView();
			}
		});

		memoryButtonUp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switch(memoryDisplayMode){
					case 8:
						memoryViewFirstAddress--;
						break;
					case 16:
						memoryViewFirstAddress-=2;
						break;
					case 32:
						memoryViewFirstAddress-=4;
						break;
				}
				if (memoryViewFirstAddress<0){
					memoryViewFirstAddress = 0;
				}
				updateMemoryView();
			}
		});
		memoryButtonDown.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switch(memoryDisplayMode){
					case 8:
						memoryViewFirstAddress++;
						break;
					case 16:
						memoryViewFirstAddress+=2;
						break;
					case 32:
						memoryViewFirstAddress+=4;
						break;
				}
				//TODO add max address check
				updateMemoryView();
			}
		});

		enterExecutionModeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				String programString = codingTextArea.getText();
				try {
					theArmSimulator.setProgramString(programString);
					enterExecutionMode(programString);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		exitExecutionModeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				exitExecutionMode();
			}
		});


		runMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (executionMode){
					theArmSimulator.run();
					updateRegisters();
					updateMemoryView();
				}
			}
		});
		runSingleMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (executionMode){
					theArmSimulator.runStep();
					updateRegisters();
					updateMemoryView();
				}
			}
		});



		openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open a source File");
				String path = fileChooser.showOpenDialog(stage).getAbsolutePath();
				if(path != null) {
					try {
						codingTextArea.setText(new String(Files.readAllBytes(Paths.get(path)), "UTF-8"));
						programFilePath = new File(path);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		saveAsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save assembly program");
				File chosenFile = fileChooser.showSaveDialog(stage);
				saveFile(codingTextArea.getText(), chosenFile);
				
			}
		});
		saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (programFilePath != null){
					saveFile(codingTextArea.getText(), programFilePath);
				}
				else
				{
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save assembly program");
					File chosenFile = fileChooser.showSaveDialog(stage);
					saveFile(codingTextArea.getText(), chosenFile);
				}


			}
		});

		goToAddressButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String addressTyped = goToAddressField.getText();
				Text MemoryViewTitleText = (Text) scene.lookup("#goToAddressLabel");
				MemoryViewTitleText.setText("Go to address: ");
				MemoryViewTitleText.setUnderline(false);

				try {
					if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
						memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 16); //parsing a int in base 16, the 2 first chars of the string are removed (0x)
					} else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
						memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 2); //parsing a int in base 2, the 2 first chars of the string are removed (0b)
					} else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
						memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 10); //parsing a int in base 10, the 2 first chars of the string are removed (0b)
					} else {
						memoryViewFirstAddress = Integer.parseInt(addressTyped);
					}
					updateMemoryView();
				}
				catch (java.lang.NumberFormatException exeption){
					MemoryViewTitleText.setText("The address is invalid");
					MemoryViewTitleText.setUnderline(true);
				}

			}
		});
		goToAddressField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					String addressTyped = goToAddressField.getText();
					Text MemoryViewTitleText = (Text) scene.lookup("#goToAddressLabel");
					MemoryViewTitleText.setText("Go to address: ");
					MemoryViewTitleText.setUnderline(false);

					try {
						if (addressTyped.startsWith("0x") || addressTyped.startsWith("0X")) {
							memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 16); //parsing a int in base 16, the 2 first chars of the string are removed (0x)
						} else if (addressTyped.startsWith("0b") || addressTyped.startsWith("0B")) {
							memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 2); //parsing a int in base 2, the 2 first chars of the string are removed (0b)
						} else if (addressTyped.startsWith("0d") || addressTyped.startsWith("0D")) {
							memoryViewFirstAddress = Integer.parseInt(addressTyped.substring(2), 10); //parsing a int in base 10, the 2 first chars of the string are removed (0b)
						} else {
							memoryViewFirstAddress = Integer.parseInt(addressTyped);
						}
						updateMemoryView();
					}
					catch (java.lang.NumberFormatException exeption){
						MemoryViewTitleText.setText("The address is invalid");
						MemoryViewTitleText.setUnderline(true);
					}
				}
			}
		});


		//THE CONSOLE OUTPUT

		OutputStream consoleOut = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				Platform.runLater(() ->{
					TextFlow consoleTextFlow = (TextFlow) scene.lookup("#consoleTextFlow");
					consoleTextFlow.getChildren().add(new Text((String.valueOf((char) b))));
				});
			}
		};
		System.setOut(new PrintStream(consoleOut, true));


		memoryViewFirstAddress = 0;
		memoryDisplayMode = 8;

		updateMemoryView();
		updateRegisters();

		stage.show();

	}

	private void exitExecutionMode() {
		this.executionMode = false;
		codingTextArea.setEditable(true);
		codingTextArea.setVisible(true);
		for(int item = 0; item < menusToDisableInExecMode.size(); item++){
			menusToDisableInExecMode.get(item).setDisable(false);
		}
	}

	private void enterExecutionMode(String program) { //TODO parametre peut etre temoraire
		this.executionMode = true;
		codingTextArea.setEditable(false);
		codingTextArea.setVisible(false);
		executionModeTextFlow.getChildren().add(new Text(program));
		for(int item = 0; item < menusToDisableInExecMode.size(); item++){
			menusToDisableInExecMode.get(item).setDisable(true);
		}

	}

	/**
	 * Update the displayed registers
	 */
	private void updateRegisters(){

		int currentRegisterValue;

		for(int currentRegisterNumber=0; currentRegisterNumber<16; currentRegisterNumber++){
			currentRegisterValue = theArmSimulator.getRegisterValue(currentRegisterNumber);
			hexadecimalRegisterText.	get(currentRegisterNumber).setText(Integer.toHexString(currentRegisterValue));
			//decimalRegisterText.		get(currentRegisterNumber).setText(""+Integer.toUnsignedLong(currentRegisterValue));
			//signedDecimalRegisterText.	get(currentRegisterNumber).setText(""+currentRegisterValue);
		}
		stage.show();
	}

	/**
	 * updates the memory view
	 */
	private void updateMemoryView(){
		int displayableMemoryRows = 8; //temporaire!!!

		int displayedMemoryAddress = memoryViewFirstAddress;

		switch(this.memoryDisplayMode){
			case 8:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
					MemoryAddressViewList.get(labelNumber).	setText("0x" + Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).		setText(""+theArmSimulator.getRamByte(displayedMemoryAddress));
					displayedMemoryAddress++;
				}
				break;
			case 16:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
					MemoryAddressViewList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText(""+theArmSimulator.getRamHWord(displayedMemoryAddress));
					displayedMemoryAddress+=2;
				}
				break;
			case 32:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
					MemoryAddressViewList.get(labelNumber).setText("0x"+Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText("" +theArmSimulator.getRamWord(displayedMemoryAddress));
					displayedMemoryAddress += 4;
				}
				break;
			default:
				this.memoryDisplayMode=8;
		}

	}

	private void saveFile(String content, File theFile){
		if (theFile != null) {
			try {
				FileWriter outputStream = new FileWriter(theFile);
				outputStream.write(content);
				outputStream.close();
				this.programFilePath = theFile;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
