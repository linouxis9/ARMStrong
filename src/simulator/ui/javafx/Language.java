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

		language.put("File", "");
		language.put("Edit", "");
		language.put("Run", "");
		language.put("Help", "");

		language.put("New", "");
		language.put("Open file", "");
		language.put("Save", "");
		language.put("Save as", "");
		language.put("Exit", "");
		language.put("Help", "");


		language.put("Enter in execution mode", "");
		language.put("Exit the execution mode", "");
		language.put("Run a single instruction", "");
		language.put("Reload Program", "");
		language.put("Stop Execution", "");
		language.put("Documentation", "");
		language.put("Preferences", "");


		language.put("Unsigned Decimal", "");
		language.put("Signed Decimal", "");
		language.put("Hexadecimal", "");

		
		return language;
	}

	private static Map initializeEnglish() {
		Map<String,String> language = new HashMap<>();
		
		return language;
	}
	
	public String getTranslation(String name) {
		return this.language.get(name);
	}
}
