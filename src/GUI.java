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

	//the code editor
	private TextArea codingTextArea;
	private TextFlow executionModeTextFlow;
	private boolean executionMode;
	private File programFilePath;


	private GUIMenuBar theGUIMenuBar;
	private GUIMemoryView theGUIMemoryView;
	private GUIRegisterView theGUIRegisterView;

	public void startGUI() {
		launch(null);
	}

	@Override
	public void start(Stage stage) throws Exception {

		theArmSimulator = new ArmSimulator();
		programFilePath = null;
		this.executionMode = false;


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

		stage.show(); //to be sure the scene.lookup() works properly //////////////////////////////////////////////////////////////////////



		theGUIMenuBar = new GUIMenuBar((MenuBar) scene.lookup("#theMenuBar"));
		theGUIMemoryView = new GUIMemoryView(scene, theArmSimulator);
		theGUIRegisterView = new GUIRegisterView(scene, theArmSimulator);


		//THE CODING AREA

		codingTextArea = (TextArea) scene.lookup("#codeTexArea");
		executionModeTextFlow = (TextFlow) scene.lookup("#executionModeTextFlow");


		//THE ACTION EVENTS

		theGUIMenuBar.getEnterExecutionModeMenuItem()	.setOnAction(new EventHandler<ActionEvent>() {
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
		theGUIMenuBar.getExitExecutionModeMenuItem()	.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				exitExecutionMode();
			}
		});

		theGUIMenuBar.getRunMenuItem()		.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (executionMode){
					theArmSimulator.run();
					theGUIRegisterView.updateRegisters();
					theGUIMemoryView.updateMemoryView();
					stage.show();
				}
			}
		});
		theGUIMenuBar.getRunSingleMenuItem().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (executionMode){
					theArmSimulator.runStep();
					theGUIRegisterView.updateRegisters();
					theGUIMemoryView.updateMemoryView();
					stage.show();
				}
			}
		});

		theGUIMenuBar.getOpenMenuItem()		.setOnAction(new EventHandler<ActionEvent>() {
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
		theGUIMenuBar.getSaveAsMenuItem()	.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save assembly program");
				File chosenFile = fileChooser.showSaveDialog(stage);
				saveFile(codingTextArea.getText(), chosenFile);

			}
		});
		theGUIMenuBar.getSaveMenuItem()		.setOnAction(new EventHandler<ActionEvent>() {
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


		theGUIMemoryView.updateMemoryView();
		theGUIRegisterView.updateRegisters();
		stage.show();

	}

	private void exitExecutionMode() {
		this.executionMode = false;
		codingTextArea.setEditable(true);
		codingTextArea.setVisible(true);
		theGUIMenuBar.exitExecMode();
	}

	private void enterExecutionMode(String program) { //TODO parametre peut etre temoraire
		this.executionMode = true;
		codingTextArea.setEditable(false);
		codingTextArea.setVisible(false);
		theGUIMenuBar.setExecMode();
		executionModeTextFlow.getChildren().add(new Text(program));
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
