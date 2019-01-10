package projetarm_v2.simulator.ui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

public class ArmToolBar {

    private ToolBar toolBar;

    private ArmSimulator simulator;
    private CodeEditor codeEditor;

    private Button switchButton;
    private Button runButton;
    private Button stepByStepButton;
    private Button reloadButton;
    private Button stopButton;



    public ArmToolBar(ArmSimulator armSimulator, CodeEditor codeEditor){
        this.codeEditor = codeEditor;
        this.simulator = armSimulator;

        this.switchButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/switch.png").toExternalForm())));
        this.runButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/run.png").toExternalForm())));
        this.stepByStepButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/runByStep.png").toExternalForm())));
        this.reloadButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/reload.png").toExternalForm())));
        this.stopButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/stop.png").toExternalForm())));
        this.toolBar = new ToolBar(switchButton, new Separator(), runButton, stepByStepButton, reloadButton, stopButton);

        runButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armSimulator.run();
            }
        });

        stepByStepButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armSimulator.runStep();
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armSimulator.interruptExecutionFlow();

            }
        });


    }

    public void setExecutionMode(boolean executionMode){
        //changer les boutons affichés
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public Button getReloadButton() {
        return reloadButton;
    }

    public Node getNode(){
        return this.toolBar;
    }
}
