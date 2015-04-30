package uk.ac.brighton.jamss;


/**
 * This class creates the choices available to the user for the 
 * drum track.
 * @author Nick Walker
 *
 */
class DrumChoice {
	private final String dname;

	/** 
	 * Constructor
	 * @param name
	 */
	public DrumChoice(String name) {
		this.dname = name;
	}

	/**
	 * Getter for name
	 * @return
	 */
	public String getName() {
		return dname;
	}

	@Override
	public String toString() {
		return dname;
	}
	
	/**
	 * Getter for populating list of choices in GUI
	 * @return
	 */
	public static DrumChoice[] getDChoice() {
		return new DrumChoice[] {
				new DrumChoice("src/samples/072ChorusRide16thsF6b.mid"),
				new DrumChoice("src/samples/110ChorusRide8thsF4.mid"),
		};
	}

}