package uk.ac.brighton.jamss;


/**
 * This class creates the options available to the user for the Lead
 * instrument's MIDI sound. Each choice is an array containing the
 * name of the sound and it's corresponding MIDI sound number.
 * @author Nick Walker
 *
 */
class LeadChoice {
	private final String name;
	private final int midiSound;

	/** 
	 * Constructor
	 * @param name
	 * @param midiValue
	 */
	public LeadChoice(String name, int midiValue) {
		this.name = name;
		this.midiSound = midiValue;
	}

	/**
	 * Getter for sound
	 * @return
	 */
	public int getMidiSound() {
		return midiSound;
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
	 * Getter for 
	 * populating list of choices in GUI
	 * @return
	 */
	public static LeadChoice[] getSounds() {
		return new LeadChoice[] {
				new LeadChoice("Acoustic Guitar (nylon)", 25),
				new LeadChoice("Acoustic Guitar (steel)", 26),
				new LeadChoice("Electric Guitar (jazz)", 27),
				new LeadChoice("Electric Guitar (clean)", 28),
				new LeadChoice("Electric Guitar (muted)", 29),
				new LeadChoice("Overdriven Guitar", 30),
				new LeadChoice("Distortion Guitar", 31),
				new LeadChoice("Guitar harmonics", 32),
				new LeadChoice("Square", 81),
				new LeadChoice("Sawtooth", 82),
				new LeadChoice("Calliope", 83),
				new LeadChoice("Chiff", 84),
				new LeadChoice("Charang", 85),
				new LeadChoice("Voice", 86),
		};
	}
}