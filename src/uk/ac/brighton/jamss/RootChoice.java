package uk.ac.brighton.jamss;

class RootChoice {
	private final String rname;

	public RootChoice(String name) {
		this.rname = name;
	}

	public String getName() {
		return rname;
	}

	@Override
	public String toString() {
		return rname;
	}
	
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