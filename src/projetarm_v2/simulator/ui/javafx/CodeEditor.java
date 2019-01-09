package projetarm_v2.simulator.ui.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.dockfx.DockNode;

import java.io.IOException;

public class CodeEditor {

    private Pane mainPane;
    private DockNode dockNode;
    private Image dockImage;

    private TextArea textArea;
    private ToolBar toolBar;

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
        this.toolBar = (ToolBar) mainPane.lookup("#toolbar");

        mainPane.setMaxHeight(Double.MAX_VALUE);
        mainPane.setMaxWidth(Double.MAX_VALUE);

    }


    public DockNode getNode() {
        return this.dockNode;
    }

    public TextArea getTextArea() {
        return this.textArea;
    }
}
