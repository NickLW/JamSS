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


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

/**
 * An example that plays a Midi sequence. First, the sequence is played once
 * with track 1 turned off. Then the sequence is played once with track 1 turned
 * on. Track 1 is the drum track in the example midi file.
 */
public class MidiTest implements MetaEventListener {

  // The drum track in the example Midi file
  private static final int DRUM_TRACK = 1;
  public float tempo = 72;
  

  /*public static void main(String[] args) {
    new MidiTest().run();
  }*/

  public MidiPlayer player;
  
  public void setBPM(int beatsPerMinute){
	  tempo = beatsPerMinute;
	  
  }
   

  public void run() {

    player = new MidiPlayer();
    
    // load a sequence
    Sequence sequence = player.getSequence("src/samples/072ChorusRide16thsF6b.mid");
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
    sequencer.addMetaEventListener(this);
    
 // play the sequence
    sequencer.setTempoInBPM(tempo);
    player.setBPMs(tempo);
    player.play(sequence, false);
  }

  /**
   * This method is called by the sound system when a meta event occurs. In
   * this case, when the end-of-track meta event is received, the drum track
   * is turned on.
   */
  public void meta(MetaMessage event) {
    if (event.getType() == MidiPlayer.END_OF_TRACK_MESSAGE) {
      Sequencer sequencer = player.getSequencer();
      /*if (sequencer.getTrackMute(DRUM_TRACK)) {
        // turn on the drum track
        System.out.println("Turning on drums...");
        sequencer.setTrackMute(DRUM_TRACK, false);
      } else {
        // close the sequencer and exit
        System.out.println("End of loop...");
        //player.close();
        //System.exit(0);
      }*/
    }
  }

}

class MidiPlayer implements MetaEventListener {

  // Midi meta event
  public static final int END_OF_TRACK_MESSAGE = 47;

  private Sequencer sequencer;
  
  public float tempo;
  
  public void setBPMs(float beatsPerMinute){
	  tempo = beatsPerMinute;
  }

  private boolean loop;

  public boolean paused;

  /**
   * Creates a new MidiPlayer object.
   */
  public MidiPlayer() {
    try {
      sequencer = MidiSystem.getSequencer();
      sequencer.open();
      sequencer.addMetaEventListener(this);
      sequencer.setTempoInBPM(tempo);
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
        sequencer.setTempoInBPM(tempo);
        /*if(loop) {
            sequencer.setLoopStartPoint(0);
            sequencer.setLoopEndPoint(-1);
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.setTempoInBPM(tempo);
        }*/
        //sequencer.setTempoFactor(1.0F);
        sequencer.setTempoInBPM(tempo);
        sequencer.start();
        //sequencer.setTempoFactor(1.0F);
        sequencer.setTempoInBPM(tempo);
        
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
  public void meta(MetaMessage event) {
    if (event.getType() == END_OF_TRACK_MESSAGE) {
      if (sequencer != null && sequencer.isOpen() && loop) {
    	sequencer.setMicrosecondPosition(0);
    	sequencer.setTempoInBPM(tempo);
        sequencer.start();
        sequencer.setTempoInBPM(tempo);
      }
    }
  }

  /**
   * Stops the sequencer and resets its position to 0.
   */
  public void stop() {
    if (sequencer != null && sequencer.isOpen()) {
      sequencer.stop();
      sequencer.setMicrosecondPosition(0);
      sequencer.setTempoInBPM(tempo);
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
