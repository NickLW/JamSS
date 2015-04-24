package uk.ac.brighton.jamss;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import be.tarsos.dsp.util.fft.FFT;

/**
 * V0.9
 * This class links the 3 main panels, the Input panel, the metronome panel
 * and the pitch detection panel. The detected pitch is passed to the metronome.
 */
public class Jam extends JFrame implements PitchDetectionHandler {

	private static final long serialVersionUID = 1383896180290138076L;
	private final SpectrogramPanel panel;
	private final MetronomePanel metPanel;
	private AudioDispatcher dispatcher;
	private PitchEstimationAlgorithm algo;
	private double pitch; 
	private float sampleRate = 44100;
	private int bufferSize = 1024 * 4;
	private int overlap = 768 * 4 ;
	private String fileName;


	/**
	 * Constructor
	 * 
	 */
	public Jam(String fileName){
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("JamSS");
		panel = new SpectrogramPanel();
		metPanel = new MetronomePanel();
		algo = PitchEstimationAlgorithm.FFT_YIN;
		this.fileName = fileName;



		JPanel inputPanel = new InputPanel();
		
		/**
		 * Listener for selecting microphone input
		 */
		inputPanel.addPropertyChangeListener("mixer",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				try {
					setNewMixer((Mixer) arg0.getNewValue());
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		JPanel containerPanel = new JPanel(new GridLayout(1,0));
		containerPanel.add(inputPanel);
		containerPanel.add(metPanel);
		this.add(containerPanel,BorderLayout.NORTH);

		JPanel otherContainer = new JPanel(new BorderLayout());
		otherContainer.add(panel,BorderLayout.CENTER);
		otherContainer.setBorder(new TitledBorder("Current pitch"));


		this.add(otherContainer,BorderLayout.CENTER);
	}

	/**
	 * 
	 * @param mixer
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 * Mixer for obtaining input
	 */
	private void setNewMixer(Mixer mixer) throws LineUnavailableException, UnsupportedAudioFileException {

		if(dispatcher!= null){
			dispatcher.stop();
		}
		if(fileName == null){
			final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
					false);
			final DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, format);
			TargetDataLine line;
			line = (TargetDataLine) mixer.getLine(dataLineInfo);
			final int numberOfSamples = bufferSize;
			line.open(format, numberOfSamples);
			line.start();
			final AudioInputStream stream = new AudioInputStream(line);

			JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);
			// create a new dispatcher
			dispatcher = new AudioDispatcher(audioStream, bufferSize,
					overlap);
		} else {
			try {
				File audioFile = new File(fileName);
				dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);
				AudioFormat format = AudioSystem.getAudioFileFormat(audioFile).getFormat();
				dispatcher.addAudioProcessor(new AudioPlayer(format));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// add a processor, handle pitch event.
		dispatcher.addAudioProcessor(new PitchProcessor(algo, sampleRate, bufferSize, this));
		dispatcher.addAudioProcessor(fftProcessor);

		// run the dispatcher (on a new thread).
		new Thread(dispatcher,"Audio dispatching").start();
	}

	AudioProcessor fftProcessor = new AudioProcessor(){

		FFT fft = new FFT(bufferSize);
		float[] amplitudes = new float[bufferSize/2];

		@Override
		public void processingFinished() {
			// TODO Auto-generated method stub
		}

		/**
		 * draws current pitch and amplitude to spectrogram and sends pitch
		 * to the metronome
		 */
		@Override
		public boolean process(AudioEvent audioEvent) {
			float[] audioFloatBuffer = audioEvent.getFloatBuffer();
			float[] transformbuffer = new float[bufferSize*2];
			System.arraycopy(audioFloatBuffer, 0, transformbuffer, 0, audioFloatBuffer.length); 
			fft.forwardTransform(transformbuffer);
			fft.modulus(transformbuffer, amplitudes);
			panel.drawFFT(pitch, amplitudes,fft);
			panel.repaint();
			metPanel.getMetronome().setCurrNote(pitch);
			return true;
		}

	};

	/**
	 * Sets the pitch 
	 */
	@Override
	public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
		if(pitchDetectionResult.isPitched()){
			pitch = pitchDetectionResult.getPitch();

		} else {
			pitch = -1;
		}

	}

	public static void main(final String... strings) throws InterruptedException,
	InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// ignore failure to set default look and feel;
				}
				JFrame frame = strings.length == 0 ? new Jam(null) : new Jam(strings[0]) ;
				frame.pack();
				frame.setSize(800, 600);
				frame.setVisible(true);
			}
		});
	}
}
