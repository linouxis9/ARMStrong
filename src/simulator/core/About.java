package simulator.core;

/**
 * Information about the Software
 */
public final class About {
	/**
	 * The name of the software.
	 */
	public static final String NAME = "#@rm Simulator";
	
	/**
	 * The software's version.
	 */
	public static final String VERSION = "v1.0";
	
	/**
	 * The software's copyright.
	 */
	public static final String COPYRIGHT = "Copyright (C) 2018 Project #@rm Simulator";
	
	/**
	 * The software's developers.
	 */
	public static final String DEVELOPERS = "Valentin D'Emmanuele, Gilles Mertens, Nicolas Gervasi, Hugo Chemarin, Dylan Fraisse";
	
	/**
	 * The software's copyright.
	 */
	public static final String EMAIL = "projectarm@devling.xyz";
	
	/**
	 * Returns a small recap of the software About's information.
	 * It should be noted that toString can't be a static method, which explains this method's name.
	 */
	public static String info() {
		return NAME + " " + VERSION + " by " + DEVELOPERS + System.lineSeparator() + COPYRIGHT;
	}
	
	/**
	 * Prevents the creation of an About object.
	 */	
	private About() {
		
	}
}
