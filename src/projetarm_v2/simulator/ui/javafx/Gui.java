package projetarm_v2.simulator.ui.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.dockfx.DockPane;
import org.dockfx.DockPos;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import projetarm_v2.simulator.boilerplate.ArmSimulator;

public class Gui extends Application {

	private ArrayList<RegistersView> registersViews;
	private ArrayList<RamView> ramViews;
	private CodeEditor codeEditor;
	private ConsoleView consoleView;

	private ArmMenuBar armMenuBar;
	private ArmToolBar armToolBar;
	private DockPane dockPane;

	private boolean executionMode;

	private ArmSimulator simulator;
	private AtomicBoolean running;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.simulator = new ArmSimulator();
		this.executionMode = false;
		this.running = new AtomicBoolean(false);

		primaryStage.setTitle("#@RMStrong");
		Image applicationIcon = new Image("file:logo.png");
		primaryStage.getIcons().add(applicationIcon);

		// create a dock pane that will manage our dock nodes and handle the layout
		this.dockPane = new DockPane();

		// load an image to caption the dock nodes
		// Image dockImage = new
		// Image(Gui.class.getResource("docknode.png").toExternalForm());

		// creating the nodes
		this.registersViews = new ArrayList<>();
		this.ramViews = new ArrayList<>();

		this.codeEditor = new CodeEditor();
		this.codeEditor.getNode().dock(dockPane, DockPos.LEFT);

		this.registersViews.add(new RegistersView(this.simulator));
		this.registersViews.get(0).getNode().dock(dockPane, DockPos.LEFT);

		this.ramViews.add(new RamView(simulator));
		this.ramViews.get(0).getNode().dock(dockPane, DockPos.RIGHT);

		this.consoleView = new ConsoleView();
		this.consoleView.getNode().dock(dockPane, DockPos.BOTTOM);

		this.armMenuBar = new ArmMenuBar(simulator, codeEditor, primaryStage, this);
		this.armToolBar = new ArmToolBar(simulator, codeEditor);

		VBox vbox = new VBox();
		vbox.getChildren().addAll(this.armMenuBar.getNode(), this.armToolBar.getNode(), dockPane);
		VBox.setVgrow(dockPane, Priority.ALWAYS);

		//primaryStage.show(); // render to avoid node.lookup() to fail
		setButtonEvents();

		this.codeEditor.setExecutionMode(this.executionMode);
		this.armMenuBar.setExecutionMode(this.executionMode);
		this.armToolBar.setExecutionMode(this.executionMode);

		primaryStage.setScene(new Scene(vbox, 800, 500));
		primaryStage.sizeToScene();

		// test the look and feel with both Caspian and Modena
		Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

		newUpdateThread(this.running);
		
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            	System.exit(0);
            }
        });

		// initialize the default styles for the dock pane and undocked nodes using the
		// DockFX
		// library's internal Default.css stylesheet
		// unlike other custom control libraries this allows the user to override them
		// globally
		// using the style manager just as they can with internal JavaFX controls
		// this must be called after the primary stage is shown
		// https://bugs.openjdk.java.net/browse/JDK-8132900
		DockPane.initializeDefaultUserAgentStylesheet();
	}

	private void setButtonEvents() {

		// the window "items"
		this.armMenuBar.getNewMemoryWindow().setOnAction(actionEvent -> {
			RamView moreRamView = new RamView(simulator);
			this.ramViews.add(moreRamView);
			moreRamView.getNode().dock(dockPane, DockPos.RIGHT);
		});
		this.armMenuBar.getNewRegistersWindow().setOnAction(actionEvent -> {
			RegistersView moreRegistersView = new RegistersView(simulator);
			this.registersViews.add(moreRegistersView);
			moreRegistersView.getNode().dock(dockPane, DockPos.LEFT);
		});
		this.armMenuBar.getNewLedGame().setOnAction(actionEvent -> {
			LedView moreLedView = new LedView();
			//this.ledViews.add(moreLedView);
			moreLedView.getNode().dock(dockPane, DockPos.RIGHT);
		});

		// the "Run" items
		this.armMenuBar.getSwitchMode().setOnAction(actionEvent -> {
			if (!running.get()) {
				executionMode = !executionMode;
				if (executionMode) {
					try {
						simulator.setProgram(codeEditor.getProgramAsString());
					} catch (Exception e) {
						System.out.println(e.getMessage());
						executionMode = !executionMode;
					}
				}
				codeEditor.setExecutionMode(executionMode);
				armMenuBar.setExecutionMode(executionMode);
				armToolBar.setExecutionMode(executionMode);
			}
		});
		this.armMenuBar.getReloadMenuItem().setOnAction(actionEvent -> {
			if (!running.get()) {
				this.simulator.resetState();
				this.simulator.setProgram(codeEditor.getProgramAsString());
				updateUI();
			}
		});

		// the toolbar buttons
		this.armToolBar.getSwitchButton().setOnAction(actionEvent -> armMenuBar.getSwitchMode().fire());
		
		this.armToolBar.getReloadButton().setOnAction(actionEvent -> armMenuBar.getReloadMenuItem().fire());
		
		this.armToolBar.getRunButton().setOnAction(actionEvent -> {
			if (executionMode && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);
					
					this.simulator.run();
					
					this.running.set(false);
					
					updateUI();
				}).start();
			}
		});

		this.armToolBar.getStepByStepButton().setOnAction(actionEvent -> {
			if (executionMode && !(running.get())) {
				new Thread(() -> {
					this.running.set(true);

					this.simulator.runStep();

					this.running.set(false);
					
					updateUI();
				}).start();
			}
		});
		this.armToolBar.getStopButton().setOnAction(actionEvent -> {
			simulator.interruptExecutionFlow();
			updateUI();
			this.running.set(false);
		});

		// the console
		this.consoleView.getTextField().setOnKeyPressed((KeyEvent ke) -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				simulator.setConsoleInput(this.consoleView.getTextField().getText());
				this.consoleView.getTextField().clear();
			}
		});
	}

	private void updateUI() {
		Platform.runLater(() -> {
			for (RegistersView registerView : this.registersViews) {
				registerView.updateRegisters();
			}
			for (RamView ramView : this.ramViews) {
				ramView.updateContents();
			}
			
			this.codeEditor.highlightLine(this.simulator.getCurrentLine());
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
				
				updateUI();
			}
		}).start();
	}
	
}
