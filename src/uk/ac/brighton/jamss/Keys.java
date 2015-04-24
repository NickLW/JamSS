package uk.ac.brighton.jamss;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

//import org.apache.log4j.Logger;

/**
 * A metronome.
 * @author  Dave Briccetti
 */
public class Keys {

	private final int velocity = 50;
	private Thread thread; // New thread each time the metronome is turned on
	private final Runnable runnable = createRunnable();
	private MidiChannel channel = null;
	public int note;
	public int currNote = 30;
	public int count = 1;
	public int barCount = 0;
	private static String scale = "Minor";
	private static String root = "E";
	public long sleepTime;

	private static double[] MAJFREQUENCIES = Scale.getFreq(root, scale);
	private static String[] MAJNAME        = Scale.getScale(root, scale);

	public static void main(String[] args) {

	}

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

	private Runnable createRunnable() {
		return new Runnable() {

			public void run() {

				long wokeLateOrEarlyBy = 0;
				boolean played = false;
				int octaves = 12;

				while (count < 5) {

					// Someone could change note while we sleep. Make sure we 
					// turn on and off the same note.
					final int noteForThisBeat = note; 

					System.out.println ("late(+)/early(-): " + wokeLateOrEarlyBy);

					/*THIS BIT IS SORTING WHAT THE GUITARS PLAY AND WHEN!!*/

					MAJFREQUENCIES = Scale.getFreq(root, scale);
					MAJNAME        = Scale.getScale(root, scale); 

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


					final long currentTimeBeforeSleep = System.currentTimeMillis();
					//correct time to sleep by previous error, to keep the overall tempo
					//sleepTime = (timeBetweenBeats - wokeLateOrEarlyBy) / 4;
					final long expectedWakeTime = currentTimeBeforeSleep + sleepTime;

					try {
						Thread.sleep(sleepTime);

					} catch (InterruptedException ex) {
						// log.debug("Interrupted");
					}

					wokeLateOrEarlyBy = System.currentTimeMillis() - expectedWakeTime;

					channel.noteOff(noteForThisBeat - octaves);
					channel.noteOff((noteForThisBeat + 4) - octaves);
					channel.noteOff((noteForThisBeat + 7) - octaves);

				}
				// log.debug("Thread ending");
				count = 1;
			}
		};
	}

	void startThread() {
		if (channel != null) {
			thread = new Thread(runnable, "Metronome");
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
	}

}