package simulator.ui.javafx;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import simulator.boilerplate.ArmSimulator;
import simulator.core.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

/**
 * GUI is the class responsible of handling the JavaFX Graphical User Interface.
 */
public class GUI extends Application {

	//THE GRAPHICAL ELEMENTS
	private Scene scene;
	private Stage stage;
	private Parent root;

	// the code editor
	private TextArea codingTextArea;
	private TextFlow executionModeTextFlow;

	//the views
	private GUIMenuBar theGUIMenuBar;
	private GUIMemoryView theGUIMemoryView;
	private GUIRegisterView theGUIRegisterView;
	private GUIButtonBar theGUIButtonBar;
	private ScrollPane consoleScrollPane; //the console

	BorderPane centralBorderPaneContainer; //contains: theGUIRegisterView and (the codingTextArea or the executionModeTextFlow) and theGUIMemoryView

	//THE OTHER ELEMENTS
	private ArmSimulator theArmSimulator;

	private AtomicBoolean executionMode;
	private AtomicBoolean running;

	private File programFilePath;
	private String lastFilePath;

	private List<Text> instructionsAsText;

	//the settings
	private Preferences prefs;
	private Set<String> themes;
	private static final String DEFAULT_THEME = "red";
	private Color themeColor;
	
	private Set<String> languages;
	public static String language = "English";

	public void startGUI() {
		launch(null);
	}

	@Override
	public void start(Stage stage) throws Exception {

		this.theArmSimulator = new ArmSimulator();
		this.programFilePath = null;
		this.lastFilePath = null;
		this.executionMode = new AtomicBoolean(false);
		this.running = new AtomicBoolean(false);

		// setting graphical "static" elements
		this.stage = stage;
		this.root = FXMLLoader.load(getClass().getResource("ihmv4.fxml"));
		this.scene = new Scene(root, 800, 800); //TODO for smaller screens

		//this.stage.setMaximized(true);
		this.stage.setMinHeight(800);
		this.stage.setMinWidth(800);
		this.stage.setTitle("#@RM");
		this.stage.setScene(this.scene);
		this.stage.setOnHiding((WindowEvent event) -> {
			this.theArmSimulator.interruptExecutionFlow(true);
			System.exit(0);
		});

		// Change the icon of the application
		Image applicationIcon = new Image("file:logo.png");
		this.stage.getIcons().add(applicationIcon);

		Font.loadFont(getClass().getResource("Quicksand.ttf").toExternalForm(), 16);

		this.prefs = Preferences.userNodeForPackage(this.getClass());
		this.prefs.getBoolean("FONT", true);
		this.prefs.get("THEME", GUI.DEFAULT_THEME);
		this.themeColor = Color.RED;
		
		String colorR = "red";
		String colorB = "blue";
		String colorG = "green";
		
		themes = new HashSet<>();
		themes.add(colorR);
		themes.add(colorB);
		themes.add(colorG);
		applyTheme();
		
		GUI.language = this.prefs.get("LANGUAGE", language);
		
		String languageEN = "English";
		String languageFR = "French";
		
		languages = new HashSet<>();
		languages.add(languageEN);
		languages.add(languageFR);

		stage.show(); // to be sure the scene.lookup() works properly

		centralBorderPaneContainer = (BorderPane) scene.lookup("#borderPane");

		theGUIMenuBar = new GUIMenuBar((MenuBar) scene.lookup("#theMenuBar"));
		theGUIMemoryView = new GUIMemoryView(scene, theArmSimulator);
		theGUIRegisterView = new GUIRegisterView(scene, theArmSimulator);
		theGUIButtonBar = new GUIButtonBar((ToolBar) scene.lookup("#thebuttonBar"));

		// THE CODING AREA
		codingTextArea = (TextArea) scene.lookup("#codeTexArea");
		executionModeTextFlow = new TextFlow();

		//THE CONSOLE
		TextFlow consoleTextFlow = (TextFlow) scene.lookup("#consoleTextFlow");
		consoleTextFlow.getChildren().add(new Text(""));
		consoleScrollPane = (ScrollPane) scene.lookup("#consoleScrollPane");

		OutputStream consoleOut = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
					Platform.runLater(() -> {
						Text text = (Text)consoleTextFlow.getChildren().get(0);
						text.setText(text.getText() + (char)b);
						consoleScrollPane.setVvalue(consoleScrollPane.getHmax());
					});
			}
		};
		System.setOut(new PrintStream(consoleOut, true));

		setActionEvents();
		updateGUI();
		stage.show();

	}

	private void setActionEvents(){
		
		// THE PREFERENCES MENU
		theGUIMenuBar.getPreferencesMenuItem().setOnAction((ActionEvent actionEvent) -> {
			Stage preferencesDialog = new Stage();
			preferencesDialog.initModality(Modality.APPLICATION_MODAL);
			preferencesDialog.initOwner(stage);
			VBox dialogVbox = new VBox(20);

			String selectTheme = "Select a theme";
			String applyClose = "Apply and Close";
			String close = "Close";
			String chooseaTheme = "Choose a theme:";
			String chooseaLanguage = "Choose a language:";
			String useQuicksand = "Use the Quicksand font:";
			
			ChoiceBox<String> languageChoiceBox = new ChoiceBox<>();
			
			languageChoiceBox.getItems().addAll(languages);

			languageChoiceBox.setTooltip(new Tooltip("Choose a language"));

			languageChoiceBox.setValue(prefs.get("LANGUAGE", ""));

			languageChoiceBox.setId("choiceboxPreferencesLang");
			
			ChoiceBox<String> theme = new ChoiceBox<>();
			
			theme.getItems().addAll(themes);

			theme.setTooltip(new Tooltip(selectTheme));

			theme.setValue(prefs.get("THEME", ""));

			theme.setId("choiceboxPreferences");

			Button button1 = new Button(applyClose);
			button1.setId("applyClosePreferences");

			Button button2 = new Button(close);
			button2.setId("closePreferences");

			Label labelLanguage = new Label();
			labelLanguage.setText(chooseaLanguage);
			labelLanguage.setId("labelLanguagePreferences");
			
			Label labelTheme = new Label();
			labelTheme.setText(chooseaTheme);
			labelTheme.setId("labelThemePreferences");

			Label labelQuicksand = new Label();
			labelQuicksand.setText(useQuicksand);
			labelQuicksand.setId("labelQuicksandPreferences");

			CheckBox checkBoxFont = new CheckBox();

			checkBoxFont.setSelected(prefs.getBoolean("FONT", true));

			Text lineBreak = new Text();
			lineBreak.setFont(new Font(20));
			lineBreak.setText("\n");

			button1.setOnAction((ActionEvent e) -> {
				prefs.put("THEME", theme.getValue());

				prefs.putBoolean("FONT", checkBoxFont.isSelected());
				
				prefs.put("LANGUAGE", languageChoiceBox.getValue());
				
				GUI.language = languageChoiceBox.getValue();
				
				theGUIMenuBar.translate();
				theGUIRegisterView.translate();
				theGUIMemoryView.translate();
				
				applyTheme();

				preferencesDialog.close();
			});

			button2.setOnAction((ActionEvent e) ->	preferencesDialog.close());

			GridPane pane = new GridPane();
			pane.setAlignment(Pos.CENTER);
			pane.setHgap(5);
			pane.setVgap(5);
			pane.setPadding(new Insets(25, 25, 25, 25));

			pane.add(labelTheme, 0, 1);
			pane.add(theme, 1, 1);
			
			pane.add(labelLanguage, 0, 2);
			pane.add(languageChoiceBox, 1, 2);

			pane.add(labelQuicksand, 0, 3);
			pane.add(checkBoxFont, 1, 3);

			pane.add(lineBreak, 0, 4);

			pane.add(button1, 0, 5);
			pane.add(button2, 1, 5);

			dialogVbox.getChildren().add(pane);

			Scene preferencesDialogScene = new Scene(dialogVbox, 600, 400);
			preferencesDialog.setScene(preferencesDialogScene);

			String cssPreferences = getClass().getResource("css.css").toExternalForm();
			preferencesDialogScene.getStylesheets().addAll(cssPreferences);

			preferencesDialog.setTitle("Preferences");
			Image preferencesIcon = new Image("file:logo.png");
			preferencesDialog.getIcons().add(preferencesIcon);

			preferencesDialog.show();

		});
		
		
		// TODO I'm wondering if we shouldn't move the logic somewhere else? We are quite bloating the constructor there? Maybe we could move that into GUIMenuBar? Maybe methods instead of lambdas?
		// TODO the problem is that these buttons are using a lot of methods/attributes in this class... =/, if we move them in GUIMenuBar we have to give access it to GUI (his parent)
		// THE ACTION EVENTS
		theGUIMenuBar.getEnterExecutionModeMenuItem().setOnAction((ActionEvent actionEvent) -> {
			String programString = codingTextArea.getText();
			if(programString.length() != 0){
				try {
					theArmSimulator.setProgramString(programString);
					enterExecutionMode(programString);
				} catch (AssemblyException e1) {
					System.out.println(e1.toString());
				}
			}
		});
		theGUIMenuBar.getExitExecutionModeMenuItem().setOnAction((ActionEvent actionEvent) -> exitExecutionMode());
		theGUIMenuBar.getRunMenuItem().setOnAction((ActionEvent actionEvent) -> {
			if (executionMode.get() && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);
					int counter = 0;
					this.theArmSimulator.interruptExecutionFlow(false);
					while(!this.theArmSimulator.run()) {
						if (counter >= 1) {
							this.updateGUIfromThread();
							counter = 0;
						}
						counter++;
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							break;
						}
					}

					this.updateGUIfromThread();

					this.running.set(false);
				}).start();
			}
		});
		theGUIMenuBar.getRunSingleMenuItem().setOnAction((ActionEvent actionEvent) -> {
			if (executionMode.get() && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);

					theArmSimulator.runStep();

					this.updateGUIfromThread();

					this.running.set(false);
				}).start();
			}
		});
		theGUIMenuBar.getStopMenuItem().setOnAction((ActionEvent actionEvent) -> theArmSimulator.interruptExecutionFlow(true));
		theGUIMenuBar.getReloadProgramMenuItem().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getEnterExecutionModeMenuItem().fire());
		theGUIMenuBar.getOpenMenuItem().setOnAction((ActionEvent actionEvent) -> {
			FileChooser fileChooser = new FileChooser();
			if (lastFilePath != null) {
				fileChooser.setInitialDirectory(new File(new File(lastFilePath).getParent()));
			}

			fileChooser.setInitialFileName("test");
			fileChooser.setTitle("Open a source File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("#@rm Files", "*.S"));

			String path = fileChooser.showOpenDialog(stage).getAbsolutePath();
			if (path != null) {
				try {
					codingTextArea.setText(new String(Files.readAllBytes(Paths.get(path)), "UTF-8"));
					programFilePath = new File(path);
					lastFilePath = path;
					stage.setTitle("#@RM - " + programFilePath.getName());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		theGUIMenuBar.getSaveAsMenuItem().setOnAction((ActionEvent actionEvent) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save assembly program");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("#@rm Files", "*.S"));
			File chosenFile = fileChooser.showSaveDialog(stage);
			saveFile(codingTextArea.getText(), chosenFile);
		});
		theGUIMenuBar.getSaveMenuItem().setOnAction((ActionEvent actionEvent) -> {
			if (programFilePath != null) {
				saveFile(codingTextArea.getText(), programFilePath);
			} else {
				theGUIMenuBar.getSaveAsMenuItem().fire();
			}
		});
		theGUIMenuBar.getNewMenuItem().setOnAction((ActionEvent actionEvent) -> {
			Stage confirmBox = new Stage();
			confirmBox.initModality(Modality.APPLICATION_MODAL);
			confirmBox.initOwner(stage);
			VBox dialogVbox = new VBox(20);
			Label exitLabel = new Label("All unsaved work will be lost");

			Button yesBtn = new Button("Yes");

			yesBtn.setOnAction((ActionEvent arg0) -> {
				confirmBox.close();
				codingTextArea.setText("");
				programFilePath = null;
			});
			Button noBtn = new Button("No");

			noBtn.setOnAction((ActionEvent arg0) -> confirmBox.close());

			HBox hBox = new HBox();
			hBox.getChildren().addAll(yesBtn, noBtn);
			VBox vBox = new VBox();
			vBox.getChildren().addAll(exitLabel, hBox);

			confirmBox.setScene(new Scene(vBox));
			confirmBox.show();
		});
		theGUIMenuBar.getHelpMenuItem().setOnAction((ActionEvent actionEvent) -> {
			final Stage helpPopup = new Stage();
			helpPopup.initModality(Modality.APPLICATION_MODAL);
			helpPopup.initOwner(stage);

			VBox dialogVbox = new VBox(20);
			dialogVbox.getChildren().add(new Text("This is very helpful help wow"));
			Scene dialogScene = new Scene(dialogVbox, 300, 200);

			helpPopup.setScene(dialogScene);
			helpPopup.show();
		});
		theGUIMenuBar.getDocumentationMenuItem().setOnAction((ActionEvent actionEvent) -> showDocumentation());

		theGUIButtonBar.getExececutionModeButton().setOnAction((ActionEvent actionEvent) -> {
			if(this.executionMode.get()){
				theGUIMenuBar.getExitExecutionModeMenuItem().fire();
			}
			else{
				theGUIMenuBar.getEnterExecutionModeMenuItem().fire();
			}
		});
		theGUIButtonBar.getNewFileButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getNewMenuItem().fire());
		theGUIButtonBar.getSaveButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getSaveMenuItem().fire());
		theGUIButtonBar.getReloadButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getReloadProgramMenuItem().fire());
		theGUIButtonBar.getRunButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getRunMenuItem().fire());
		theGUIButtonBar.getRunSingleButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getRunSingleMenuItem().fire());
		theGUIButtonBar.getStopButton().setOnAction((ActionEvent actionEvent) -> theGUIMenuBar.getStopMenuItem().fire());

		

		// Several keyboard shortcut
		scene.setOnKeyPressed((KeyEvent ke) -> handleKeyboardEvent(ke));

	}
	
	private void handleKeyboardEvent(KeyEvent ke) {
		// TODO Maybe we should make them static or in a enum or something to avoid allocating new objects every time someone push a key on the keyboard?
		
		final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlShiftS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
				KeyCombination.SHIFT_DOWN);
		final KeyCombination ctrlO = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlP = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
		final KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
		final KeyCombination f5 = new KeyCodeCombination(KeyCode.F5);
		final KeyCombination f11 = new KeyCodeCombination(KeyCode.F11);
		final KeyCombination ctrlE = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);

		if (ctrlS.match(ke)) {
			theGUIMenuBar.getSaveMenuItem().fire();
		}
		if (ctrlShiftS.match(ke)) {
			theGUIMenuBar.getSaveAsMenuItem().fire();
		}
		if (ctrlO.match(ke)) {
			theGUIMenuBar.getOpenMenuItem().fire();
		}
		if (ctrlP.match(ke)) {
			theGUIMenuBar.getPreferencesMenuItem().fire();
		}
		if (ctrlN.match(ke)) {
			theGUIMenuBar.getNewMenuItem().fire();
		}
		if (f5.match(ke)) {
			theGUIMenuBar.getRunMenuItem().fire();
		}
		if (ctrlE.match(ke)) {
			if (executionMode.get()) {
				theGUIMenuBar.getExitExecutionModeMenuItem().fire();
			} else {
				theGUIMenuBar.getEnterExecutionModeMenuItem().fire();
			}
		}
		if (f11.match(ke)) {
			theGUIMenuBar.getRunSingleMenuItem().fire();
		}
	}

	private void highlightCurrentLine() {
		for (int i = 0; i < instructionsAsText.size(); i++) {
			instructionsAsText.get(i).setFill(Color.BLACK);
		}
		if (!theArmSimulator.hasFinished()) {
			try {
				instructionsAsText.get(this.theArmSimulator.getCurrentLine()).setFill(themeColor);
			}
			catch (IndexOutOfBoundsException e) {}
		}
	}

	private void exitExecutionMode() {
		this.theArmSimulator.interruptExecutionFlow(true);
		this.executionMode.set(false);
		this.codingTextArea.setEditable(true);

		this.centralBorderPaneContainer.setCenter(codingTextArea);

		this.theGUIMenuBar.exitExecMode();
		this.theGUIButtonBar.exitExecMode();
	}

	private void enterExecutionMode(String program){

		this.executionMode.set(true);

		this.theGUIMenuBar.setExecMode();
		this.theGUIButtonBar.setExecMode();

		this.codingTextArea.setEditable(false);

		this.centralBorderPaneContainer.setCenter(this.executionModeTextFlow);
		this.executionModeTextFlow.getChildren().clear();

		String[] instructionsAsStrings = program.split("\\r?\\n");
		this.instructionsAsText = new ArrayList<Text>();

		for (int lineNumber = 1; lineNumber <= instructionsAsStrings.length; lineNumber++) {
			String line = lineNumber + "\t" + instructionsAsStrings[lineNumber-1] + '\n';
			this.instructionsAsText.add(new Text(line));
			this.executionModeTextFlow.getChildren().add(this.instructionsAsText.get(lineNumber-1));
		}
		highlightCurrentLine();
		updateGUI();
	}

	private void updateGUI() {
		theGUIMemoryView.updateMemoryView();
		theGUIRegisterView.updateRegisters();
		consoleScrollPane.setVvalue(consoleScrollPane.getHmax());
	}

	private void updateGUIfromThread() {
		Platform.runLater(() -> {
			highlightCurrentLine();
			updateGUI();
			stage.show();
		});
	}
	
	private void saveFile(String content, File theFile) {
		try (FileWriter outputStream = new FileWriter(theFile)) {
			outputStream.write(content);
			this.programFilePath = theFile;
			stage.setTitle("#@RM - " + theFile.getName());
		} catch (IOException | NullPointerException e) {

		}
	}

	private void showDocumentation() {
		Stage docWindow = new Stage();

		docWindow.initModality(Modality.APPLICATION_MODAL);
		docWindow.initOwner(stage);
		docWindow.setTitle("Documentation");

		Parent container;
		Scene docScene;

		// TODO To remove once documentation.fxml is made
		try {
			container = FXMLLoader.load(getClass().getResource("documentation.fxml"));
			docScene = new Scene(container, 100, 100);
			docWindow.setScene(docScene);
			docWindow.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void applyTheme() {

		String currentTheme = prefs.get("THEME", "");

		scene.getStylesheets().clear();
		
		String css = getClass().getResource("css.css").toExternalForm();
		scene.getStylesheets().addAll(css);

		if (!themes.contains(currentTheme)) {
			currentTheme = GUI.DEFAULT_THEME;
		}

		css = getClass().getResource(currentTheme + ".css").toExternalForm();

		scene.getStylesheets().addAll(css);

		switch(currentTheme) {
		case "green":
			themeColor = Color.SEAGREEN;
			break;
		case "blue":
			themeColor = Color.BLUE;
			break;
		case "red":
		default:
			themeColor = Color.RED;
		}
		
		if (prefs.getBoolean("FONT", true)) {
			this.root.setStyle("-fx-font-family: \"Quicksand\"; -fx-font-size: 16px;");
		} else {
			this.root.setStyle("-fx-font-family: ''; -fx-font-size: 16px;");
		}
	
	}

	
}
