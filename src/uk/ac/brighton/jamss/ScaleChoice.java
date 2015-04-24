package uk.ac.brighton.jamss;

class ScaleChoice {
	private final String name;

	public ScaleChoice(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static ScaleChoice[] getSChoice() {
		return new ScaleChoice[] {
				new ScaleChoice("Major"),
				new ScaleChoice("Minor"),
		};
	}

}