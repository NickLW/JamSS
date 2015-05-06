package uk.ac.brighton.jamss;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * This class creates a midi synthesiser and turns notes on
 * and off, sleeping a thread for timing.
 * @author Nick Walker
 */
public class Keys {

	private final int velocity = 50;
	private Thread thread; // New thread each time the metronome is turned on
	private final Runnable runnable = createRunnable();
	private MidiChannel channel = null;
	public int note;
	public int count = 1;
	public int barCount = 0;
	private int octaves = 12;				//used to change pitch of midi note
	private int noteForThisBeat; 
	double rndNote;
	public long sleepTime;

	/** 
	 * Constructor
	 */
	public Keys() {
		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			channel = synthesizer.getChannels()[1];
			channel.programChange(0, 1);
			Instrument[] instr = synthesizer.getDefaultSoundbank().getInstruments();
			System.out.println(instr[1]);
			synthesizer.loadInstrument(instr[1]);

		} catch (MidiUnavailableException ex) {
			//log.error(ex);
		}

	}


	/**
	 * Sets the MIDI root note, to use for deciding which
	 * notes are played
	 * @param note the MIDI note to use
	 */
	public void setNote(int note) {
		this.note = note;
	}


	/**
	 * Stops the Keys.
	 */
	public void stop() {
		//keepPlaying = false;
		if (thread != null) {
			thread.interrupt(); // Interrupt the sleep
		}
	}

	/**
	 * Plays an arpeggio of notes using the time frame given by the Band
	 * at 16th note lengths. The notes to be played are set from the chosen
	 * root note.
	 */
	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {
				

				while (count < 5) {
					// Someone could change note while we sleep. Make sure we 
					// turn on and off the same note.
					noteForThisBeat = note; 
					rndNote = Math.random();
					
					if (rndNote < 0.34){
						noteForThisBeat += 2;
					} else if (rndNote < 0.67){
						noteForThisBeat += 7;
					} else {
						noteForThisBeat += 12;
					}

					if(count > 1){
						channel.noteOn(noteForThisBeat - octaves, velocity);
					}

					count ++;
					
					try {
						Thread.sleep(sleepTime);

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}

					channel.noteOff(noteForThisBeat - octaves);

				}
				// log.debug("Thread ending");
				count = 1;
			}
		};
	}

	/**
	 * Starts the thread
	 */
	void startThread() {
		if (channel != null) {
			thread = new Thread(runnable, "Keys");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}
}