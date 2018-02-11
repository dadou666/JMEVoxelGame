package dadou.son;

import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;

import dadou.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The <code>ExamplePlayer</code> thread class will simply download and play OGG
 * media. All you need to do is supply a valid URL as the first argument.
 * 
 * @author Jon Kristensen
 * @version 1.0
 */
public class OggEmeteur implements Runnable {
	// If you wish to debug this source, please set the variable below to true.
	private final boolean debugMode = false;
	public Emeteur emeteur;
	public DataLine.Info datalineInfo;
	/*
	 * URLConnection and InputStream objects so that we can open a connection to
	 * the media file.
	 */
	private byte[] bytes;
	private InputStream inputStream = null;

	/*
	 * We need a buffer, it's size, a count to know how many bytes we have read
	 * and an index to keep track of where we are. This is standard networking
	 * stuff used with read().
	 */
	public byte[] buffer = null;
	int bufferSize = 2048;
	int count = 0;
	int index = 0;

	/*
	 * JOgg and JOrbis require fields for the converted buffer. This is a buffer
	 * that is modified in regards to the number of audio channels. Naturally,
	 * it will also need a size.
	 */
	public byte[] convertedBuffer;
	int convertedBufferSize;

	// The source data line onto which data can be written.
	private SourceDataLine outputLine = null;

	// A three-dimensional an array with PCM information.
	private float[][][] pcmInfo;

	// The index for the PCM information.
	private int[] pcmIndex;

	// Here are the four required JOgg objects...
	private Packet joggPacket = new Packet();
	private Page joggPage = new Page();
	private StreamState joggStreamState = new StreamState();
	private SyncState joggSyncState = new SyncState();

	// ... followed by the four required JOrbis objects.
	private DspState jorbisDspState = new DspState();
	private Block jorbisBlock = new Block(jorbisDspState);
	private Comment jorbisComment = new Comment();
	private Info jorbisInfo = new Info();

	public void createObject() {

		// joggPacket = new Packet();
		// joggPage = new Page();
		// joggStreamState = new StreamState();
		joggSyncState = new SyncState();

		// ... followed by the four required JOrbis objects.
		// jorbisDspState = new DspState();
		// jorbisBlock = new Block(jorbisDspState);
		// jorbisComment = new Comment();
		// jorbisInfo = new Info();
	}

	private void cleanUp() {
		debugOutput("Cleaning up.");

		// Clear the necessary JOgg/JOrbis objects.
		joggStreamState.reset();
		// joggStreamState.clear();
		// jorbisBlock.clear();
		// jorbisDspState.clear();
		// jorbisInfo.clear();

		joggSyncState.clear();
		int channels = jorbisInfo.channels;
		int rate = jorbisInfo.rate;
		pcmInfo = new float[1][][];
		pcmIndex = new int[jorbisInfo.channels];

		// Creates an AudioFormat object and a DataLine.Info object.
		/*
		 * AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels,
		 * true, false); try { outputLine.close(); outputLine = (SourceDataLine)
		 * AudioSystem.getLine(datalineInfo); outputLine.open(audioFormat); }
		 * catch (LineUnavailableException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		if (this.outputLine != null) {
		//	Log.print("flush=");
			this.outputLine.flush();
		}
	
		debugOutput("Done cleaning up.");
	}

	/**
	 * The programs <code>main()</code> method. Will read the first command-line
	 * argument and use it as URL, after which it will start the thread.
	 * 
	 * @param args
	 *            command-line arguments
	 */
	public void initObjects() {

		count = 0;
		index = 0;

	}

	public static void main(String[] args) {

		File fileMusique = new File("C:\\tmp\\nightwish_storytime.ogg");

		File file = new File("C:\\tmp\\echantillon_claque.ogg");
		OggEmeteur examplePlayer;
		OggEmeteur exampleMusique;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			examplePlayer = new OggEmeteur(file);
			exampleMusique = new OggEmeteur(fileMusique);
			new Thread(examplePlayer).start();
			new Thread(exampleMusique).start();
			exampleMusique.demarer(0.0f);
			// exampleMusique.volume(0.0f);
			float volume = 0.0f;
			while ((line = stdin.readLine()) != null) {
				if (line.equals("+")) {
					volume += 0.01f;
					System.out.println(" volume =" + volume);
					exampleMusique.volume(volume);

				}
				if (line.equals("-")) {
					volume -= 0.01f;
					System.out.println(" volume =" + volume);
					exampleMusique.volume(volume);
				}
				if (line.equals("START")) {
					examplePlayer.demarer(1.0f);
				}
				if (line.equals("PAUSE")) {
					examplePlayer.pause();
				}
				if (line.equals("CONTINUE")) {
					examplePlayer.continuer();
				}
				if (line.equals("STOP")) {
					examplePlayer.stop();
					exampleMusique.stop();
					return;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * The constructor; will configure the <code>InputStream</code>.
	 * 
	 * @param pUrl
	 *            the URL to be opened
	 * @throws IOException
	 */
	public OggEmeteur(File pUrl) throws IOException {

		attente = new Semaphore(0);
		Path path = Paths.get(pUrl.toURI());
		bytes = Files.readAllBytes(path);

	}

	/**
	 * Given a string, <code>getUrl()</code> will return an URL object.
	 * 
	 * @param pUrl
	 *            the URL to be opened
	 * @return the URL object
	 */
	public URL getUrl(String pUrl) {
		URL url = null;

		try {
			url = new URL(pUrl);
		} catch (MalformedURLException exception) {
			System.err.println("Malformed \"url\" parameter: \"" + pUrl + "\"");
		}

		return url;
	}

	/**
	 * Sets the <code>inputStream</code> object by taking an URL, opens a
	 * connection to it and get the <code>InputStream</code>.
	 * 
	 * @param pUrl
	 *            the url to the media file
	 */

	/**
	 * This method is probably easiest understood by looking at the body.
	 * However, it will - if no problems occur - call methods to initialize the
	 * JOgg JOrbis libraries, read the header, initialize the sound system, read
	 * the body of the stream and clean up.
	 */

	Semaphore attente;
	boolean enCours = false;

	public boolean enCours() {
		return enCours;
	}

	float volumeInit = 0.0f;

	public void demarer(float volumeInit) {
		synchronized (this) {
			if (enCours) {
				return;
			}
			this.volumeInit = volumeInit;
			enCours = true;

			attente.release();

		}
	}

	int etat = 2;
	static int PAUSE = 0;
	static int STOP = 1;
	static int CONTINUE = 2;

	public void continuer() {
		etat = CONTINUE;
	}

	public void pause() {

		etat = PAUSE;
	}

	public void stop() {

		attente.release();
		etat = STOP;
	}
	
	public void run() {
	
	
	
		while (true) {
			attente.acquireUninterruptibly();

			if (etat == STOP) {
				cleanUp();
				if (this.outputLine != null) {
				this.outputLine.close(); }
				//Log.print("STOP"+this);
				return;
			}
			// Initialize JOrbis.{
			// this.joggSyncState.clear();

			initializeJOrbis();
			
			if (this.inputStream != null) {
				try {
					this.inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			this.inputStream = new ByteArrayInputStream(bytes);
			/*
			 * If we can read the header, we try to inialize the sound system.
			 * If we could initialize the sound system, we try to read the body.
			 */
			// Log.print("Joueur son");
			//
			if (readHeader()) {
				this.initializeSound();

				this.volume(volumeInit);
				try {
					readBody();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
				}

			// Afterwards, we clean up.

			this.initObjects();
			if (etat == STOP) {
				cleanUp();
				if (this.outputLine != null) {
					this.outputLine.close(); }
		//		Log.print("STOP"+this);
				return;
			}

			synchronized (this) {
				enCours = false;

			}

			if (emeteur != null) {
				emeteur.finSon();

			}
		}
	}

	/**
	 * Initializes JOrbis. First, we initialize the <code>SyncState</code>
	 * object. After that, we prepare the <code>SyncState</code> buffer. Then we
	 * "initialize" our buffer, taking the data in <code>SyncState</code>.
	 */
	private void initializeJOrbis() {
		debugOutput("Initializing JOrbis.");
		this.cleanUp();
		this.createObject();

		// Initialize SyncState
		joggSyncState.init();

		// Prepare the to SyncState internal buffer
		joggSyncState.buffer(bufferSize);

		/*
		 * Fill the buffer with the data from SyncState's internal buffer. Note
		 * how the size of this new buffer is different from bufferSize.
		 */
		buffer = joggSyncState.data;

		debugOutput("Done initializing JOrbis.");
	}

	/**
	 * This method reads the header of the stream, which consists of three
	 * packets.
	 * 
	 * @return true if the header was successfully read, false otherwise
	 */
	private boolean readHeader() {
		debugOutput("Starting to read the header.");

		/*
		 * Variable used in loops below. While we need more data, we will
		 * continue to read from the InputStream.
		 */
		boolean needMoreData = true;

		/*
		 * We will read the first three packets of the header. We start off by
		 * defining packet = 1 and increment that value whenever we have
		 * successfully read another packet.
		 */
		int packet = 1;

		/*
		 * While we need more data (which we do until we have read the three
		 * header packets), this loop reads from the stream and has a big
		 * <code>switch</code> statement which does what it's supposed to do in
		 * regards to the current packet.
		 */
		while (needMoreData) {
			// Read from the InputStream.
			try {
				count = inputStream.read(buffer, index, bufferSize);
			} catch (IOException exception) {
				System.err.println("Could not read from the input stream.");
				exception.printStackTrace();
			}
			if (count == 0) {
				return true;
			}
			// We let SyncState know how many bytes we read.
			joggSyncState.wrote(count);

			/*
			 * We want to read the first three packets. For the first packet, we
			 * need to initialize the StreamState object and a couple of other
			 * things. For packet two and three, the procedure is the same: we
			 * take out a page, and then we take out the packet.
			 */
			switch (packet) {
			// The first packet.
			case 1: {
				// We take out a page.
				switch (joggSyncState.pageout(joggPage)) {
				// If there is a hole in the data, we must exit.
				case -1: {
					System.err.println("There is a hole in the first " + "packet data.");
					return false;
				}

				// If we need more data, we break to get it.
				case 0: {
					break;
				}

				/*
				 * We got where we wanted. We have successfully read the first
				 * packet, and we will now initialize and reset StreamState, and
				 * initialize the Info and Comment objects. Afterwards we will
				 * check that the page doesn't contain any errors, that the
				 * packet doesn't contain any errors and that it's Vorbis data.
				 */
				case 1: {
					// Initializes and resets StreamState.
					joggStreamState.init(joggPage.serialno());
					joggStreamState.reset();

					// Initializes the Info and Comment objects.
					jorbisInfo.init();
					jorbisComment.init();

					// Check the page (serial number and stuff).
					if (joggStreamState.pagein(joggPage) == -1) {
						System.err.println("We got an error while " + "reading the first header page.");
						return false;
					}

					/*
					 * Try to extract a packet. All other return values than "1"
					 * indicates there's something wrong.
					 */
					if (joggStreamState.packetout(joggPacket) != 1) {
						System.err.println("We got an error while " + "reading the first header packet.");
						return false;
					}

					/*
					 * We give the packet to the Info object, so that it can
					 * extract the Comment-related information, among other
					 * things. If this fails, it's not Vorbis data.
					 */
					if (jorbisInfo.synthesis_headerin(jorbisComment, joggPacket) < 0) {
						System.err.println("We got an error while " + "interpreting the first packet. "
								+ "Apparantly, it's not Vorbis data.");
						return false;
					}

					// We're done here, let's increment "packet".
					packet++;
					break;
				}
				}

				/*
				 * Note how we are NOT breaking here if we have proceeded to the
				 * second packet. We don't want to read from the input stream
				 * again if it's not necessary.
				 */
				if (packet == 1)
					break;
			}

			// The code for the second and third packets follow.
			case 2:
			case 3: {
				// Try to get a new page again.
				switch (joggSyncState.pageout(joggPage)) {
				// If there is a hole in the data, we must exit.
				case -1: {
					System.err.println("There is a hole in the second " + "or third packet data.");
					return false;
				}

				// If we need more data, we break to get it.
				case 0: {
					break;
				}

				/*
				 * Here is where we take the page, extract a packet and and (if
				 * everything goes well) give the information to the Info and
				 * Comment objects like we did above.
				 */
				case 1: {
					// Share the page with the StreamState object.
					joggStreamState.pagein(joggPage);

					/*
					 * Just like the switch(...packetout...) lines above.
					 */
					switch (joggStreamState.packetout(joggPacket)) {
					// If there is a hole in the data, we must exit.
					case -1: {
						System.err.println("There is a hole in the first" + "packet data.");
						return false;
					}

					// If we need more data, we break to get it.
					case 0: {
						break;
					}

					// We got a packet, let's process it.
					case 1: {
						/*
						 * Like above, we give the packet to the Info and
						 * Comment objects.
						 */
						jorbisInfo.synthesis_headerin(jorbisComment, joggPacket);

						// Increment packet.
						packet++;

						if (packet == 4) {
							/*
							 * There is no fourth packet, so we will just end
							 * the loop here.
							 */
							needMoreData = false;
						}

						break;
					}
					}

					break;
				}
				}

				break;
			}
			}

			// We get the new index and an updated buffer.
			index = joggSyncState.buffer(bufferSize);
			buffer = joggSyncState.data;

			/*
			 * If we need more data but can't get it, the stream doesn't contain
			 * enough information.
			 */
			if (count == 0 && needMoreData) {
				System.err.println("Not enough header data was supplied.");
				return false;
			}
		}

		debugOutput("Finished reading the header.");

		return true;
	}

	/**
	 * This method starts the sound system. It starts with initializing the
	 * <code>DspState</code> object, after which it sets up the
	 * <code>Block</code> object. Last but not least, it opens a line to the
	 * source data line.
	 * 
	 * @return true if the sound system was successfully started, false
	 *         otherwise
	 */

	public void volume(float v) {
		synchronized (this) {
			if (!this.enCours) {
				return;
			}
		}
		if (this.outputLine == null) {
			return;
		}
		SourceDataLine sdl = (SourceDataLine) this.outputLine;
		FloatControl control = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
		float gain = 20.0f * (float) Math.log10(v);
		gain = Math.max(Math.min(gain, control.getMaximum()), control.getMinimum());

		control.setValue(gain);

	}

	private boolean initializeSound() {
		if (datalineInfo != null) {

			return true;
		}
		debugOutput("Initializing the sound system.");

		// This buffer is used by the decoding method.
		convertedBufferSize = bufferSize * 2;
		convertedBuffer = new byte[convertedBufferSize];

		// Initializes the DSP synthesis.
		jorbisDspState.synthesis_init(jorbisInfo);

		// Make the Block object aware of the DSP.
		jorbisBlock.init(jorbisDspState);

		// Wee need to know the channels and rate.
		int channels = jorbisInfo.channels;
		int rate = jorbisInfo.rate;

		
		// Creates an AudioFormat object and a DataLine.Info object.
		AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, false);
		datalineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);

		// Check if the line is supported.
		if (!AudioSystem.isLineSupported(datalineInfo)) {
			System.err.println("Audio output line is not supported.");
			return false;
		}

		/*
		 * Everything seems to be alright. Let's try to open a line with the
		 * specified format and start the source data line.
		 */
		try {
			outputLine = (SourceDataLine) AudioSystem.getLine(datalineInfo);
			outputLine.open(audioFormat);
		} catch (Throwable exception) {
			System.out.println("The audio output line could not be opened due " + "to resource restrictions.");
			exception.printStackTrace();
			return false;
		}

		// Start it.
		outputLine.start();

		/*
		 * We create the PCM variables. The index is an array with the same
		 * length as the number of audio channels.
		 */
		pcmInfo = new float[1][][];
		pcmIndex = new int[jorbisInfo.channels];

		debugOutput("Done initializing the sound system.");

		return true;
	}

	/**
	 * This method reads the entire stream body. Whenever it extracts a packet,
	 * it will decode it by calling <code>decodeCurrentPacket()</code>.
	 * 
	 * @throws IOException
	 */
	private boolean readBody() throws IOException {
		debugOutput("Reading the body.");

		/*
		 * Variable used in loops below, like in readHeader(). While we need
		 * more data, we will continue to read from the InputStream.
		 */
		boolean needMoreData = true;

		while (needMoreData) {
			if (etat == STOP) {
				return true;
			}
			if (etat == CONTINUE) {

				switch (joggSyncState.pageout(joggPage)) {
				// If there is a hole in the data, we just proceed.
				case -1: {
					debugOutput("There is a hole in the data. We proceed.");
				}

				// If we need more data, we break to get it.
				case 0: {
					break;
				}

				// If we have successfully checked out a page, we continue.
				case 1: {
					// Give the page to the StreamState object.
					joggStreamState.pagein(joggPage);

					// If granulepos() returns "0", we don't need more data.
					if (joggPage.granulepos() == 0) {
						needMoreData = false;
						break;
					}

					// Here is where we process the packets.
					processPackets: while (true) {
						switch (joggStreamState.packetout(joggPacket)) {
						// Is it a hole in the data?
						case -1: {
							debugOutput("There is a hole in the data, we " + "continue though.");
						}

						// If we need more data, we break to get it.
						case 0: {
							break processPackets;
						}

						/*
						 * If we have the data we need, we decode the packet.
						 */
						case 1: {
							decodeCurrentPacket();
						}
						}
					}

					/*
					 * If the page is the end-of-stream, we don't need more
					 * data.
					 */
					if (joggPage.eos() != 0)
						needMoreData = false;
				}
				}

				// If we need more data...
				if (needMoreData) {
					// We get the new index and an updated buffer.
					index = joggSyncState.buffer(bufferSize);
					buffer = joggSyncState.data;

					// Read from the InputStream.
					if (index >= 0) {
						count = inputStream.read(buffer, index, Math.min(bufferSize, buffer.length - index - 1));

						// We let SyncState know how many bytes we read.{
						if (count > 0) {
							joggSyncState.wrote(count);
						}
					}
					// There's no more data in the stream.
					if (count <= 0 || index < 0)
						needMoreData = false;
				}
			}
		}
		debugOutput("Done reading the body.");
		return false;
	}

	/**
	 * A clean-up method, called when everything is finished. Clears the
	 * JOgg/JOrbis objects and closes the <code>InputStream</code>.
	 */

	/**
	 * Decodes the current packet and sends it to the audio output line.
	 */
	private void decodeCurrentPacket() {
		int samples;

		// Check that the packet is a audio data packet etc.
		if (jorbisBlock.synthesis(joggPacket) == 0) {
			// Give the block to the DspState object.
			jorbisDspState.synthesis_blockin(jorbisBlock);
		}

		// We need to know how many samples to process.
		int range;

		/*
		 * Get the PCM information and count the samples. And while these
		 * samples are more than zero...
		 */
		while ((samples = jorbisDspState.synthesis_pcmout(pcmInfo, pcmIndex)) > 0) {
			// We need to know for how many samples we are going to process.
			if (samples < convertedBufferSize) {
				range = samples;
			} else {
				range = convertedBufferSize;
			}

			// For each channel...
			for (int i = 0; i < jorbisInfo.channels; i++) {
				int sampleIndex = i * 2;

				// For every sample in our range...
				for (int j = 0; j < range; j++) {
					/*
					 * Get the PCM value for the channel at the correct
					 * position.
					 */
					int value = (int) (pcmInfo[0][i][pcmIndex[i] + j] * 32767);

					/*
					 * We make sure our value doesn't exceed or falls below
					 * +-32767.
					 */
					if (value > 32767) {
						value = 32767;
					}
					if (value < -32768) {
						value = -32768;
					}

					/*
					 * It the value is less than zero, we bitwise-or it with
					 * 32768 (which is 1000000000000000 = 10^15).
					 */
					if (value < 0)
						value = value | 32768;

					/*
					 * Take our value and split it into two, one with the last
					 * byte and one with the first byte.
					 */
					convertedBuffer[sampleIndex] = (byte) (value);
					convertedBuffer[sampleIndex + 1] = (byte) (value >>> 8);

					/*
					 * Move the sample index forward by two (since that's how
					 * many values we get at once) times the number of channels.
					 */
					sampleIndex += 2 * (jorbisInfo.channels);
				}
			}

			// Write the buffer to the audio output line.
			outputLine.write(convertedBuffer, 0, 2 * jorbisInfo.channels * range);

			// Update the DspState object.
			jorbisDspState.synthesis_read(range);
		}
	}

	/**
	 * This method is being called internally to output debug information
	 * whenever that is wanted.
	 * 
	 * @param output
	 *            the debug output information
	 */
	private void debugOutput(String output) {
		if (debugMode)
			System.out.println("Debug: " + output);
	}
}