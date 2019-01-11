package projetarm_v2.simulator.ui.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dockfx.DockPane;
import org.dockfx.DockPos;

import java.util.ArrayList;

import projetarm_v2.simulator.boilerplate.ArmSimulator;

public class Gui extends Application {

    private ArrayList<RegistersView> registersViews;
    private ArrayList<RamView> RamViews;
    private CodeEditor codeEditor;
    private ConsoleView consoleView;

    private ArmMenuBar armMenuBar;
    private ArmToolBar armToolBar;
    private DockPane dockPane;

    private boolean executionMode;
    
    private ArmSimulator simulator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.simulator = new ArmSimulator();
        this.executionMode = false;

        primaryStage.setTitle("#@RMStrong");
        Image applicationIcon = new Image("file:logo.png");
        primaryStage.getIcons().add(applicationIcon);

        // create a dock pane that will manage our dock nodes and handle the layout
        this.dockPane = new DockPane();

        // load an image to caption the dock nodes
        //Image dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());

        //creating the nodes
        this.registersViews = new ArrayList<>();
        this.RamViews = new ArrayList<>();

        this.codeEditor = new CodeEditor();
        this.codeEditor.getNode().dock(dockPane, DockPos.LEFT);

        this.registersViews.add(new RegistersView(this.simulator));
        this.registersViews.get(0).getNode().dock(dockPane, DockPos.LEFT);

        this.RamViews.add(new RamView());
        this.RamViews.get(0).getNode().dock(dockPane, DockPos.RIGHT);

        this.consoleView = new ConsoleView();
        this.consoleView.getNode().dock(dockPane, DockPos.BOTTOM);

        this.armMenuBar = new ArmMenuBar(simulator,codeEditor);
        this.armToolBar = new ArmToolBar(simulator,codeEditor);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(this.armMenuBar.getNode(), this.armToolBar.getNode(), dockPane);
        VBox.setVgrow(dockPane, Priority.ALWAYS);

        setButtonEvents();

        this.codeEditor.setExecutionMode(this.executionMode);
        this.armMenuBar.setExecutionMode(this.executionMode);
        this.armToolBar.setExecutionMode(this.executionMode);

        primaryStage.setScene(new Scene(vbox, 800, 500));
        primaryStage.sizeToScene();

        primaryStage.show();

        // test the look and feel with both Caspian and Modena
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        
        // initialize the default styles for the dock pane and undocked nodes using the DockFX
        // library's internal Default.css stylesheet
        // unlike other custom control libraries this allows the user to override them globally
        // using the style manager just as they can with internal JavaFX controls
        // this must be called after the primary stage is shown
        // https://bugs.openjdk.java.net/browse/JDK-8132900
        DockPane.initializeDefaultUserAgentStylesheet();
    }

    private void setButtonEvents(){

        //the window "items"
        this.armMenuBar.getNewMemoryWindow().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RamView moreRamView = new RamView();
                moreRamView.getNode().dock(dockPane, DockPos.RIGHT);
                //consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
            }
        });
        this.armMenuBar.getNewRegistersWindow().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RegistersView moreRegistersView = new RegistersView(simulator);
                moreRegistersView.getNode().dock(dockPane, DockPos.LEFT);
                //consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
            }
        });
        this.armMenuBar.getNewLedGame().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LedView moreLedView = new LedView();
                moreLedView.getNode().dock(dockPane, DockPos.RIGHT);
                //consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
            }
        });

        //the "Run" items
        this.armMenuBar.getSwichMode().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                executionMode = !executionMode;
                if(executionMode){
                    try {
                        simulator.setProgram(codeEditor.getProgramAsSting());
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                        executionMode=!executionMode;
                    }
                }
                codeEditor.setExecutionMode(executionMode);
                armMenuBar.setExecutionMode(executionMode);
                armToolBar.setExecutionMode(executionMode);


            }
        });
        this.armMenuBar.getReloadMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

            }
        });

        //the toolbar buttons
        this.armToolBar.getSwitchButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armMenuBar.getSwichMode().fire();
            }
        });
        this.armToolBar.getReloadButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armMenuBar.getReloadMenuItem().fire();
            }
        });
        this.armToolBar.getRunButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.run();
            }
        });
        this.armToolBar.getStepByStepButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.runStep();
            }
        });
        this.armToolBar.getStopButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.interruptExecutionFlow();
            }
        });
    }

}
