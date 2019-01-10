package projetarm_v2.simulator.ui.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;

public class Gui extends Application {

    private ArrayList<RegistersView> registersViews;
    private ArrayList<RamView> RamViews;
    private CodeEditor codeEditor;
    private ConsoleView consoleView;

    private ArmMenuBar armMenuBar;
    private ArmToolBar armToolBar;
    private DockPane dockPane;
    
    private ArmSimulator simulator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.simulator = new ArmSimulator();

        primaryStage.setTitle("ARMStrong");
        Image applicationIcon = new Image("file:logo.png");
        primaryStage.getIcons().add(applicationIcon);

        // create a dock pane that will manage our dock nodes and handle the layout
        this.dockPane = new DockPane();

        // load an image to caption the dock nodes
        //Image dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());

        //creating the nodes
        this.registersViews = new ArrayList<>();
        this.RamViews = new ArrayList<>();

        this.registersViews.add(new RegistersView(this.simulator));
        this.registersViews.get(0).getNode().dock(dockPane, DockPos.LEFT);

        this.codeEditor = new CodeEditor();
        this.codeEditor.getNode().dock(dockPane, DockPos.LEFT);

        this.RamViews.add(new RamView());
        this.RamViews.get(0).getNode().dock(dockPane, DockPos.RIGHT);

        this.consoleView = new ConsoleView();
        this.consoleView.getNode().dock(dockPane, DockPos.BOTTOM);

        this.armMenuBar = new ArmMenuBar(simulator, dockPane, codeEditor);
        this.armToolBar = new ArmToolBar(simulator,codeEditor);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(this.armMenuBar.getNode(), this.armToolBar.getNode(), dockPane);
        VBox.setVgrow(dockPane, Priority.ALWAYS);

        setButtonEvents();

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

        // TODO: after this feel free to apply your own global stylesheet using the StyleManager class
    }

    private void setButtonEvents(){
        this.armToolBar.getReloadButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //

            }
        });

        this.armMenuBar.getNewMemoryWindow().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RamView moreRamView = new RamView();
                moreRamView.getNode().dock(dockPane, DockPos.RIGHT);
                consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
            }
        });

        this.armMenuBar.getNewRegistersWindow().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RegistersView moreRegistersView = new RegistersView(simulator);
                moreRegistersView.getNode().dock(dockPane, DockPos.LEFT);
                consoleView.getNode().dock(dockPane, DockPos.BOTTOM);
            }
        });
        //this.armToolBar.getSwitchButton()

    }

}
