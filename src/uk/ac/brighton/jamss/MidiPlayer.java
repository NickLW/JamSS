package uk.ac.brighton.jamss;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * Creates a sequencer used to play a percussion sequence taken
 * from a .midi file.
 * @author Nick Walker
 */
class MidiPlayer {

	// Midi meta event
	public static final int END_OF_TRACK_MESSAGE = 47;

	private Sequencer sequencer;
	public float tempo;
	private boolean loop;
	public boolean paused;
	
	public void setBPMs(float beatsPerMinute){
		tempo = beatsPerMinute;
	}

	/**
	 * Creates a new MidiPlayer object.
	 */
	public MidiPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//sequencer.addMetaEventListener(this);
		} catch (MidiUnavailableException ex) {
			sequencer = null;
		}
	}

	/**
	 * Loads a sequence from the file system. Returns null if an error occurs.
	 */
	public Sequence getSequence(String filename) {
		try {
			return getSequence(new FileInputStream(filename));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Loads a sequence from an input stream. Returns null if an error occurs.
	 */
	public Sequence getSequence(InputStream is) {
		try {
			if (!is.markSupported()) {
				is = new BufferedInputStream(is);
			}
			Sequence s = MidiSystem.getSequence(is);
			is.close();
			return s;
		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Plays a sequence, optionally looping. This method returns immediately.
	 * The sequence is not played if it is invalid.
	 */
	public void play(Sequence sequence, boolean loop) {
		if (sequencer != null && sequence != null && sequencer.isOpen()) {
			try {
				sequencer.setSequence(sequence);
				sequencer.open();
				/*if(loop) {
            		sequencer.setLoopStartPoint(0);
            		sequencer.setLoopEndPoint(-1);
            		sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            		sequencer.setTempoInBPM(tempo);
        		}*/

				sequencer.setTempoInBPM(tempo);
				sequencer.start();

				this.loop = loop;
			} catch (InvalidMidiDataException ex) {
				ex.printStackTrace();
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is called by the sound system when a meta event occurs. In
	 * this case, when the end-of-track meta event is received, the sequence is
	 * restarted if looping is on.
	 */
	/*public void meta(MetaMessage event) {
		if (event.getType() == END_OF_TRACK_MESSAGE) {
			if (sequencer != null && sequencer.isOpen() && loop) {
				sequencer.setMicrosecondPosition(0);
				sequencer.setTempoInBPM(tempo);
				sequencer.start();
			}
		}
	}*/

	/**
	 * Stops the sequencer and resets its position to the 
	 * start of the sequence.
	 */
	public void stop() {
		if (sequencer != null && sequencer.isOpen()) {
			sequencer.stop();
			sequencer.setMicrosecondPosition(0);
		}
	}

	/**
	 * Closes the sequencer.
	 */
	public void close() {
		if (sequencer != null && sequencer.isOpen()) {
			sequencer.close();
		}
	}

	/**
	 * Gets the sequencer.
	 */
	public Sequencer getSequencer() {
		return sequencer;
	}

	/**
	 * Sets the paused state. Music may not immediately pause.
	 */
	public void setPaused(boolean paused) {
		if (this.paused != paused && sequencer != null && sequencer.isOpen()) {
			this.paused = paused;
			if (paused) {
				sequencer.stop();
			} else {
				sequencer.start();
			}
		}
	}

	/**
	 * Returns the paused state.
	 */
	public boolean isPaused() {
		return paused;
	}
}