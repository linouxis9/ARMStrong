package simulator.ui.javafx;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIMenuBar {

	private List<MenuItem> diabledInExecMode;
	private List<MenuItem> enabledInExecMode;
	private MenuBar theMenuBar;

	private MenuItem newMenuItem;
	private MenuItem openMenuItem;
	private MenuItem saveMenuItem;
	private MenuItem saveAsMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem enterExecutionModeMenuItem;
	private MenuItem exitExecutionModeMenuItem;
	private MenuItem runMenuItem;
	private MenuItem runSingleMenuItem;
	private MenuItem helpMenuItem;
	private MenuItem documentationMenuItem;
	private MenuItem reloadProgramMenuItem;
	private MenuItem preferencesMenuItem;
	private MenuItem stopMenuItem;

	private Menu fileMenu;
	private Menu editMenu;
	private Menu runMenu;
	private Menu helpMenu;
	
	private Map<String, Language> languages;
	private String language;
	
	public GUIMenuBar(MenuBar aMenuBar) {

		theMenuBar = aMenuBar;

		this.fileMenu = new Menu("File");
		this.editMenu = new Menu("Edit");
		this.runMenu = new Menu("Run");
		this.helpMenu = new Menu("Help");

		this.newMenuItem = new MenuItem("New");
		this.openMenuItem = new MenuItem("Open file");
		this.saveMenuItem = new MenuItem("Save");
		this.saveAsMenuItem = new MenuItem("Save as");
		this.exitMenuItem = new MenuItem("Exit");
		this.helpMenuItem = new MenuItem("Help");

		this.enterExecutionModeMenuItem = new MenuItem("Enter in execution mode");
		this.exitExecutionModeMenuItem = new MenuItem("Exit the execution mode");
		this.runMenuItem = new MenuItem("Run");
		this.runSingleMenuItem = new MenuItem("Run a single instruction");
		this.reloadProgramMenuItem = new MenuItem("Reload Program");
		this.stopMenuItem = new MenuItem("Stop Execution");
		this.documentationMenuItem = new MenuItem("Documentation");
		this.preferencesMenuItem = new MenuItem("Preferences");


		this.languages = GUI.getLanguagesData();
		
		this.language = GUI.getCurrentLanguage();
		
		diabledInExecMode = new ArrayList<MenuItem>();
		enabledInExecMode = new ArrayList<MenuItem>();

		changeLanguage();

		theMenuBar.getMenus().addAll(fileMenu, editMenu, runMenu, helpMenu);

		fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, preferencesMenuItem, new SeparatorMenuItem(), exitMenuItem);
		editMenu.getItems().addAll();
		runMenu.getItems().addAll(enterExecutionModeMenuItem, exitExecutionModeMenuItem, new SeparatorMenuItem(), runMenuItem, runSingleMenuItem, stopMenuItem, new SeparatorMenuItem(), reloadProgramMenuItem);
		helpMenu.getItems().addAll(helpMenuItem, documentationMenuItem);

		diabledInExecMode.add(newMenuItem);
		diabledInExecMode.add(openMenuItem);
		diabledInExecMode.add(saveMenuItem);
		diabledInExecMode.add(saveAsMenuItem);
		diabledInExecMode.add(enterExecutionModeMenuItem);

		enabledInExecMode.add(exitExecutionModeMenuItem);
		enabledInExecMode.add(runMenuItem);
		enabledInExecMode.add(runSingleMenuItem);
		enabledInExecMode.add(stopMenuItem);
		enabledInExecMode.add(reloadProgramMenuItem);

		exitMenuItem.setOnAction((ActionEvent actionEvent) -> {
			System.exit(0);
		});
		
		exitExecMode();

	}

	public void changeLanguage() {
		fileMenu.setText(this.languages.get(language).getTranslation("File"));
		editMenu.setText(this.languages.get(language).getTranslation("Edit"));
		runMenu.setText(this.languages.get(language).getTranslation("Run"));
		helpMenu.setText(this.languages.get(language).getTranslation("Help"));

		newMenuItem.setText(this.languages.get(language).getTranslation("New"));
		openMenuItem.setText(this.languages.get(language).getTranslation("Open file"));
		saveMenuItem.setText(this.languages.get(language).getTranslation("Save"));
		saveAsMenuItem.setText(this.languages.get(language).getTranslation("Save as"));
		exitMenuItem.setText(this.languages.get(language).getTranslation("Exit"));
		helpMenuItem.setText(this.languages.get(language).getTranslation("Help"));

		enterExecutionModeMenuItem.setText(this.languages.get(language).getTranslation("Enter in execution mode"));
		exitExecutionModeMenuItem.setText(this.languages.get(language).getTranslation("Exit the execution mode"));
		runMenuItem.setText(this.languages.get(language).getTranslation("Run a single instruction"));
		runSingleMenuItem.setText(this.languages.get(language).getTranslation("Run a single instruction"));
		reloadProgramMenuItem.setText(this.languages.get(language).getTranslation("Reload Program"));
		stopMenuItem.setText(this.languages.get(language).getTranslation("Stop Execution"));
		documentationMenuItem.setText(this.languages.get(language).getTranslation("Documentation"));
		preferencesMenuItem.setText(this.languages.get(language).getTranslation("Preferences"));
		
	}
	
	public void setExecMode() {
		switchMenuState(false);
	}

	public void exitExecMode() {
		switchMenuState(true);
	}

	private void switchMenuState(boolean state) {
		for (int item = 0; item < diabledInExecMode.size(); item++) {
			diabledInExecMode.get(item).setDisable(!state);
		}
		for (int item=0; item < enabledInExecMode.size(); item++){
			enabledInExecMode.get(item).setDisable(state);
		}
	}

	public MenuItem getNewMenuItem() {
		return newMenuItem;
	}

	public MenuItem getOpenMenuItem() {
		return openMenuItem;
	}

	public MenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	public MenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	public MenuItem getEnterExecutionModeMenuItem() {
		return enterExecutionModeMenuItem;
	}

	public MenuItem getExitExecutionModeMenuItem() {
		return exitExecutionModeMenuItem;
	}

	public MenuItem getRunMenuItem() {
		return runMenuItem;
	}

	public MenuItem getRunSingleMenuItem() {
		return runSingleMenuItem;
	}

	public MenuItem getHelpMenuItem() {
		return helpMenuItem;
	}

	public MenuItem getReloadProgramMenuItem() {
		return reloadProgramMenuItem;
	}

	public MenuItem getDocumentationMenuItem() {
		return documentationMenuItem;
	}

	public MenuItem getPreferencesMenuItem() {
		return preferencesMenuItem;
	}

	public MenuItem getStopMenuItem() {
		return stopMenuItem;
	}
}
