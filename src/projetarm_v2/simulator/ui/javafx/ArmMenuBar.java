package projetarm_v2.simulator.ui.javafx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import projetarm_v2.simulator.boilerplate.ArmSimulator;

import java.io.IOException;
import java.util.HashSet;

import org.dockfx.DockPane;

public class ArmMenuBar {

    private MenuBar menuBar;

    private MenuItem newMemoryWindow;
    private MenuItem newRegistersWindow;
    private MenuItem newLedGameWindow;

    private MenuItem switchMode;
    private MenuItem reloadMenuItem;

    private HashSet<MenuItem> disableInExecution;
    private HashSet<MenuItem> disableInEdition;

    private Stage primaryStage;

    public ArmMenuBar(ArmSimulator simulator, CodeEditor codeEditor, Stage stage, Application gui){
    	
    	this.primaryStage = stage;
    	
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
        this.newLedGameWindow = new MenuItem("Led Game");
        newMenu.getItems().addAll(this.newMemoryWindow, this.newRegistersWindow, this.newLedGameWindow);

        windowMenu.getItems().addAll(newMenu, preferences);

        //RUN
        this.switchMode = new MenuItem("Switch Mode");
        final MenuItem runMenuItem = new MenuItem("Run");
        final MenuItem runStepMenuItem = new MenuItem("Run Step by Step");
        final MenuItem stopMenuItem = new MenuItem("Stop");
        reloadMenuItem = new MenuItem("Reload");
        runMenu.getItems().addAll(this.switchMode, runMenuItem, runStepMenuItem, stopMenuItem, reloadMenuItem);

        final MenuItem aboutMenu = new MenuItem("About");
        helpMenu.getItems().add(aboutMenu);

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

        exitMenu.setOnAction(actionEvent -> Platform.exit());
        
        aboutMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	final Stage aboutPopUp = new Stage();
                aboutPopUp.setTitle("#@RMStrong");
                Image applicationIcon = new Image("file:logo.png");
                aboutPopUp.getIcons().add(applicationIcon);
                aboutPopUp.initOwner(primaryStage);
                aboutPopUp.getIcons().add(applicationIcon);
                aboutPopUp.setTitle("About - #@RMStrong");
                
				try {
					VBox main = FXMLLoader.load(getClass().getResource("/resources/aboutView.fxml"));
					aboutPopUp.setScene(new Scene(main, 500, 280));
					Hyperlink gitLink = (Hyperlink)main.lookup("#linkGit");
					gitLink.setOnAction(new EventHandler<ActionEvent>() {

		                @Override
		                public void handle(ActionEvent t) {
		                	HostServices services = gui.getHostServices();
		                	services.showDocument(gitLink.getText());
		                }
		            });
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
                aboutPopUp.show();
            }
        });
    }

    public MenuItem getReloadMenuItem() {
        return reloadMenuItem;
    }

    public MenuItem getSwitchMode() {
        return switchMode;
    }

    public MenuItem getNewMemoryWindow() {
        return newMemoryWindow;
    }

    public MenuItem getNewRegistersWindow() {
        return newRegistersWindow;
    }
    
    public MenuItem getNewLedGame() {
        return newLedGameWindow;
    }
    
    public void setExecutionMode(boolean executionMode){
        for (MenuItem item : this.disableInEdition){
            item.setDisable(!executionMode);
        }

        for (MenuItem item : this.disableInExecution){
            item.setDisable(executionMode);
        }
    }

    public Node getNode(){
        return this.menuBar;
    }

}
