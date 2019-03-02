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
import projetarm_v2.simulator.boilerplate.ArmSimulator;
import projetarm_v2.simulator.boilerplate.InvalidInstructionException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.dockfx.DockNode;

public class Interpreter {

	private AnchorPane mainPane;
	private DockNode dockNode;
	private Image dockImage;
	private TextFlow textFlow;
	private TextField textField;
	
	OutputStream output;
	
	private long pc;
	
	private ArmSimulator simulator;
	
	public Interpreter(ArmSimulator simulator) {
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/resources/ConsoleView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.simulator = simulator;
		
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
	
	public void initialize() {
		this.redirectToInterpreter();
		this.pc = this.simulator.getCpu().getCurrentAddress();
		this.simulator.setStartingAddress(0);
		System.out.println("Welcome to the ARMStrong Interpreter!\n .reset To reset the interpreter\n Close the interpreter to get back to the usual simulation mode.");
	}
	
	public void redirectToInterpreter() {	
		System.setOut(new PrintStream(output));
	}
	
	public void stopInterpreter() {
		this.simulator.getCpu().setCurrentAddress(this.pc);
		this.simulator.setStartingAddress(0x1000);
	}
	
	public DockNode getNode() {
		return dockNode;
	}
	
	public TextField getTextField() {
		return (this.textField);
	}

	public void run() {
		String instruction = this.getTextField().getText();
		if (instruction.contentEquals(".reset")) {
			simulator.resetState();
			System.out.println("The CPU has been reset.");
			this.redirectToInterpreter();
			return;
		}
		simulator.setConsoleInput(instruction);
		try {
			simulator.setProgram(instruction);
			System.out.println("[EXEC] " + instruction + " [" + Integer.toHexString(this.simulator.getRamWord(0)) + "]");
			this.simulator.getCpu().setCurrentAddress(0);
			simulator.runStep();
			this.getTextField().clear();
		} catch (InvalidInstructionException e) {
			System.out.println(e.getMessage());
		}
	}
}
