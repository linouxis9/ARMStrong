package simulator.ui.javafx;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public enum Language {
	ENGLISH(Collections.unmodifiableMap(initializeEnglish())), FRENCH(Collections.unmodifiableMap(initializeFrench()));
	
	private Map<String, String> language;

	private Language(Map<String,String> language) {
		this.language = language;
	}

	private static Map initializeFrench() {
		Map<String,String> language = new HashMap<>();
		language.put("viewMode", "Mode d'affichage");
		language.put("goToAddressField", "Aller à l'adresse");
		language.put("invalidAddress", "Adresse Invalide");
		language.put("addressTooLowOrTooHigh", "Adresse hors RAM");
		language.put("fileMenu", "File");
		language.put("editMenu", "Edit");
		language.put("runMenu", "Run");
		language.put("helpMenu", "Help");
		
		return language;
	}

	private static Map initializeEnglish() {
		Map<String,String> language = new HashMap<>();
		language.put("viewMode", "View mode");
		language.put("goToAddressField", "Go to adress");
		language.put("invalidAddress", "Invalid Address");
		language.put("addressTooLowOrTooHigh", "Address too low or too high");
		language.put("fileMenu", "Fichier");
		language.put("editMenu", "Editer");
		language.put("runMenu", "Executer");
		language.put("helpMenu", "Aide");
		return language;
	}
	
	public String getTranslation(String name) {
		return this.language.get(name);
	}
}
