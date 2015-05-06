package uk.ac.brighton.jamss;

import javax.sound.midi.Sequence;

/**
 * This class creates a MidiPlayer and controls which
 * sequence to play. It contains methods to start and stop
 * the MidiPlayer.
 * @author Nick Walker
 */
public class DrumTrack {
	public float tempo = 72;
	private String beat = "src/samples/072ChorusRide16thsF6b.mid";
	private MidiPlayer player;

	/**
	 * Sets the speed of the sequence
	 * @param beatsPerMinute
	 */
	public void setBPM(int beatsPerMinute){
		tempo = beatsPerMinute;
	}

	/**
	 * Sets the .midi file to use for the drum sequence
	 * @param track
	 */
	public void setBeat(String track){
		beat = track;
	}
	
	/**
	 * Loads the sequence, sets the tempo and begins play.
	 */
	public void run() {

		player = new MidiPlayer();

		// load a sequence
		Sequence sequence = player.getSequence(beat);
		sequence.createTrack();

		// play the sequence
		player.setBPMs(tempo);
		player.play(sequence, false);
	}
	
	public void stop(){
		player.stop();
	}

}
