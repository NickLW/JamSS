package uk.ac.brighton.jamss;

class DrumChoice {
	private final String dname;

	public DrumChoice(String name) {
		this.dname = name;
	}

	public String getName() {
		return dname;
	}

	@Override
	public String toString() {
		return dname;
	}
	
	public static DrumChoice[] getDChoice() {
		return new DrumChoice[] {
				new DrumChoice("src/samples/072ChorusRide16thsF6b.mid"),
				new DrumChoice("src/samples/110ChorusRide8thsF4.mid"),
		};
	}

}