package simulator.ui.javafx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GUIButtonBar {

    private ToolBar theToolBar;

    private Button newFile;
    private Button save;

    private Button executionMode;

    private Button run;
    private Button runSingle;
    private Button reload;
    private Button stop;

    private List<Button> editMode;
    private List<Button> execMode;
    
    public GUIButtonBar(ToolBar aToolBar){
    	
        this.theToolBar = aToolBar;

        executionMode = new Button("", new ImageView(new Image(getClass().getResource("/resources/switch.png").toExternalForm())));

        run = new Button("", new ImageView(new Image(getClass().getResource("/resources/run.png").toExternalForm())));
        runSingle = new Button("", new ImageView(new Image(getClass().getResource("/resources/runByStep.png").toExternalForm())));
        reload = new Button("", new ImageView(new Image(getClass().getResource("/resources/reload.png").toExternalForm())));
        stop = new Button("", new ImageView(new Image(getClass().getResource("/resources/stop.png").toExternalForm())));

        save = new Button("", new ImageView(new Image(getClass().getResource("/resources/save.png").toExternalForm())));
        newFile = new Button("", new ImageView(new Image(getClass().getResource("/resources/newFile.png").toExternalForm())));

        editMode = new ArrayList<Button>();
        execMode = new ArrayList<Button>();

        editMode.add(save);
        editMode.add(newFile);
        editMode.add(executionMode);

        execMode.add(executionMode);
        execMode.add(run);
        execMode.add(runSingle);
        execMode.add(reload);
        execMode.add(stop);

        save.setTooltip(new Tooltip("Save (Ctrl+S)"));
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
        return save;
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
