package uk.ac.brighton.jamss;
/*
DEVELOPING GAME IN JAVA 

Caracteristiques

Editeur : NEW RIDERS 
Auteur : BRACKEEN 
Parution : 09 2003 
Pages : 972 
Isbn : 1-59273-005-1 
Reliure : Paperback 
Disponibilite : Disponible a la librairie 
 */


import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * An example that plays a Midi sequence. First, the sequence is played once
 * with track 1 turned off. Then the sequence is played once with track 1 turned
 * on. Track 1 is the drum track in the example midi file.
 */
public class DrumTrack {
	public float tempo = 72;
	private String beat = "src/samples/072ChorusRide16thsF6b.mid";
	public MidiPlayer player;

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
	 * 
	 */
	public void run() {

		player = new MidiPlayer();

		// load a sequence
		Sequence sequence = player.getSequence(beat);
		sequence.createTrack();
		Sequencer sequencer = player.getSequencer();

		try {
			sequencer.setSequence(sequence);
		} catch (InvalidMidiDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sequencer.open();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// play the sequence
		sequencer.setTempoInBPM(tempo);
		player.setBPMs(tempo);
		player.play(sequence, false);
	}
	
	public void stop(){
		player.stop();
	}

}
