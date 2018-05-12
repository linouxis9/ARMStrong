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
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI is the class responsible of handling the JaveFX Graphical User Interface.
 */
public class GUI extends Application {

	ArmSimulator theArmSimulator;

	Scene scene;

	Stage stage;

	List<Text> hexadecimalRegisterText;
	List<Text> decimalRegisterText;
	List<Text> signedDecimalRegisterText;

	List<Text> MemoryAddressViewList;
	List<Text> MemoryContentList;
	int memoryViewFirstAddress;
	int memoryDisplayMode;

	public void startGUI() {
		launch(null);
	}

	@Override
	public void start(Stage stage) throws Exception {

		theArmSimulator = new ArmSimulator();

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

		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu runMenu = new Menu("Run");
		Menu helpMenu = new Menu("Help");

		MenuItem newMenuItem = new MenuItem("New");
		MenuItem saveMenuItem = new MenuItem("Save");
		MenuItem exitMenuItem = new MenuItem("Exit");
		MenuItem runAllMenuItem = new MenuItem("Run All");
		MenuItem runSingleMenuItem = new MenuItem("Run a single instruction");

		theMenuBar.getMenus().addAll(fileMenu, editMenu, runMenu, helpMenu);

		fileMenu.getItems().addAll(newMenuItem, saveMenuItem, exitMenuItem);
		editMenu.getItems().addAll();
		runMenu.getItems().addAll(runAllMenuItem, runSingleMenuItem);
		helpMenu.getItems().addAll();

		//FETCHING THE ELEMENTS

		//the coding area

		TextArea codingTextArea = (TextArea) scene.lookup("#codeTexArea");

		//the fields

		TextField goToAddressField = (TextField) scene.lookup("#goToAddressField");
		//TextField consoleUserInput = (TextField) scene.lookup("#"); //TODO add an id

		//the buttons

		Button button8bitView = (Button) scene.lookup("#button8Bit");
		Button button16bitView = (Button) scene.lookup("#button16Bit");
		Button button32bitView = (Button) scene.lookup("#button32Bit");

		Button memoryButtonUp  = (Button) scene.lookup("#memoryButtonUp");
		Button memoryButtonDown  = (Button) scene.lookup("#memoryButtonDown");

		Button goToAddressButton = (Button) scene.lookup("#goToAddressButton");

		//Button runAllButton = (Button) scene.lookup("#"); //TODO add an id
		//Button runStepByStepButton = (Button) scene.lookup("#"); //TODO add an id

		//the text display

			//the register view

		this.hexadecimalRegisterText = new ArrayList<Text>();
		this.decimalRegisterText = new ArrayList<Text>();
		this.signedDecimalRegisterText = new ArrayList<Text>();

		for(int register=0; register<16; register++){
			this.hexadecimalRegisterText.add((Text) this.scene.lookup("#register"+register+"Hex"));
			this.decimalRegisterText.add((Text) this.scene.lookup("#register"+register+"Dec"));
			this.signedDecimalRegisterText.add((Text) this.scene.lookup("#register"+register+"SigDec"));
		}

			//the memory view

		this.MemoryAddressViewList = new ArrayList<Text>();
		this.MemoryContentList = new ArrayList<Text>();

		for(int address=1; address<9; address++){
			MemoryAddressViewList.add((Text) scene.lookup("#addresslMemoryView"+address));
			MemoryContentList.add((Text) scene.lookup("#contentMemoryView"+address));
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
				updateMemoryView();
			}
		});

		runAllMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String programString = codingTextArea.getText();
				theArmSimulator.run(programString);
				updateRegisters();
				updateMemoryView();
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

	/**
	 * Update the displayed registers
	 */
	public void updateRegisters(){

		int currentRegisterValue;

		for(int currentRegisterNumber=0; currentRegisterNumber<16; currentRegisterNumber++){
			currentRegisterValue = ArmSimulator.getRegisterValue(currentRegisterNumber);
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
					MemoryContentList.get(labelNumber).		setText(""+ArmSimulator.getRamByte(displayedMemoryAddress));
					displayedMemoryAddress++;
				}
				break;
			case 16:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
					MemoryAddressViewList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText(""+ArmSimulator.getRamHWord(displayedMemoryAddress));
					displayedMemoryAddress+=2;
				}
				break;
			case 32:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
					MemoryAddressViewList.get(labelNumber).setText("0x"+Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText("" +ArmSimulator.getRamWord(displayedMemoryAddress));
					displayedMemoryAddress += 4;
				}
				break;
			default:
				this.memoryDisplayMode=8;
		}

	}

}
