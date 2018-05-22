import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GUIButtonBar {

    private ToolBar theToolBar;

    private Button newFile;
    private Button Save;

    private Button executionMode;

    private Button run;
    private Button runSingle;
    private Button reload;
    private Button stop;

    private List<Button> editMode;
    private List<Button> execMode;

    public GUIButtonBar(ToolBar aToolBar){

        this.theToolBar = aToolBar;

        executionMode = new Button("", new ImageView(new Image("file:images/switch.png")));

        run = new Button("", new ImageView(new Image("file:images/run.png")));
        runSingle = new Button("", new ImageView(new Image("file:images/runByStep.png")));
        reload = new Button("", new ImageView(new Image("file:images/reload.png")));
        stop = new Button("", new ImageView(new Image("file:images/stop.png")));

        Save = new Button("", new ImageView(new Image("file:images/save.png")));
        newFile = new Button("", new ImageView(new Image("file:images/newFile.png")));

        editMode = new ArrayList<Button>();
        execMode = new ArrayList<Button>();

        editMode.add(Save);
        editMode.add(newFile);
        editMode.add(executionMode);

        execMode.add(executionMode);
        execMode.add(run);
        execMode.add(runSingle);
        execMode.add(reload);
        execMode.add(stop);

        Save.setTooltip(new Tooltip("Save (Ctrl+S)"));
        executionMode.setTooltip(new Tooltip("Switch Execution/Edit mode"));
        newFile.setTooltip(new Tooltip("New file"));
        
        exitExecMode();
    }

    public void setExecMode(){
        theToolBar.getItems().clear();
        for(int i=0; i<execMode.size(); i++){
            theToolBar.getItems().add(this.execMode.get(i));
        }
    }

    public void exitExecMode(){
        theToolBar.getItems().clear();
        for(int i=0; i<editMode.size(); i++){
            theToolBar.getItems().add(this.editMode.get(i));
        }
    }

    public Button getNewFileButton() {
        return newFile;
    }

    public Button getSaveButton() {
        return Save;
    }

    public Button getExececutionModeButton() {
        return executionMode;
    }

    public Button getRunButton() {
        return run;
    }

    public Button getRunSingleButton() {
        return runSingle;
    }

    public Button getReloadButton() {
        return reload;
    }

    public Button getStopButton() {
        return stop;
    }
}
