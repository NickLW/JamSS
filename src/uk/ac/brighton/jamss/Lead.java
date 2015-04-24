package uk.ac.brighton.jamss;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
//import org.apache.log4j.Logger;

/**
 * This class creates the lead instrument
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
	public int currNote = 64;
	public int rootNote = 64;
	public DrumTrack drums = new DrumTrack();
	boolean drumsOn = false;
	boolean keysOn = false;
	public int count = 1;
	public int barCount = 0;
	public long sleepTime;


	public static void main(String[] args) {


	}

	/** Creates new form Metronome */
	public Lead() {
		try {
			final Synthesizer synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			channel = synthesizer.getChannels()[2];
			channel.programChange(0, lead);
			Instrument[] instr = synthesizer.getDefaultSoundbank().getInstruments();
			System.out.println(instr[lead]);
			synthesizer.loadInstrument(instr[lead]);

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

	public void setLead(int note) {
		this.lead = note;
		this.channel.programChange(0, lead);
	}


	/**
	 * Stops the metronome.
	 */
	public void stop() {
		keepPlaying = false;
		if (thread != null) {
			thread.interrupt(); // Interrupt the sleep
		}
		count = 1;
		barCount = 0;
	}

	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {


				long wokeLateOrEarlyBy = 0;
				int octaves = 12;

				while (keepPlaying) {
					final int noteForThisBeat = note;
					//sleepTime = timeBetweenBeats - wokeLateOrEarlyBy;
					System.out.println ("late(+)/early(-): " + wokeLateOrEarlyBy);

					/*THIS BIT IS SORTING WHAT THE GUITARS PLAY AND WHEN!!*/

					channel.noteOn(noteForThisBeat - octaves, velocity);
					channel.noteOn((noteForThisBeat + 4) - octaves, velocity);
					channel.noteOn((noteForThisBeat + 7) - octaves, velocity);
					count ++;

					if(count > 4){
						count = 1;
						barCount ++;
					}

					final long currentTimeBeforeSleep = System.currentTimeMillis();
					//correct time to sleep by previous error, to keep the overall tempo

					final long expectedWakeTime = currentTimeBeforeSleep + sleepTime;

					try {
						Thread.sleep(sleepTime+1);

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}
					wokeLateOrEarlyBy = System.currentTimeMillis() - expectedWakeTime;

					channel.noteOff(noteForThisBeat - octaves);
					channel.noteOff((noteForThisBeat + 4) - octaves);
					channel.noteOff((noteForThisBeat + 7) - octaves);
				}
				// log.debug("Thread ending");
			}
		};
	}

	void startThread() {
		if (channel != null) {
			keepPlaying = true;
			thread = new Thread(runnable, "Metronome");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}

}


