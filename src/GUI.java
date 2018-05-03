 
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;


public class GUI extends Application {
	// TODO write javadoc comment
	/**
	 * 
	 */
	public void startIHM() {
		launch(null);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("ihm_#@rm.fxml"));
        Scene scene = new Scene(root, 700, 275);
        
        stage.setMaximized(true);
        
        stage.setMinHeight(800);
        stage.setMinWidth(800);

        
        stage.setTitle("#@RM");
        stage.setScene(scene);

	    OutputStream consoleOut = new OutputStream() {
	        @Override
	        public void write(int b) throws IOException {
	        	Platform.runLater(() ->{
	        	        TextFlow consoleTextFlow = (TextFlow) scene.lookup("#consoleTextFlow");
	        	        consoleTextFlow.getChildren().add(new Text((String.valueOf((char) b))));
	        	});
	        }
	    };
	    System.setOut(new PrintStream(consoleOut, true));
	    stage.show();
	}
}
