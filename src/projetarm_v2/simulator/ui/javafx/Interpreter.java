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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dockfx.DockNode;

public class Interpreter {

	private AnchorPane mainPane;
	private DockNode dockNode;
	private Image dockImage;
	private TextFlow textFlow;
	private TextField textField;

	private List<String> asm;
	
	OutputStream output;
	
	public Interpreter() {
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/resources/ConsoleView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.asm = new ArrayList<>();
		
		this.dockNode = new DockNode(mainPane, "Interpreter", new ImageView(dockImage));

		this.mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.dockNode.getStylesheets().add("/resources/style.css");
		ScrollPane scrollPane = (ScrollPane) mainPane.lookup("#scrollPane");

		this.textField = new TextField();
		this.textField.setId("consoleInput");
		this.mainPane.getChildren().add(this.textField);
		AnchorPane.setBottomAnchor(this.textField, (double)0);
		AnchorPane.setLeftAnchor(this.textField, (double)23);
		AnchorPane.setRightAnchor(this.textField, (double)0);


		this.textFlow = new TextFlow();
		this.textFlow.setPadding(new Insets(5));
		this.textFlow.setId("textConsole");

		scrollPane.setContent(this.textFlow);

		output = new OutputStream() {
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
	}

	public void add(String asm) {
		if (!asm.isBlank())
			this.asm.add(asm);
	}
	
	public List<String> getAsm() {
		return Collections.unmodifiableList(this.asm);
	}
	
	public void pop() {
		this.asm.remove(this.asm.size() - 1);
	}
	
	public void redirectToInterpreter() {
		System.setOut(new PrintStream(output));
	}
	
	public DockNode getNode() {
		return dockNode;
	}
	
	public TextField getTextField() {
		return (this.textField);
	}
}
