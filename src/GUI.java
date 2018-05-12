 
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import simulator.*;

/**
 * GUI is the class responsible of handling the JaveFX Graphical User Interface.
 */
public class GUI extends Application {

	Parent root;
	Scene scene;

	Stage stage;


	List<Text> hexadecimalRegisterText;
	List<Text> decimalRegisterText;
	List<Text> signedDdecimalRegisterText;

	List<Text> MemoryAddressViewList;
	List<Text> MemoryContentList;

	TextField goToAddressField;
	Button goToAddressButton;


	int memoryViewFirstAddress;
	int memoryDisplayMode;

	//testing
	Memory myMemory;

	public void startIHM() {
		launch(null);
	}

	@Override
	public void start(Stage stage) throws Exception {

		//setting "static" elements

		this.stage = stage;

		root = FXMLLoader.load(getClass().getResource("ihm_#@rm.fxml"));
		scene = new Scene(root, 700, 275);

		stage.setMaximized(true);
		stage.setMinHeight(800);
		stage.setMinWidth(800);
		stage.setTitle("#@RM");
		stage.setScene(scene);

		stage.show();

		MenuBar theMenuBar = (MenuBar) scene.lookup("#theMenuBar");
		Menu file = new Menu("File");
		Menu run = new Menu("Run");
		MenuItem runAllMenuItem = new MenuItem("Run All");
		theMenuBar.getMenus().addAll(run, file);
		run.getItems().add(runAllMenuItem);
		
		//Add the CSS file
    	String css = getClass().getResource("css.css").toExternalForm();
		scene.getStylesheets().addAll(css);
	    
		//Change the icon of the application
		Image applicationIcon = new Image("file:logo.png");
		stage.getIcons().add(applicationIcon);

		

		//setting up "moving" elements

		TextArea codeTexArea = (TextArea) scene.lookup("#codeTexArea");

		//the buttons

		Button button8bitView = (Button) scene.lookup("#button8Bit");
		Button button16bitView = (Button) scene.lookup("#button16Bit");
		Button button32bitView = (Button) scene.lookup("#button32Bit");

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

		Button memoryButtonUp  = (Button) scene.lookup("#memoryButtonUp");
		Button memoryButtonDown  = (Button) scene.lookup("#memoryButtonDown");

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
				String programString = codeTexArea.getText();
				Cpu cpu = new Cpu();
				Program program = new Program();
				Interpretor interpretor = new Interpretor(cpu, program);
				program.setNewProgram(programString);

				try {
					interpretor.parseProgram();
					cpu.execute();
				} catch (AssemblyException exeption) {
					exeption.printStackTrace();
				}

				int[] registerValues = new int[16];
				for(int i=0; i<16; i++){
					registerValues[i]=cpu.getRegisters(i).getValue();
				}

				updateRegisters(registerValues);

				myMemory = cpu.ram;
				updateMemoryView();
			}
		});

		//the console output

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


		//the register view

        hexadecimalRegisterText = new ArrayList<Text>();
        decimalRegisterText = new ArrayList<Text>();
        signedDdecimalRegisterText = new ArrayList<Text>();


        for(int register=0; register<16; register++){
            hexadecimalRegisterText.add((Text) scene.lookup("#register"+register+"Hex"));
            decimalRegisterText.add((Text) scene.lookup("#register"+register+"Dec"));
            signedDdecimalRegisterText.add((Text) scene.lookup("#register"+register+"SigDec"));
        }

		//the memory view

		//testing code
		myMemory = new Ram(1000);
		Address anAddress = new Address(3);
		myMemory.setByte(anAddress, (byte)14);
		//-----------

		MemoryAddressViewList = new ArrayList<Text>();
		MemoryContentList = new ArrayList<Text>();

		for(int address=1; address<9; address++){
			MemoryAddressViewList.add((Text) scene.lookup("#addresslMemoryView"+address));
			MemoryContentList.add((Text) scene.lookup("#contentMemoryView"+address));
		}

		goToAddressButton = (Button) scene.lookup("#goToAddressButton");
		goToAddressField = (TextField) scene.lookup("#goToAddressField");

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


		memoryViewFirstAddress = 0;
		memoryDisplayMode = 8;

		updateMemoryView();

		stage.show();

	}

	/**
	 * Update the displayed registers
	 * @param registersValues
	 * 		the int values of the registers
	 */
	public void updateRegisters(int[] registersValues){
		for(int register=0; register<16; register++){
			hexadecimalRegisterText.get(register).setText(Integer.toHexString(registersValues[register]));
			/*decimalRegisterText.get(register).setText(""+Integer.toUnsignedLong(registersValues[register]));
			signedDdecimalRegisterText.get(register).setText(""+registersValues[register]);*/
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
					MemoryAddressViewList.get(labelNumber).setText("0x" + Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText(""+myMemory.getByte(new Address(displayedMemoryAddress)));
					displayedMemoryAddress++;
				}
				break;
			case 16:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++){
					MemoryAddressViewList.get(labelNumber).setText("0x" +Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText(""+myMemory.getHWord(new Address(displayedMemoryAddress)));
					displayedMemoryAddress+=2;
				}
				break;
			case 32:
				for (int labelNumber=0 ; labelNumber < displayableMemoryRows; labelNumber++) {
					MemoryAddressViewList.get(labelNumber).setText("0x"+Integer.toHexString(displayedMemoryAddress));
					MemoryContentList.get(labelNumber).setText("" + myMemory.getValue(new Address(displayedMemoryAddress)));
					displayedMemoryAddress += 4;
				}
				break;
			default:
				this.memoryDisplayMode=8;
		}

	}

}
