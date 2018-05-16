import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GUIMenuBar {

    private List<MenuItem> menusToDisableInExecMode;
    private MenuBar theMenuBar;

    private MenuItem newMenuItem;
    private MenuItem openMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem saveAsMenuItem;
    private MenuItem exitMenuItem;
    private MenuItem enterExecutionModeMenuItem;
    private MenuItem exitExecutionModeMenuItem;
    private MenuItem runMenuItem;
    private MenuItem runSingleMenuItem;

    public GUIMenuBar(MenuBar aMenuBar){

        theMenuBar = aMenuBar;

        menusToDisableInExecMode = new ArrayList<MenuItem>();

        Menu fileMenu 	= new Menu("File");
        Menu editMenu 	= new Menu("Edit");
        Menu runMenu 	= new Menu("Run");
        Menu helpMenu 	= new Menu("Help");

        newMenuItem 	= new MenuItem("New");
        openMenuItem 	= new MenuItem("Open file");
        saveMenuItem 	= new MenuItem("Save");
        saveAsMenuItem  = new MenuItem("Save as");
        exitMenuItem 	= new MenuItem("Exit");

        enterExecutionModeMenuItem = new MenuItem("Enter in execution mode");
        exitExecutionModeMenuItem 	= new MenuItem("Exit the execution mode");
        runMenuItem 				= new MenuItem("Run");
        runSingleMenuItem 			= new MenuItem("Run a single instruction");

        theMenuBar.getMenus().addAll(fileMenu, editMenu, runMenu, helpMenu);

        fileMenu.getItems().addAll(newMenuItem,openMenuItem,saveMenuItem, saveAsMenuItem, exitMenuItem);
        editMenu.getItems().addAll();
        runMenu.getItems().addAll(enterExecutionModeMenuItem,exitExecutionModeMenuItem, runMenuItem, runSingleMenuItem);
        helpMenu.getItems().addAll();

        menusToDisableInExecMode.add(newMenuItem);
        menusToDisableInExecMode.add(openMenuItem);
        menusToDisableInExecMode.add(saveMenuItem);
        menusToDisableInExecMode.add(saveAsMenuItem);
        menusToDisableInExecMode.add(enterExecutionModeMenuItem);

        //Button runAllButton = (Button) scene.lookup("#"); //TODO add an id
        //Button runStepByStepButton = (Button) scene.lookup("#"); //TODO add an id

        exitExecMode();

    }


    public void setExecMode(){
        for(int item = 0; item < menusToDisableInExecMode.size(); item++){
            menusToDisableInExecMode.get(item).setDisable(true);
        }
        exitExecutionModeMenuItem.setDisable(false);
        runMenuItem.setDisable(false);
        runSingleMenuItem.setDisable(false);
    }

    public void exitExecMode(){
        for(int item = 0; item < menusToDisableInExecMode.size(); item++){
            menusToDisableInExecMode.get(item).setDisable(false);
        }
        exitExecutionModeMenuItem.setDisable(true);
        runMenuItem.setDisable(true);
        runSingleMenuItem.setDisable(true);
    }



    public MenuItem getNewMenuItem() {
        return newMenuItem;
    }

    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public MenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public MenuItem getSaveAsMenuItem() {
        return saveAsMenuItem;
    }

    public MenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public MenuItem getEnterExecutionModeMenuItem() {
        return enterExecutionModeMenuItem;
    }

    public MenuItem getExitExecutionModeMenuItem() {
        return exitExecutionModeMenuItem;
    }

    public MenuItem getRunMenuItem() {
        return runMenuItem;
    }

    public MenuItem getRunSingleMenuItem() {
        return runSingleMenuItem;
    }
}
