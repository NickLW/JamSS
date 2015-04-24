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

	// The drum track in the example Midi file
	private static final int DRUM_TRACK = 1;
	public float tempo = 72;
	private String beat = "src/samples/072ChorusRide16thsF6b.mid";


	/*public static void main(String[] args) {
    new MidiTest().run();
  	}*/

	public MidiPlayer player;

	public void setBPM(int beatsPerMinute){
		tempo = beatsPerMinute;

	}

	public void setBeat(String track){
		beat = track;

	}
	
	public void run() {

		player = new MidiPlayer();

		// load a sequence
		Sequence sequence = player.getSequence(beat);
		sequence.createTrack();


		// turn off the drums
		System.out.println("Playing (without drums)...");
		Sequencer sequencer = player.getSequencer();
		sequencer.setTempoInBPM(tempo);

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
		sequencer.setTempoInBPM(tempo);

		//sequencer.setTrackMute(DRUM_TRACK, false);
		//sequencer.addMetaEventListener(this);

		// play the sequence
		sequencer.setTempoInBPM(tempo);
		player.setBPMs(tempo);
		player.play(sequence, false);
	}
	
	public void stop(){
		player.stop();
	}

}
