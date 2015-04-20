

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


public class Jam extends JFrame implements PitchDetectionHandler {

	/**
	 * V0.9
	 */
	private static final long serialVersionUID = 1383896180290138076L;
	private final SpectrogramPanel panel;
	private final Metronome metronome;
	private static Keys keys;
	private final MidiTest drums;
	private AudioDispatcher dispatcher;
	private Mixer currentMixer;	
	private PitchEstimationAlgorithm algo;
	private double pitch; 

	private float sampleRate = 44100;
	private int bufferSize = 1024 * 4;
	private int overlap = 768 * 4 ;

	private String fileName;

	private static final double[] FREQUENCIES = { 174.61, 164.81, 155.56, 146.83, 138.59, 130.81, 123.47, 116.54, 110.00, 103.83, 98.00, 92.50, 87.31, 82.41, 77.78};
	private static final String[] NAME        = { "F",    "E",    "D#",   "D",    "C#",   "C",    "B",    "A#",   "A",    "G#",   "G",   "F#",  "F",   "E",   "D#"};



	private ActionListener algoChangeListener = new ActionListener(){
		@Override
		public void actionPerformed(final ActionEvent e) {
			String name = e.getActionCommand();
			PitchEstimationAlgorithm newAlgo = PitchEstimationAlgorithm.valueOf(name);
			algo = newAlgo;
			try {
				setNewMixer(currentMixer);
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			}
		}};

		public Jam(String fileName){
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("Spectrogram");
			panel = new SpectrogramPanel();
			metronome = new Metronome();
			keys = new Keys();
			drums = new MidiTest();
			algo = PitchEstimationAlgorithm.FFT_YIN;
			this.fileName = fileName;



			JPanel inputPanel = new InputPanel();

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
			containerPanel.add(metronome);
			this.add(containerPanel,BorderLayout.NORTH);

			JPanel otherContainer = new JPanel(new BorderLayout());
			otherContainer.add(panel,BorderLayout.CENTER);
			otherContainer.setBorder(new TitledBorder("3. Utter a sound (whistling works best)"));


			this.add(otherContainer,BorderLayout.CENTER);
		}


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
			currentMixer = mixer;

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

			@Override
			public boolean process(AudioEvent audioEvent) {
				float[] audioFloatBuffer = audioEvent.getFloatBuffer();
				float[] transformbuffer = new float[bufferSize*2];
				System.arraycopy(audioFloatBuffer, 0, transformbuffer, 0, audioFloatBuffer.length); 
				fft.forwardTransform(transformbuffer);
				fft.modulus(transformbuffer, amplitudes);
				panel.drawFFT(pitch, amplitudes,fft);
				panel.repaint();
				metronome.setCurrNote(pitch);
				keys.setCurrNote(pitch);
				return true;
			}

		};

		@Override
		public void handlePitch(PitchDetectionResult pitchDetectionResult,AudioEvent audioEvent) {
			if(pitchDetectionResult.isPitched()){
				pitch = pitchDetectionResult.getPitch();

			} else {
				pitch = -1;
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

		public String setNote() {
			return panel.currentPitch;// metronome.note;
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
						// ignore failure to set default look en feel;
					}
					JFrame frame = strings.length == 0 ? new Jam(null) : new Jam(strings[0]) ;
					frame.pack();
					frame.setSize(800, 600);
					frame.setVisible(true);
				}
			});

		}


		public static void startKeys(int root, long bpm) {
			keys.sleepTime = bpm / 4;
			//keys.count = 1;
			//keys.barCount = 0;
			keys.setNote(root);
			keys.startThread();	
		}

		public static void stopKeys() {
			keys.stop();	
		}

}
