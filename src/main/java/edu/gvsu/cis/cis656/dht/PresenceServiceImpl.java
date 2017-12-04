package edu.gvsu.cis.cis656.dht;

import java.net.InetAddress;
import java.net.MalformedURLException;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;

public class PresenceServiceImpl implements PresenceService
{
	//private static Logger logger = Logger.getLogger("edu.gvsu.cis.cis656.dht.PresenceServiceImpl");
	//private static boolean DEBUG = false;
    
	Chord chord;
	
	public PresenceServiceImpl(String host, int port, boolean isMaster){
		try {
			if(isMaster) {
				this.createNetwork(host, port);

			} else {
				this.joinNetwork(host, port);				
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createNetwork(String host, int port)
	{
		System.out.println(">>>>>>>>Creating Chat Chord network on [" + host + "]");
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL( protocol + "://" + host + ":" + port + "/");			
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
	
	public void joinNetwork(String host, int port)
	
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
	
    public boolean register(RegistrationInfo reg){
    	boolean success = false;

    	return success;
    }
    
    public boolean updateRegistrationInfo(RegistrationInfo reg){
    	boolean success = false;

    	return success;
    }
    
    public void unregister(String name){
    	
    }
    
    public RegistrationInfo lookup(String name){
    	RegistrationInfo user = null;

    	return user;
    }
    

}
