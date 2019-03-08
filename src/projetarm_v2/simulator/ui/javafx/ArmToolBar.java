package projetarm_v2.simulator.ui.javafx;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.HashSet;

public class ArmToolBar {

    private ToolBar toolBar;

    private ArmSimulator simulator;
    private CodeEditor codeEditor;

    private Button switchButton;
    private Button runButton;
    private Button stepByStepButton;
    private Button reloadButton;
    private Button stopButton;

    private HashSet<Button> disableInEdition;

    public ArmToolBar(ArmSimulator armSimulator, CodeEditor codeEditor){
        this.codeEditor = codeEditor;
        this.simulator = armSimulator;

        this.switchButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/switch.png").toExternalForm())));
        this.switchButton.setTooltip(new Tooltip("Switch between editor and simulation mode [CTRL-E]"));
        
        this.runButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/run.png").toExternalForm())));
        this.runButton.setTooltip(new Tooltip("Run all at once [F5]"));

        this.stepByStepButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/runByStep.png").toExternalForm())));
        this.stepByStepButton.setTooltip(new Tooltip("Run a single instruction [F11]"));
        
        this.reloadButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/reload.png").toExternalForm())));
        this.reloadButton.setTooltip(new Tooltip("Reset CPU [CTRL-R]"));
   
        this.stopButton = new Button("", new ImageView(new Image(getClass().getResource("/resources/stop.png").toExternalForm())));
        this.stopButton.setTooltip(new Tooltip("Stop simulation"));

        
        this.toolBar = new ToolBar(switchButton, new Separator(), runButton, stepByStepButton, reloadButton, stopButton);
        
        this.switchButton.getStyleClass().add("buttonToolBar");
        this.runButton.getStyleClass().add("buttonToolBar");
        this.stepByStepButton.getStyleClass().add("buttonToolBar");
        this.reloadButton.getStyleClass().add("buttonToolBar");
        this.stopButton.getStyleClass().add("buttonToolBar");
        
        this.disableInEdition = new HashSet<>();
        disableInEdition.add(runButton);
        disableInEdition.add(stepByStepButton);
        disableInEdition.add(reloadButton);
        disableInEdition.add(stopButton);
    }

    public void setExecutionMode(boolean executionMode){
        for (Button item : this.disableInEdition){
            item.setDisable(!executionMode);
        }
    }

    public Button getSwitchButton() {
        return switchButton;
    }

    public Button getReloadButton() {
        return reloadButton;
    }

    public Button getRunButton() {
        return runButton;
    }

    public Button getStepByStepButton() {
        return stepByStepButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Node getNode(){
        return this.toolBar;
    }
}
