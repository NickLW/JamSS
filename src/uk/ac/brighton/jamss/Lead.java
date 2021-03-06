package uk.ac.brighton.jamss;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
//import org.apache.log4j.Logger;

/**
 * This class creates a midi synthesiser and turns notes on
 * and off, sleeping a thread for timing.
 * @author Nick Walker
 */
public class Lead { 

	//static Logger log = Logger.getLogger(Metronome.class);
	private final int velocity = 85;
	private Thread thread; // New thread each time the metronome is turned on
	private final Runnable runnable = createRunnable();
	private MidiChannel channel = null;
	private boolean keepPlaying;
	public int note;
	public int lead = 26;
	private int octaves = 12;
	private int noteForThisBeat;
	public long sleepTime;

	/** 
	 * Constructor
	 */
	public Lead() {
		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			channel = synthesizer.getChannels()[0];
			channel.programChange(0, lead);
			Instrument[] instr = synthesizer.getDefaultSoundbank().getInstruments();
			//System.out.println(instr[lead]);
			synthesizer.loadInstrument(instr[lead]);

		} catch (MidiUnavailableException ex) {
			//log.error(ex);
		}
	}

	/**
	 * Sets the MIDI note for creation of a
	 * chord. 
	 * @param note the MIDI note to use
	 */
	public void setNote(int note) {
		this.note = note;
	}

	/**
	 * Sets the MIDI instrument to be played
	 * @param instr
	 */
	public void setLead(int instr) {
		this.lead = instr;
		this.channel.programChange(0, lead);
	}


	/**
	 * Stops the Lead.
	 */
	public void stop() {
		keepPlaying = false;
		if (thread != null) {
			thread.interrupt(); // Interrupt the sleep
		}
	}

	/**
	 * Runnable that plays a chord of 3 MIDI notes for the length of
	 * 1 whole beat.
	 * @return
	 */
	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {
				
				while (keepPlaying) {
					noteForThisBeat = note;

					channel.noteOn(noteForThisBeat - octaves, velocity);
					channel.noteOn((noteForThisBeat + 7) - octaves, velocity);
					channel.noteOn((noteForThisBeat + 12) - octaves, velocity);
					
					try {
						Thread.sleep(sleepTime+1);		//+1 to account for slight changes in timing from Band

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}

					channel.noteOff(noteForThisBeat - octaves);
					channel.noteOff((noteForThisBeat + 7) - octaves);
					channel.noteOff((noteForThisBeat + 12) - octaves);
				}
				// log.debug("Thread ending");
			}
		};
	}

	/**
	 * Starts the thread
	 */
	void startThread() {
		if (channel != null) {
			keepPlaying = true;
			thread = new Thread(runnable, "Lead");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}
}


