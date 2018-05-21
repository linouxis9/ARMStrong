import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.tools.Tool;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUIButtonBar {

    ToolBar theToolBar;

    Button newFile;
    Button Save;

    Button exececutionMode;

    Button run;
    Button runSingle;
    Button reload;
    Button stop;

    List<Button> editMode;
    List<Button> execMode;

    public GUIButtonBar(ToolBar aToolBar){

        this.theToolBar = aToolBar;


        exececutionMode = new Button("(switchMode)", new ImageView(new Image("file:images/logoSmall.png")));

        run = new Button("", new ImageView(new Image("file:images/run.png")));
        runSingle = new Button("(run step)", new ImageView(new Image("file:images/logoSmall.png")));
        reload = new Button("(reload)", new ImageView(new Image("file:images/logoSmall.png")));
        stop = new Button("(stop)", new ImageView(new Image("file:images/logoSmall.png")));

        Save = new Button("", new ImageView(new Image("file:images/save.png")));
        newFile = new Button("(new file)", new ImageView(new Image("file:images/logoSmall.png")));

        editMode = new ArrayList<Button>();
        execMode = new ArrayList<Button>();

        editMode.add(Save);
        editMode.add(newFile);
        editMode.add(exececutionMode);

        execMode.add(run);
        execMode.add(runSingle);
        execMode.add(reload);
        execMode.add(exececutionMode);
        execMode.add(stop);

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
        return exececutionMode;
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
