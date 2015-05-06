package uk.ac.brighton.jamss;

/**
 * This class creates the choices available to the user for the 
 * scale.
 * @author Nick Walker
 *
 */
class ScaleChoice {
	private final String name;

	/** 
	 * Constructor
	 * @param name
	 */
	public ScaleChoice(String name) {
		this.name = name;
	}

	/**
	 * Getter for name
	 * @return
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Getter for populating list of choices in GUI
	 * @return
	 */
	public static ScaleChoice[] getSChoice() {
		return new ScaleChoice[] {
				new ScaleChoice("Major"),
				new ScaleChoice("Minor"),
		};
	}
}