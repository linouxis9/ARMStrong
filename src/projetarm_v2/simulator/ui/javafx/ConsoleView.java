/*
 * Copyright (c) 2018-2019 Valentin D'Emmanuele, Gilles Mertens, Dylan Fraisse, Hugo Chemarin, Nicolas Gervasi
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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

	OutputStream output;
	
	public ConsoleView() {
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/resources/ConsoleView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.dockNode = new DockNode(mainPane, "Console", new ImageView(dockImage));
		this.dockNode.setPrefSize(1000, 1500);
		this.dockNode.setClosable(false);
		dockNode.setMaxHeight(300); //mmm

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
						if (textFlow.getChildren().size() > 100) {
							textFlow.getChildren().clear();
						}
					});
				}
			}
		};
		
	}

	
	public void clear() {
		Platform.runLater(() -> {
			textFlow.getChildren().clear();
		});
	}
	
	public void redirectToConsole() {
		System.setOut(new PrintStream(output));
	}
	
	public DockNode getNode() {
		return dockNode;
	}

	public TextField getTextField() {
		return (this.textField);
	}
}
