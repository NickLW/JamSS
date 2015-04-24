package uk.ac.brighton.jamss;



class LeadSound {
	private final String name;
	private final int midiNote;

	public LeadSound(String name, int midiNote) {
		this.name = name;
		this.midiNote = midiNote;
	}

	public int getMidiNote() {
		return midiNote;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static LeadSound[] getSounds() {
		return new LeadSound[] {
				new LeadSound("Acoustic Guitar (nylon)", 25),
				new LeadSound("Acoustic Guitar (steel)", 26),
				new LeadSound("Electric Guitar (jazz)", 27),
				new LeadSound("Electric Guitar (clean)", 28),
				new LeadSound("Electric Guitar (muted)", 29),
				new LeadSound("Overdriven Guitar", 30),
				new LeadSound("Distortion Guitar", 31),
				new LeadSound("Guitar harmonics", 32),
				new LeadSound("Square", 81),
				new LeadSound("Sawtooth", 82),
				new LeadSound("Calliope", 83),
				new LeadSound("Chiff", 84),
				new LeadSound("Charang", 85),
				new LeadSound("Voice", 86),
		};
	}

}