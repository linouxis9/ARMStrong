package projetarm_v2.simulator.ui.javafx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;

public class ConsoleView {

	private AnchorPane mainPane;
	private DockNode dockNode;
	private Image dockImage;
	private TextFlow textFlow;
	private TextField textField;

	public ConsoleView() {
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/resources/ConsoleView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.dockNode = new DockNode(mainPane, "Console", new ImageView(dockImage));
		this.dockNode.setPrefSize(1000, 1500);
		this.dockNode.setClosable(false);

		this.mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.dockNode.getStylesheets().add("/resources/style.css");
		ScrollPane scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");

		this.textField = new TextField();
		this.mainPane.getChildren().add(this.textField);
		this.mainPane.setBottomAnchor(this.textField, (double)0);
		this.mainPane.setLeftAnchor(this.textField, (double)23);
		this.mainPane.setRightAnchor(this.textField, (double)0);


		this.textFlow = new TextFlow();
		this.textFlow.setPadding(new Insets(5));

		scrollPane.setContent(this.textFlow);

		OutputStream output = new OutputStream() {
			private StringBuffer currentLine = new StringBuffer();
			@Override
			public void write(int b) throws IOException {
				this.currentLine.append((char)b);
				if (b == '\n') {
					Platform.runLater(() -> {
						textFlow.getChildren().add(new Text(currentLine.toString()));
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

	public TextField getTextField() {
		return (this.textField);
	}
}
