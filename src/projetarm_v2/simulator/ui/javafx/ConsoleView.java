package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class ConsoleView {

    Pane mainPane;
    DockNode dockNode;
    Image dockImage;
    TextFlow textArea;
    ScrollPane consoleScrollPane;

    public ConsoleView(){
       // dockImage = new Image(Gui.class.getResource("docknode.png").toExternalForm());
        mainPane = new Pane();
 
        dockNode = new DockNode(mainPane, "Console", new ImageView(dockImage));
        dockNode.setPrefSize(1000,1500);      
        
        this.textArea = new TextFlow();
        
        this.textArea.setPadding(new Insets(5));
        
        this.consoleScrollPane = new ScrollPane();
        
        OutputStream output = new OutputStream(){
	        @Override
	        public void write(int b) throws IOException {
	        	Platform.runLater(()->textArea.getChildren().add(new Text(String.valueOf((char) b))));    
	        }
        };
        
        consoleScrollPane.setFitToWidth(true);
        consoleScrollPane.setFitToHeight(true);
        
        consoleScrollPane.setContent(this.textArea);    
        consoleScrollPane.setHmin(dockNode.getHeight());
        
        System.setOut(new PrintStream(output));
        
        dockNode.getChildren().add(this.consoleScrollPane);
        
    }
    
    public DockNode getNode(){
        return dockNode;
    }
   
}
