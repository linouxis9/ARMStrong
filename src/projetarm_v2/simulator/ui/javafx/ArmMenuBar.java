package projetarm_v2.simulator.ui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

public class ArmMenuBar {
    private MenuBar menuBar;
    private Boolean isRunningMode;

    public ArmMenuBar(ArmSimulator simulator, DockPane dockPane, Boolean isRunningMode){
        this.isRunningMode = isRunningMode;
        final Menu fileMenu = new Menu("File");
        final Menu windowMenu = new Menu("Window");
        final Menu editMenu = new Menu("Edit");
        final Menu runMenu = new Menu("Run");
        final Menu helpMenu = new Menu("Help");

        fileMenu.getItems().add(new MenuItem("New"));
        fileMenu.getItems().add(new MenuItem("Open File..."));
        fileMenu.getItems().add(new MenuItem("Save As..."));
        final MenuItem exitMenu = new MenuItem("Exit");
        fileMenu.getItems().add(exitMenu);

        final Menu newMenu = new Menu("New Window");

        final MenuItem newMemoryWindow = new MenuItem("Memory");
        final MenuItem newRegisterWindow = new MenuItem("Registers");

        newMenu.getItems().addAll(newMemoryWindow, newRegisterWindow);

        windowMenu.getItems().add(newMenu);
        windowMenu.getItems().add(new MenuItem("Preferences"));

        final MenuItem runMenuItem = new MenuItem("Run");
        runMenu.getItems().add(runMenuItem);
        runMenu.getItems().add(new MenuItem("Run Step by Step"));

        helpMenu.getItems().add(new MenuItem("About"));

        this.menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, windowMenu, editMenu, runMenu, helpMenu);

        newMemoryWindow.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RamView ramView = new RamView();
                ramView.getNode().dock(dockPane, DockPos.RIGHT);

            }
        });

        newRegisterWindow.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                RegistersView registerView = new RegistersView();
                registerView.getNode().dock(dockPane, DockPos.CENTER);
            }
        });

        exitMenu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //primaryStage.close();
            }
        });

        runMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulator.run();
            }
        });

    }

    public Node getNode(){
        return this.menuBar;
    }

}
