package edu.gvsu.cis.cis656.client;
/**
 * Some sample OpenChord code.  See OpenChord manual and javadocs for more detail.
 * @author Jonathan Engelsma
 */

import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import edu.gvsu.cis.cis656.utils.StringKey;

public class ChordClient {

	Chord chord;
	
    public ChordClient(String masterHost, boolean master)
    {
		try {
			if(master) {
				this.createNetwork(masterHost);

			} else {
				this.joinNetwork(masterHost);				
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    
	public void createNetwork(String host)
	{
		System.out.println(">>>>>>>>Creating Chord network on [" + host + "]");
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL( protocol + "://" + host + ":8080/");			
		} catch ( MalformedURLException e){
			throw new RuntimeException (e);
		}
		
		this.chord = new ChordImpl();
	
		try {
			this.chord.create( localURL );
			
			
		} catch ( ServiceException e) {
			throw new RuntimeException (" Could not create DHT!", e);
		}
		
	}
	
	public void joinNetwork(String host)
	
	{
		System.out.println(">>>>>>>>Joining Chord network on [" + host + "]");		
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);		
		URL localURL = null;
		try {
			localURL = new URL( protocol + "://" + InetAddress.getLocalHost().getHostAddress() + "/");			
		} catch ( MalformedURLException e){
			throw new RuntimeException (e);
		} catch ( Exception ex) {
			throw new RuntimeException (ex);
		}
		
		URL bootstrapURL = null;
		try {
			bootstrapURL = new URL( protocol + "://" + host + ":8080/");	
		} catch ( MalformedURLException e){
			throw new RuntimeException (e);
		}
		this.chord = new ChordImpl();
		try {
			this.chord.join( localURL , bootstrapURL );
		} catch ( ServiceException e) {
			throw new RuntimeException (" Could not join DHT!", e);
		}		
					
	}    
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.println("usage:\n\tjava ChordClient -master|-notmaster bootstrap-host");
			return;
		}
		
		// Step 1: Load the Chord properties files. 
		PropertiesLoader.loadPropertyFile();
		
		// Step 2: Create or join the Chord network.
		boolean theMaster = args[0].intern() == "-master" ? true: false;
		ChordClient client = new ChordClient(args[1], theMaster );
		
		
		// Step 3: do some key insert/retrieve operations.		
		try {
			

			if( theMaster) {
				
				// ok, just for fun we'll create a key and insert a value into the DHT.
				String keyVal = "A";
				String data = "Just an example.";
				StringKey myKey = new StringKey ( keyVal );
				client.chord.insert(myKey, data);
				
				// We want to keep the master process around long enough to start up a second
				// process to read what we just inserted!
				System.out.println("Waiting");
				System.in.read();
				System.out.println("Master client is exiting");

				
				
			} else {
				
				
				//  try to retrieve the data the master node inserted.
				StringKey myKey = new StringKey("A");
				
				// notice that we get a set of vals back... but if we make sure
				// only one item is inserted per key, we'll only get one item in our set.
				Set<Serializable> vals = client.chord.retrieve(myKey);
				Iterator<Serializable> it = vals.iterator();
				while(it.hasNext()) {
					String data = (String) it.next();
					System.out.println("Got [" + data + "]");
				}
				
				System.out.println("Client is exiting.");
			}
			
			// Step 4: client should inform the Chord network it is leaving, so it
			// can rearrange itself.
			client.chord.leave();
			
			
		} catch ( ServiceException e) {
			throw new RuntimeException (" Could not create DHT!", e);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
