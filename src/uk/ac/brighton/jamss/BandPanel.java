package uk.ac.brighton.jamss;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * This class provides the GUI for the Band. Each of the 'chooser' boxes
 * takes a list generated from it's associated 'choice' class and passes the
 * user selection onto the Band.
 * @author Nick Walker
 */
public class BandPanel extends JPanel {
	private static final long serialVersionUID = -8405027332103503281L;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JToggleButton playButton;
	private javax.swing.JComboBox scaleChooser;
	private javax.swing.JComboBox rootChooser;
	private javax.swing.JComboBox drumChooser;
	private javax.swing.JComboBox leadChooser;
	private javax.swing.JSlider tempoChooser;
	private static Band band;
	// End of variables declaration//GEN-END:variables
	
	/**
	 * Create a frame and panel for the UI
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame("Settings");
		final JPanel bnd = new BandPanel(band);
		bnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		f.getContentPane().add(bnd, BorderLayout.CENTER);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
	}
	
	/**
	 * Constructor
	 */
	public BandPanel(Band theBand){
		band = theBand;
		initComponents();
		playButton.requestFocus();
	}
	
	/**
	 * Sets the text for the start button
	 */
	private void setPlayButtonText(int beatsPerMinute) {
		playButton.setText(Integer.toString(beatsPerMinute) + "bpm JAM!");
	}
	
	/** This method is called from within the constructor to
	 * initialise the form.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		playButton = new javax.swing.JToggleButton();
		scaleChooser = new javax.swing.JComboBox();
		rootChooser = new javax.swing.JComboBox();
		drumChooser = new javax.swing.JComboBox();
		leadChooser = new javax.swing.JComboBox();
		tempoChooser = new javax.swing.JSlider();

		setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));
		setLayout(new java.awt.GridBagLayout());

		//FOR PLAY BUTTON
		playButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		playButton.setText("72bpm JAM!");
		playButton.setToolTipText("Start and stop the jam session");
		playButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				metronomeButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
		add(playButton, gridBagConstraints);

		//FOR SCALE
		scaleChooser.setModel(new javax.swing.DefaultComboBoxModel(ScaleChoice.getSChoice()));
		scaleChooser.setToolTipText("Select the scale to use");
		scaleChooser.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				soundChooserActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		add(scaleChooser, gridBagConstraints);

		//FOR ROOT NOTE CHOICE
		rootChooser.setModel(new javax.swing.DefaultComboBoxModel(RootChoice.getRChoice()));
		rootChooser.setToolTipText("Select the root to use");
		rootChooser.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				rootChooserActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		add(rootChooser, gridBagConstraints);

		//FOR DRUMS CHOICE
		drumChooser.setModel(new javax.swing.DefaultComboBoxModel(DrumChoice.getDChoice()));
		drumChooser.setToolTipText("Select the drums to use");
		drumChooser.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				drumChooserActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(drumChooser, gridBagConstraints);

		//FOR LEAD
		leadChooser.setModel(new javax.swing.DefaultComboBoxModel(LeadChoice.getSounds()));
		leadChooser.setToolTipText("Select the sound to use");
		leadChooser.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				leadChooserActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(leadChooser, gridBagConstraints);

		//FOR TEMPO
		tempoChooser.setMaximum(160);
		tempoChooser.setMinimum(40);
		tempoChooser.setValue(72);
		tempoChooser.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				tempoChooserStateChanged(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
		add(tempoChooser, gridBagConstraints);
	}

	private void metronomeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metronomeButtonActionPerformed

		if (playButton.isSelected()) {
			band.startThread();
		} else {
			band.stop();
		}

	}

	private void soundChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooserActionPerformed
		band.setScaleFromChoice((((ScaleChoice)scaleChooser.getSelectedItem()).getName()));
	}

	private void rootChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooserActionPerformed
		band.setRootFromChoice((((RootChoice)rootChooser.getSelectedItem()).getName()));
	}

	private void drumChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooserActionPerformed
		band.setDrumFromChoice((((DrumChoice)drumChooser.getSelectedItem()).getName()));
	}

	private void leadChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundChooserActionPerformed
		band.setLeadFromChoice(((LeadChoice)leadChooser.getSelectedItem()).getMidiSound());
	}

	private void tempoChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tempoChooserStateChanged
		final int tempo = tempoChooser.getValue();
		if (((JSlider) evt.getSource()).getValueIsAdjusting()) {
			if (playButton.isSelected()){
				band.stop();
			}
			setPlayButtonText(tempo);
		} else {
			band.processTempoChange(tempo);
			if (playButton.isSelected()) {
				band.startThread();
			}
		}
	}
}
