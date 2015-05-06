package uk.ac.brighton.jamss;

/**
 * This class creates the choices available to the user for the 
 * root note.
 * @author Nick Walker
 *
 */
class RootChoice {
	private final String rname;

	/** 
	 * Constructor
	 * @param name
	 */
	public RootChoice(String name) {
		this.rname = name;
	}

	/**
	 * Getter for name
	 * @return
	 */
	public String getName() {
		return rname;
	}

	@Override
	public String toString() {
		return rname;
	}
	
	/**
	 * Getter for populating list of choices in GUI
	 * @return
	 */
	public static RootChoice[] getRChoice() {
		return new RootChoice[] {
				new RootChoice("E"),
				new RootChoice("D#"),
				new RootChoice("D"),
				new RootChoice("C#"),
				new RootChoice("C"),
				new RootChoice("B"),
				new RootChoice("A#"),
				new RootChoice("A"),
				new RootChoice("G#"),
				new RootChoice("G"),
				new RootChoice("F#"),
				new RootChoice("F"),
		};
	}
}