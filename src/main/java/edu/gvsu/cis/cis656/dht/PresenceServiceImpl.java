package edu.gvsu.cis.cis656.dht;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Set;

import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import edu.gvsu.cis.cis656.utils.StringKey;

public class PresenceServiceImpl implements PresenceService
{
	private static boolean DEBUG = false;
    
	Chord chord;
	
	public PresenceServiceImpl(String host, int port, boolean isMaster){
		// Step 1: Load the Chord properties files. 
		PropertiesLoader.loadPropertyFile();
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
	
	private int getAvailablePort(){
		int port = 0;
		try{
		    ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}	  
		return port;
	}
	
	private void createNetwork(String host, int port)
	{
		System.out.println(">>>>>>>>Creating Chat Chord network on [" + host + "]");
		String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);
		URL localURL = null;
		try {
			localURL = new URL( protocol + "://" + host + ":" + String.valueOf(port) + "/");			
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
		int localPort = this.getAvailablePort();
		URL localURL = null;
		try {
			localURL = new URL( protocol + "://" + InetAddress.getLocalHost().getHostAddress() + ":" + String.valueOf(localPort) + "/");			
		} catch ( MalformedURLException e){
			throw new RuntimeException (e);
		} catch ( Exception ex) {
			throw new RuntimeException (ex);
		}
		
		URL bootstrapURL = null;
		try {
			bootstrapURL = new URL( protocol + "://" + host + ":" + String.valueOf(port) + "/");	
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
    
    private void put(StringKey key, RegistrationInfo reg){
    	try {
			this.del(key);
    		this.chord.insert(key, reg);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private Set<Serializable> get(StringKey key){
    	Set<Serializable> vals = null;
    	try {
			vals = this.chord.retrieve(key);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	return vals;
    }
    
    private void del(StringKey key){
    	try {
            Set<Serializable> vals = chord.retrieve(key);
            Iterator<Serializable> it = vals.iterator();
            while (it.hasNext()) {
            	RegistrationInfo user = (RegistrationInfo) it.next();
            	this.chord.remove(key, user);
                if(DEBUG)
					System.out.println("Deleted [" + user.toString() + "]");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
    
    public boolean register(RegistrationInfo reg){
    	boolean success = false;
    	if(this.lookup(reg.getName()) == null){
    		StringKey regKey = new StringKey (reg.getName());
    		this.put(regKey, reg);
    		success = true;
    	}
    	return success;
    }
    
    public boolean updateRegistrationInfo(RegistrationInfo reg){
    	boolean success = false;
    	StringKey regKey = new StringKey(reg.getName());
        this.put(regKey,reg);
    	return success;
    }
    
    public void unregister(String name){
    	StringKey regKey = new StringKey(name);
    	try {
    		this.del(regKey);
    		this.chord.leave();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public RegistrationInfo lookup(String name){
    	RegistrationInfo user = null;
		StringKey regKey = new StringKey(name);
		Set<Serializable> vals = this.get(regKey);
		if(vals != null){
			Iterator<Serializable> it = vals.iterator();
			while(it.hasNext()) {
				user = (RegistrationInfo) it.next();
				if(DEBUG)
					System.out.println("Found [" + user.toString() + "]");
			}
		}
    	return user;
    }
    
}
