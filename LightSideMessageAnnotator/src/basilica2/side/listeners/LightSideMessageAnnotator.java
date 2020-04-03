package basilica2.side.listeners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import basilica2.agents.components.InputCoordinator;
import basilica2.agents.events.MessageEvent;
import basilica2.agents.listeners.BasilicaAdapter;
import basilica2.agents.listeners.BasilicaPreProcessor;
import edu.cmu.cs.lti.basilica2.core.Agent;
import edu.cmu.cs.lti.basilica2.core.Event;

public class LightSideMessageAnnotator extends BasilicaAdapter
{
	//String pathToLightSide = "/Users/researcher/Downloads/LightSide_2.3.1_20141107";
	//String pathToModel = "saved/test.xml";
	// String pathToLightSide = "../../LightSideMessageAnnotator/runtime/LightSide_2.3.1_20141107";
	String pathToLightSide = "/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/"; 
	// String pathToLightSide = "/Users/rcmurray/git/DANCEcollaborative/bazaar/LightSideMessageAnnotator/runtime/LightSide_2.3.1_20141107";
	// String pathToModel = "saved/gst_reasoning_model.model.xml";
	String pathToModel = "/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/models/negative.simple.model.xml";
	String predictionCommand = "/Users/rcmurray/git/DANCEcollaborative/bazaar/lightside/scripts/predict.sh";
	// String predictionCommand = "scripts/test.sh";
	
	OutputStream stdin;
	InputStream stderr;
	InputStream stdout;

	BufferedReader reader;
	BufferedWriter writer;

	Process process = null;

	public LightSideMessageAnnotator(Agent a)
	{
		super(a);
		
		pathToLightSide = getProperties().getProperty("pathToLightSide", pathToLightSide);
		System.err.println("pathToLightSide: " + pathToLightSide);
		pathToModel = getProperties().getProperty("pathToModel", pathToModel);
		System.err.println("pathToModel: " + pathToModel);
		predictionCommand = getProperties().getProperty("predictionCommand", predictionCommand);
		System.err.println("predictionCommand: " + predictionCommand);
		
		try
		{
			// File lightSideLocation = new File(pathToLightSide);
			// process = Runtime.getRuntime().exec(new String[] { predictionCommand, pathToModel }, null, lightSideLocation); // ORIG
			// process = Runtime.getRuntime().exec(predictionCommand, null, lightSideLocation); 
			// process=Runtime.getRuntime().exec(predictionCommand);
			
			/// test replacement for "process = ..." line above /// >>> getting Instantiation exception
			try {
				// process=Runtime.getRuntime().exec(predictionCommand);
				ProcessBuilder pb = new ProcessBuilder(predictionCommand);
				pb.inheritIO(); 
				process = pb.start();
				// process.waitFor();
				
			} 
			catch (Exception e)
			{
				System.err.println("LightSideMessageAnnotator, process creation error - place 1");
				e.printStackTrace();
			}
			// Process process = pb.start();
			///////////////////////////////////////////////////////
			
			/// test replacement for "process = ..." line above /// >>> getting Instantiation exception
			// ProcessBuilder pb = new ProcessBuilder(predictionCommand, pathToModel);
			// pb.directory(lightSideLocation);
			// What to do for the "null" in the original command? 
			// Process process = pb.start();
			///////////////////////////////////////////////////////
			
			/// test replacement for "process = ..." line above /// 
			// ProcessBuilder pb = new ProcessBuilder();
			// pb.command(predictionCommand);
			// pb.command(new String[] {predictionCommand,pathToModel});
			// pb.directory(lightSideLocation);
			// What to do for the "null" in the original command? 
			// Process process = pb.start();
			///////////////////////////////////////////////////////
			
			/// test replacement for "process = ..." line above /// 
			// ProcessBuilder pb = new ProcessBuilder(predictionCommand);
			// Process process = pb.start();
			///////////////////////////////////////////////////////
			
			Boolean isAlive = process.isAlive();
			if (isAlive) {
				System.err.println("LightSide process is alive");
			}
			else {
				System.err.println("LightSide process is NOT alive");			
			}
		} 
		catch (Exception e)
		{
			System.err.println("LightSideMessageAnnotator, process creation error - place 2");
			e.printStackTrace();
		}


		// stdin = process.getOutputStream();
		// stderr = process.getErrorStream();
		// stdout = process.getInputStream();

		reader = new BufferedReader(new InputStreamReader(stdout));
		writer = new BufferedWriter(new OutputStreamWriter(stdin));
		
		
		
		try {
			Boolean readerStatus = reader.ready();
			System.err.println("reader ready");
		}
		catch (IOException e) {
			System.err.println("reader NOT ready");
		}

	}

	/**
	 * @param source
	 *            the InputCoordinator - to push new events to. (Modified events
	 *            don't need to be re-pushed).
	 * @param event
	 *            an incoming event which matches one of this preprocessor's
	 *            advertised classes (see getPreprocessorEventClasses)
	 * 
	 *            Preprocess an incoming event, by modifying this event or
	 *            creating a new event in response. All original and new events
	 *            will be passed by the InputCoordinator to the second-stage
	 *            Reactors ("BasilicaListener" instances).
	 */
	@Override
	public void preProcessEvent(InputCoordinator source, Event event)
	{
		MessageEvent me = (MessageEvent) event;
		System.out.println(me);

		String text = me.getText();
		String label = annotateText(text);

		if (label != null)
		{
			me.addAnnotations(label);
			
		}
	}

	public String annotateText(String text)
	{
		String label = null;

		if(process != null) try
		{
			
			// ============== UPDATE ATTEMPT ==============
			try { 
				// System.err.println("LightSideMessageAnnotator, write text: " + text);  // TEMP
				// writer.write(text + "\n");
				writer.write(text);
				writer.newLine();
				// writer.flush();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.err.println("LightSideMessageAnnotator, write/newline error");
				e.printStackTrace();
			}
			
			try {
				String line = reader.readLine();
				if (line == null)
				{
					System.err.println("LightSideMessageAnnotator, read line: NULL");
				}	
				else {
					String[] response = line.split("\\s+");
					label = response[0];	
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				System.err.println("LightSideMessageAnnotator, read/label error");
				e.printStackTrace();
			}
			// ============== UPDATE ATTEMPT ==============
			
			
			/** ================= ORIG ==============
			// System.err.println("LightSideMessageAnnotator, write text: " + text);  // TEMP
			// writer.write(text + "\n");
			writer.write(text);
			writer.newLine();
			// writer.flush();
			 
			String line = reader.readLine();
			//  System.err.println("line = reader.readline(): " + line);        // TEMP
			if (line != null)
			{
				System.err.println("LightSideMessageAnnotator, read line: " + line);
				String[] response = line.split("\\s+");
				label = response[0];
			}
			else
			{
				System.err.println("response from LightSide is null!");
			}
			*/ // ================= ORIG ==============

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.err.println("LightSideMessageAnnotator: Null process");
			e.printStackTrace();
		}
		return label;
	}

	/**
	 * @return the classes of events that this Preprocessor cares about
	 */
	@Override
	public Class[] getPreprocessorEventClasses()
	{
		// only MessageEvents will be delivered to this watcher.
		return new Class[] { MessageEvent.class };
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		LightSideMessageAnnotator annotator = new LightSideMessageAnnotator(null);

		while (input.hasNext())
		{
			String text = input.nextLine();
			String label = annotator.annotateText(text);
			System.out.println("Label is " + label);
		}
		input.close();
	}

	@Override
	public Class[] getListenerEventClasses()
	{
		// no processing events.
		return new Class[]{};
	}

	@Override
	public void processEvent(InputCoordinator arg0, Event arg1)
	{
		//we do nothing
	}

}
