

import java.awt.BorderLayout;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
//import org.apache.log4j.Logger;

/**
 * A metronome.
 * @author  Dave Briccetti
 */
public class Keys extends JPanel {

    //static Logger log = Logger.getLogger(Metronome.class);
	private final int instrument = 25;
    private final int velocity = 75;
    private Thread thread; // New thread each time the metronome is turned on
    private final Runnable runnable = createRunnable();
    private long timeBetweenBeats;
    private MidiChannel channel = null;
    private boolean keepPlaying;
    public int note;
    public int note2;
    public int note3;
    public int currNote = 30;
    public MidiTest drums = new MidiTest();
    boolean drumsOn = false;
    public int count = 1;
    public int barCount = 0;
    
    
    private static final double[] FREQUENCIES = { 174.61, 164.81, 155.56, 146.83, 138.59, 130.81, 123.47, 116.54, 110.00, 103.83, 98.00, 92.50, 87.31, 82.41, 77.78};
    private static final String[] NAME        = { "F",    "E",    "D#",   "D",    "C#",   "C",    "B",    "A#",   "A",    "G#",   "G",   "F#",  "F",   "E",   "D#"};
	


    public static void main(String[] args) {
        JFrame f = new JFrame("DBSchools Metronome");
        final JPanel met = new Metronome();
        met.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        f.getContentPane().add(met, BorderLayout.CENTER);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
    }

    /** Creates new form Metronome */
    public Keys() {
        try {
        	MidiDevice.Info[] midiDeviceInfoArray = MidiSystem.getMidiDeviceInfo();
        	MidiDevice device;
        	device = MidiSystem.getMidiDevice(midiDeviceInfoArray[2]);
            final Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            //synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
            channel = synthesizer.getChannels()[2];
            channel.programChange(0, 81);
            Instrument[] instr = synthesizer.getDefaultSoundbank().getInstruments();
            System.out.println(instr[50]);
            synthesizer.loadInstrument(instr[81]);
            
          //  MidiEvent event = null;
    	    
    	   // ShortMessage first = new ShortMessage();
    	   // first.setMessage(192, 1, 25, 0);
    	   // MidiEvent changeInstrument = new MidiEvent(first, 1);
            
        } catch (MidiUnavailableException ex) {
            //log.error(ex);
        }
        initComponents();
        setTempo(72);
        setNoteFromChoice();
        metronomeButton.requestFocus();
    }

    /**
     * Sets the tempo. May be called while the metronome is on.
     * @param beatsPerMinute the tempo, in beats per minute
     */
    public void setTempo(int beatsPerMinute) {
        processTempoChange(beatsPerMinute);
        tempoChooser.setValue(beatsPerMinute);
        
    }

    /**
     * Sets the MIDI note, in the percussion channel, to use for the 
     * metronome sound. See http://en.wikipedia.org/wiki/General_MIDI. 
     * @param note the MIDI note to use
     */
    public void setNote(int note) {
        this.note = note;
        this.note2 = note + 4;
        this.note3 = note + 7;
    }
    
    public void setCurrNote(double pitch){
    	if (pitch != -1) {
            
            double frequency = normaliseFreq(pitch);
            int note = closestNote(frequency);
            
           /* String n = (NAME[note]);
            switch (note){
            case 0 : this.currNote = 63;
            break;
            case 1 : this.currNote = 64;
            break;
            
            
            } */
            
            if (NAME[note] == "F"){
            	this.currNote = 65;
            } else if (NAME[note] == "E"){
            	this.currNote = 64;
            } /*else if (NAME[note] == "D#"){
            	this.currNote = 63;
            }*/ else if (NAME[note] == "D"){
            	this.currNote = 62;
            } /*else if (NAME[note] == "C#"){
            	this.currNote = 61;
            }*/ else if (NAME[note] == "C"){
            	this.currNote = 60;
            } else if (NAME[note] == "B"){
            	this.currNote = 71;
            } /*else if (NAME[note] == "A#"){
            	this.currNote = 70;
            }*/ else if (NAME[note] == "A"){
            	this.currNote = 69;
            }/* else if (NAME[note] == "G#"){
            	this.currNote = 68;
            }*/ else if (NAME[note] == "G"){
            	this.currNote = 67;
            }/* else if (NAME[note] == "F#"){
            	this.currNote = 66;
            }*/ 

		}
    }
    
    private static double normaliseFreq(double hz) {
        // get hz into a standard range to make things easier to deal with
        while ( hz < 82.41 ) {
            hz = 2*hz;
        }
        while ( hz > 164.81 ) {
            hz = 0.5*hz;
        }
        return hz;
    }
    
    private static int closestNote(double hz) {
        double minDist = Double.MAX_VALUE;
        int minFreq = -1;
        for ( int i = 0; i < FREQUENCIES.length; i++ ) {
            double dist = Math.abs(FREQUENCIES[i]-hz);
            if ( dist < minDist ) {
                minDist=dist;
                minFreq=i;
            }
        }
        return minFreq;
    }
    
    
    

    /**
     * Stops the metronome.
     */
    public void stop() {
        keepPlaying = false;
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

        		while (keepPlaying) {
        			setNote(currNote);
        		// Someone could change note while we sleep. Make sure we 
        		// turn on and off the same note.
        		final int noteForThisBeat = note; 

        		 System.out.println ("late(+)/early(-): " + wokeLateOrEarlyBy);
        		
        		/*THIS BIT IS SORTING WHAT THE GUITARS PLAY AND WHEN!!*/
        		
        		//if (!played){
        		//if(barCount % 2 != 0){
        		 
        		 
        		 
        		 if (!drumsOn){
             		drums.run();
             		drumsOn = true;
             		}
	        		
	        		if (count % 2 == 0) {
		        		channel.noteOn(noteForThisBeat - octaves, velocity);
		        		channel.noteOn((noteForThisBeat + 4) - octaves, velocity);
		        		channel.noteOn((noteForThisBeat + 7) - octaves, velocity);
		        		count ++;
	        		} else {
	        			count ++;
	        		}
	        		
	        		
	        		
	        		if(count > 4){
		        	//	played = false;
		        		count = 1;
		        		barCount ++;
		        	//played = true;
		        	}
	        		
	        		
	        		
        		//} else {
        			/*if(count > 1) {
		        		channel.noteOn(noteForThisBeat - octaves, velocity);
		        		channel.noteOn((noteForThisBeat + 4) - octaves, velocity);
		        		channel.noteOn((noteForThisBeat + 7) - octaves, velocity);
		        		count ++;
	        		//played = true;
	        		} else {
	        			count ++;
	        		}
        			
        			if (count > 4){
    	        		//	played = false;
    	        		count = 1;
    	        		barCount ++;
    	        	}
        			
        		}*/
        		
        		
        		
        		
        		final long currentTimeBeforeSleep = System.currentTimeMillis();
        		//correct time to sleep by previous error, to keep the overall tempo
        		final long sleepTime = timeBetweenBeats - wokeLateOrEarlyBy;
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
        		if (barCount != 0 && barCount % 8 == 0 && count == 1){
        			//barCount = 1;
        			drumsOn = false;
        		}
        		}
        		// log.debug("Thread ending");
        		}
        };
    }

    private void processTempoChange(int beatsPerMinute) {
    	//float bpm = beatsPerMinute;
        setMetronomeButtonText(beatsPerMinute);
        timeBetweenBeats = 1000 * 60 / beatsPerMinute;
        restartAtEndOfBeatIfRunning();
        drums.setBPM(beatsPerMinute);
    }

    private void restartAtEndOfBeatIfRunning() {
        if (keepPlaying) {
            keepPlaying = false;
            try {
                thread.join();
            } catch (InterruptedException ex) {
                //log.debug(ex);
            }
            startThread();
        }
    }

    private void setMetronomeButtonText(int beatsPerMinute) {
        metronomeButton.setText(Integer.toString(beatsPerMinute));
    }

    private void startThread() {
        if (channel != null) {
            keepPlaying = true;
            thread = new Thread(runnable, "Metronome");
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
    }
    
    void setNoteFromChoice() {
        setNote(((PercussionSound)soundChooser.getSelectedItem()).getMidiNote());
    }

    static private class PercussionSound {
        private final String name;
        private final int midiNote;

        public PercussionSound(String name, int midiNote) {
            this.name = name;
            this.midiNote = midiNote;
        }

        public int getMidiNote() {
            return midiNote;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
        
    }
    
    private PercussionSound[] getSounds() {
        return new PercussionSound[] {
            new PercussionSound("Claves", 75),
            new PercussionSound("Cow Bell", 56),
            new PercussionSound("High Bongo", 60),
            new PercussionSound("Low Bongo", 61),
            new PercussionSound("High Wood Block", 76),
            new PercussionSound("Low Wood Block", 77),
        };
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        metronomeButton = new javax.swing.JToggleButton();
        soundChooser = new javax.swing.JComboBox();
        tempoChooser = new javax.swing.JSlider();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Metronome"));
        setLayout(new java.awt.GridBagLayout());

        metronomeButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        metronomeButton.setText("Beat");
        metronomeButton.setToolTipText("Start and stop the metronome");
        metronomeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metronomeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(metronomeButton, gridBagConstraints);

        soundChooser.setModel(new javax.swing.DefaultComboBoxModel(getSounds()));
        soundChooser.setToolTipText("Select the sound to use");
        soundChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundChooserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(soundChooser, gridBagConstraints);

        tempoChooser.setMaximum(208);
        tempoChooser.setMinimum(40);
        tempoChooser.setValue(72);
        tempoChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tempoChooserStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        add(tempoChooser, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

private void metronomeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metronomeButtonActionPerformed

    if (metronomeButton.isSelected()) {
        startThread();
    } else {
    	drums.player.stop();
    	drumsOn = false;
        stop();
    }

}//GEN-LAST:event_metronomeButtonActionPerformed

private void soundChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooserActionPerformed
    setNoteFromChoice();
}//GEN-LAST:event_soundChooserActionPerformed

private void tempoChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tempoChooserStateChanged
    final int tempo = tempoChooser.getValue();
    if (((JSlider) evt.getSource()).getValueIsAdjusting()) {
        setMetronomeButtonText(tempo);
    } else {
        processTempoChange(tempo);
    }
}//GEN-LAST:event_tempoChooserStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton metronomeButton;
    private javax.swing.JComboBox soundChooser;
    private javax.swing.JSlider tempoChooser;
    // End of variables declaration//GEN-END:variables

}