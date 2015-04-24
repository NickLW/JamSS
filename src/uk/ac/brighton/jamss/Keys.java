package uk.ac.brighton.jamss;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

//import org.apache.log4j.Logger;

/**
 * 
 * 
 */
public class Keys {

	private final int velocity = 50;
	private Thread thread; // New thread each time the metronome is turned on
	private final Runnable runnable = createRunnable();
	private MidiChannel channel = null;
	public int note;
	public int count = 1;
	public int barCount = 0;
	public long sleepTime;

	/** Creates new form Metronome */
	public Keys() {
		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			channel = synthesizer.getChannels()[3];
			channel.programChange(0, 1);
			Instrument[] instr = synthesizer.getDefaultSoundbank().getInstruments();
			System.out.println(instr[1]);
			synthesizer.loadInstrument(instr[1]);

		} catch (MidiUnavailableException ex) {
			//log.error(ex);
		}

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
	 * Stops the metronome.
	 */
	public void stop() {
		//keepPlaying = false;
		if (thread != null) {
			thread.interrupt(); // Interrupt the sleep
		}
	}

	/**
	 * Plays an arpeggio of notes using the time frame given by the metronome
	 * at 16th note lengths. The notes to be played are set from the chosen
	 * root note.
	 * @return
	 */
	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {
				int octaves = 12;

				while (count < 5) {
					// Someone could change note while we sleep. Make sure we 
					// turn on and off the same note.
					final int noteForThisBeat = note; 

					if (count == 1) {
						//channel.noteOn(noteForThisBeat - octaves, velocity);
						count ++;
					} else if(count == 2){
						channel.noteOn((noteForThisBeat + 2) - octaves, velocity);
						count ++;
					} else if(count == 3){
						channel.noteOn((noteForThisBeat + 4) - octaves, velocity);
						count ++;
					} else if(count == 4){
						channel.noteOn((noteForThisBeat + 7) - octaves, velocity);
						count ++;
					}

					try {
						Thread.sleep(sleepTime);

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}

					channel.noteOff(noteForThisBeat - octaves);
					channel.noteOff((noteForThisBeat + 4) - octaves);
					channel.noteOff((noteForThisBeat + 7) - octaves);

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