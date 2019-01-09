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
    private Boolean isRunningMode;
    private ArmSimulator simulator;
    private TextArea codeTextArea;


    public ArmToolBar(ArmSimulator armSimulator, TextArea codeTextArea, Boolean isRunningMode){
        this.isRunningMode = isRunningMode;
        this.simulator = armSimulator;
        this.codeTextArea = codeTextArea;

        Button switchButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/switch.png").toExternalForm())));
        Button runButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/run.png").toExternalForm())));
        Button stepByStepButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/runByStep.png").toExternalForm())));
        Button reloadButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/reload.png").toExternalForm())));
        Button stopButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/stop.png").toExternalForm())));
        this.toolBar = new ToolBar(switchButton, new Separator(), runButton, stepByStepButton, reloadButton, stopButton);

        switchButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                changeState();
            }
        });

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

        reloadButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armSimulator.resetState();
                armSimulator.setProgram(codeTextArea.getText());
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                armSimulator.interruptExecutionFlow();

            }
        });


    }

    private void changeState() {
        if(isRunningMode.booleanValue()){
            //exit
        }else{
            simulator.setProgram(this.codeTextArea.getText());
            isRunningMode = !isRunningMode.booleanValue();
        }
    }

    public Node getNode(){
        return this.toolBar;
    }
}
