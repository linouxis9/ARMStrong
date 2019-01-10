package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.text.TextFlow;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class ArmMenuBar {

    private MenuBar menuBar;

    private MenuItem newMemoryWindow;
    private MenuItem newRegistersWindow;

    private MenuItem swichMode;
    private MenuItem reloadMenuItem;

    private HashSet<MenuItem> disableInExecution;
    private HashSet<MenuItem> disableInEdition;



    public ArmMenuBar(ArmSimulator simulator, CodeEditor codeEditor){
        final Menu fileMenu = new Menu("File");
        final Menu windowMenu = new Menu("Window");
        final Menu editMenu = new Menu("Edit");
        final Menu runMenu = new Menu("Run");
        final Menu helpMenu = new Menu("Help");

        //FILE
        final MenuItem neW = new MenuItem("New");
        final MenuItem openFile = new MenuItem("Open File...");
        final MenuItem save = new MenuItem("Save");
        final MenuItem saveAs = new MenuItem("Save As...");
        final MenuItem exitMenu = new MenuItem("Exit");
        fileMenu.getItems().addAll(neW, openFile, new SeparatorMenuItem(), save, saveAs,new SeparatorMenuItem(), exitMenu);

        //WINDOW
        final Menu newMenu = new Menu("New Window");
        final MenuItem preferences = new MenuItem("Preferences");

        this.newMemoryWindow = new MenuItem("Memory");
        this.newRegistersWindow = new MenuItem("Registers");
        newMenu.getItems().addAll(this.newMemoryWindow, this.newRegistersWindow);

        windowMenu.getItems().addAll(newMenu, preferences);

        //RUN
        this.swichMode = new MenuItem("Switch Mode");
        final MenuItem runMenuItem = new MenuItem("Run");
        final MenuItem runStepMenuItem = new MenuItem("Run Step by Step");
        final MenuItem stopMenuItem = new MenuItem("Stop");
        reloadMenuItem = new MenuItem("Reload");
        runMenu.getItems().addAll(this.swichMode, runMenuItem, runStepMenuItem, stopMenuItem, reloadMenuItem);

        helpMenu.getItems().add(new MenuItem("About"));

        this.menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, windowMenu, editMenu, runMenu, helpMenu);

        disableInEdition = new HashSet<>();
        disableInExecution = new HashSet<>();

        disableInExecution.add(neW);
        disableInExecution.add(openFile);
        disableInExecution.add(save);
        disableInExecution.add(saveAs);

        disableInEdition.add(runMenuItem);
        disableInEdition.add(runStepMenuItem);
        disableInEdition.add(stopMenuItem);
        disableInEdition.add(reloadMenuItem);

        exitMenu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });

        runMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.run();
            }
        });
        runStepMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.runStep();
            }
        });
        stopMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.interruptExecutionFlow();
            }
        });

    }

    public MenuItem getReloadMenuItem() {
        return reloadMenuItem;
    }

    public MenuItem getSwichMode() {
        return swichMode;
    }

    public MenuItem getNewMemoryWindow() {
        return newMemoryWindow;
    }

    public MenuItem getNewRegistersWindow() {
        return newRegistersWindow;
    }

    public void setExecutionMode(boolean executionMode){
        Iterator<MenuItem> iterator = this.disableInEdition.iterator();
        while (iterator.hasNext()){
            iterator.next().setDisable(!executionMode);
        }

        iterator = this.disableInExecution.iterator();
        while (iterator.hasNext()){
            iterator.next().setDisable(executionMode);
        }
    }

    public Node getNode(){
        return this.menuBar;
    }

}
