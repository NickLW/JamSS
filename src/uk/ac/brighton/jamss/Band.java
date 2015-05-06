package uk.ac.brighton.jamss;

/**
 * This class keeps the overall timing for the session by sleeping a thread.
 * It creates each instrument, deciding when they should play. Also keeps 
 * track of current input note and settings which are passed on to instruments.
 * @author Nick Walker
 * 
 * Class originally based on:
 * A metronome.
 * @author  Dave Briccetti
 */
public class Band {

	private Thread thread; // New thread each time the metronome is turned on
	private final Runnable runnable = createRunnable();
	private long timeBetweenBeats;
	private boolean keepPlaying;
	private int note;
	private int currNote = 64;
	private int rootNote = 64;
	private DrumTrack drums = new DrumTrack();
	private static Keys keys = new Keys();
	private static Lead lead1 = new Lead();
	boolean drumsOn = false;
	private int count = 1;
	private int barCount = 0;
	private static String scale = "Minor";
	private static String root = "E";

	private static double[] FREQUENCIES = Scale.getFreq(root, scale);
	private static String[] NAME        = Scale.getScale(root, scale);


	/** 
	 * Band constructor
	 */
	public Band() {
		setTempo(72);
	}

	/**
	 * Sets the tempo. May be called while the Band is running.
	 * @param beatsPerMinute the tempo, in beats per minute
	 */
	public void setTempo(int beatsPerMinute) {
		processTempoChange(beatsPerMinute);
	}

	/**
	 * Sets the MIDI note, in the percussion channel, to use for the 
	 * metronome sound. See http://en.wikipedia.org/wiki/General_MIDI. 
	 * @param note the MIDI note to use
	 */
	public void setNote(int note) {
		this.note = note;
	}

	/**
	 * Sets currNote to a midi note value that matches the pitch
	 * @param pitch the frequency as a double
	 */
	public void setCurrNote(double pitch){
		if (pitch != -1) {

			double frequency = normaliseFreq(pitch);
			int note = closestNote(frequency);;

			if (NAME[note] == "F"){
				this.currNote = 65;
			} else if (NAME[note] == "E"){
				this.currNote = 64;
			} else if (NAME[note] == "D#"){
				this.currNote = 63;
			} else if (NAME[note] == "D"){
				this.currNote = 62;
			} else if (NAME[note] == "C#"){
				this.currNote = 61;
			} else if (NAME[note] == "C"){
				this.currNote = 60;
			} else if (NAME[note] == "B"){
				this.currNote = 71;
			} else if (NAME[note] == "A#"){
				this.currNote = 70;
			} else if (NAME[note] == "A"){
				this.currNote = 69;
			} else if (NAME[note] == "G#"){
				this.currNote = 68;
			} else if (NAME[note] == "G"){
				this.currNote = 67;
			} else if (NAME[note] == "F#"){
				this.currNote = 66;
			} 
		}
	}


	/**
	 * Ensures that the frequency of the input falls within the bounds of the 
	 * scale being used.
	 * @param hz frequency as a double
	 * @return hz
	 */
	private static double normaliseFreq(double hz) {
		// get hz into a standard range to make things easier to deal with
		while ( hz < FREQUENCIES[7] ) {
			hz = 2*hz;
		}
		while ( hz > FREQUENCIES[0] ) {
			hz = 0.5*hz;
		}
		return hz;
	}

	/**
	 * Matches the frequency to the closest note in the scale
	 * @param hz frequency as double
	 * @return hz
	 */
	private static int closestNote(double hz) {
		double minDist = Double.MAX_VALUE;
		int minFreq = -1;
		for ( int i = 0; i < FREQUENCIES.length; i++ ) {
			double dist = Math.abs(FREQUENCIES[i]-hz);
			if ( dist < minDist ) {
				minDist=dist;
				minFreq=i;
			}
		}
		return minFreq;
	}

	/**
	 * Stops the Band and drum sequence 
	 * and resets counters ready to start again.
	 */
	public void stop() {
		keepPlaying = false;
		if (thread != null) {
			thread.interrupt(); // Interrupt the sleep
		}
		drums.stop();
		drumsOn = false;
		count = 1;
		barCount = 0;
	}

	/**
	 * Runnable that sleeps based on tempo and determines when instruments
	 * should be played.
	 * @return
	 */
	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {
				long wokeLateOrEarlyBy = 0;

				while (keepPlaying) {
					
					setNote(currNote);
					
					final long sleepTime = timeBetweenBeats - wokeLateOrEarlyBy;
					//System.out.println ("late(+)/early(-): " + wokeLateOrEarlyBy);

					FREQUENCIES = Scale.getFreq(root, scale);
					NAME        = Scale.getScale(root, scale);

					if (!drumsOn){
						drums.run();
						drumsOn = true;
					}

					if (count % 2 != 0) {
						startKeys(rootNote, sleepTime);
					}

					if (count % 2 == 0) {
						startLead(note, sleepTime);
					}
					
					count ++;

					if(count > 4){
						count = 1;
						barCount ++;
					}
					
					if (barCount == 8 && count == 1){
						barCount = 0;
						drumsOn = false;
					}

					final long currentTimeBeforeSleep = System.currentTimeMillis();
					//correct time to sleep by previous error, to keep the overall tempo
					final long expectedWakeTime = currentTimeBeforeSleep + sleepTime;

					try {
						Thread.sleep(sleepTime);

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}
					
					wokeLateOrEarlyBy = System.currentTimeMillis() - expectedWakeTime;

					stopKeys();
					stopLead();
				}
				// log.debug("Thread ending");
			}
		};
	}

	/**
	 * Sets the sleep time and speed of drums to match the tempo set in the GUI
	 * @param beatsPerMinute
	 */
	void processTempoChange(int beatsPerMinute) {
		timeBetweenBeats = 1000 * 60 / beatsPerMinute;
		drums.setBPM(beatsPerMinute);
	}

	/**
	 * Starts the Band thread
	 */
	void startThread() {
			keepPlaying = true;
			thread = new Thread(runnable, "Band");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
	}

	/**
	 * Sets the scale selected from the GUI
	 * @param choice
	 */
	void setScaleFromChoice(String choice) {
		scale = choice;
	}

	/**
	 * Sets the rootnote to a midi value that matches the
	 * note selected from GUI.
	 * @param choice
	 */
	void setRootFromChoice(String choice) {
		root = choice;

		if (root == "F"){
			this.rootNote = 65;
		} else if (root == "E"){
			this.rootNote = 64;
		} else if (root == "D#"){
			this.rootNote = 63;
		} else if (root == "D"){
			this.rootNote = 62;
		} else if (root == "C#"){
			this.rootNote = 61;
		} else if (root == "C"){
			this.rootNote = 60;
		} else if (root == "B"){
			this.rootNote = 71;
		} else if (root == "A#"){
			this.rootNote = 70;
		} else if (root == "A"){
			this.rootNote = 69;
		} else if (root == "G#"){
			this.rootNote = 68;
		} else if (root == "G"){
			this.rootNote = 67;
		} else if (root == "F#"){
			this.rootNote = 66;
		} 
	}

	/**
	 * Sets the drum track selected from GUI
	 * @param choice
	 */
	void setDrumFromChoice(String choice) {
		drums.setBeat(choice);
	}

	/**
	 * Sets the midi sound for the lead instrument selected from GUI
	 * @param choice
	 */
	void setLeadFromChoice(int choice) {
		lead1.setLead(choice);
	}
	
	/**
	 * Begins the thread for the keys instrument
	 * @param root
	 * @param bpm
	 */
	public static void startKeys(int root, long bpm) {
		keys.sleepTime = bpm / 4;
		keys.setNote(root);
		keys.startThread();	
	}

	/**
	 * Stops the keys instrument
	 */
	public static void stopKeys() {
		keys.stop();	
	}
	
	/**
	 * Begins the thread for the lead instrument
	 * @param note
	 * @param bpm
	 */
	public static void startLead(int note, long bpm) {
		lead1.sleepTime = bpm;
		lead1.setNote(note);
		lead1.startThread();	
	}

	/**
	 * Stops the lead instrument
	 */
	public static void stopLead() {
		lead1.stop();	
	}
}