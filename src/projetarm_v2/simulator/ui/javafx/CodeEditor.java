package projetarm_v2.simulator.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.dockfx.DockNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeEditor {

    private Pane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private TextArea textArea;
    private ToolBar toolBar;
    private TextFlow textFlow;
    private List<Text> instructionsAsText;

    public CodeEditor() {
        try {
            mainPane = FXMLLoader.load(getClass().getResource("/resources/EditorView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dockNode = new DockNode(mainPane, "Editor", new ImageView(dockImage));
        dockNode.setPrefSize(300, 100);
        dockNode.setClosable(false);

        this.textArea = (TextArea) mainPane.lookup("#codeArea");
        this.textFlow = (TextFlow) mainPane.lookup("#textFlow");
        this.toolBar = (ToolBar) mainPane.lookup("#toolbar");
        this.dockNode.getStylesheets().add("/resources/style.css");
        mainPane.setMaxHeight(Double.MAX_VALUE);
        mainPane.setMaxWidth(Double.MAX_VALUE);
    }

    public void highlightLine(int line) {
    	// Prends bien en compte le cas où on est à line = 0 (quand on est dans une routine ou une instruction modifiée par quelqu'un)

        for (Text anInstructionsAsText : instructionsAsText) {
            anInstructionsAsText.setFill(Color.BLACK);
        }

        if(line > 0) {
            instructionsAsText.get(line - 1).setFill(Color.RED);
        }
    }
    
    public void setExecutionMode(boolean executionMode){
        this.textArea.setEditable(!executionMode);
        this.textArea.setVisible(!executionMode);
        this.toolBar.setVisible(!executionMode);

        if(executionMode){
            String[] instructionsAsStrings = this.textArea.getText().split("\\r?\\n");
            this.instructionsAsText = new ArrayList<>();
            this.textFlow.getChildren().clear();
            for (int lineNumber = 1; lineNumber <= instructionsAsStrings.length; lineNumber++) {
                String line = lineNumber + "\t" + instructionsAsStrings[lineNumber-1] + '\n';
                this.instructionsAsText.add(new Text(line));
                this.textFlow.getChildren().add(this.instructionsAsText.get(lineNumber-1));
            }
            highlightLine(1);
        }
    }

    public DockNode getNode() {
        return this.dockNode;
    }

    public String getProgramAsString() {
        return textArea.getText();
    }
    public void setProgramAsString(String program){
        this.textArea.setText(program);
    }
}
