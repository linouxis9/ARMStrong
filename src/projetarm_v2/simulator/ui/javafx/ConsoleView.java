package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;
import org.dockfx.DockPane;

public class ConsoleView {

	private ScrollPane scrollPane;
	private DockNode dockNode;
	private Image dockImage;
	private TextFlow textArea;

	public ConsoleView() {
		this.scrollPane = new ScrollPane();

		this.dockNode = new DockNode(scrollPane, "Console", new ImageView(dockImage));

		dockNode.setPrefSize(1000, 1500);
		dockNode.setClosable(false);

		this.textArea = new TextFlow();

		this.textArea.setPadding(new Insets(5));
		this.textArea.setStyle("-fx-line-spacing: -0.4em;");

		this.scrollPane.setFitToWidth(true);
		this.scrollPane.setFitToHeight(true);
		this.scrollPane.setContent(this.textArea);
		this.scrollPane.setHmin(dockNode.getHeight());
		
		OutputStream output = new OutputStream() {
			private StringBuffer currentLine = new StringBuffer();
			
			@Override
			public void write(int b) throws IOException {
				this.currentLine.append((char)b);
				if (b == '\n') {
					Platform.runLater(() -> {
						textArea.getChildren().add(new Text(currentLine.toString()));
						this.currentLine.setLength(0);
						scrollPane.setVvalue(scrollPane.getHmax());
					});
				}
			}
		};

		System.setOut(new PrintStream(output));
	}

	public DockNode getNode() {
		return dockNode;
	}

}
