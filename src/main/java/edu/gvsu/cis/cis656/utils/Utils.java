package edu.gvsu.cis.cis656.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;

import edu.gvsu.cis.cis656.dht.RegistrationInfo;

public class Utils {
	public static void printVector(Vector<RegistrationInfo> registeredClients){
    	for(RegistrationInfo reg:registeredClients){
    		String status = "busy";
    		if(reg.getStatus()){
    			status = "available";
    		}
    		System.out.println("Name: " + reg.getName() + "\t\t\tStatus: " + status);
    	}
    }
	
    public static String stringFromArray(int offset, String[] command){
    	StringBuilder buffer = new StringBuilder ();
    	String delim = "";
    	for (int i = offset; i < command.length; i++)
    	{
    	    buffer.append (delim);
    	    delim = " "; // Avoid if(); assignment is very fast!
    	    buffer.append (command[i]);
    	}
    	return buffer.toString();
    }
    
    public static String getIPAddress(){
    	String localIP = "127.0.0.1";
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	        while(e.hasMoreElements())
	        {
	            NetworkInterface n = (NetworkInterface) e.nextElement();
	            Enumeration<InetAddress> ee = n.getInetAddresses();
	            while (ee.hasMoreElements())
	            {
	                InetAddress i = (InetAddress) ee.nextElement();
	                if(i.getHostAddress().startsWith("35.") || i.getHostAddress().startsWith("192.")){
	                	localIP = i.getHostAddress();
	                }
	            }
	        }
		} catch (UnknownHostException | SocketException e1) {
			e1.printStackTrace();
		}
        return localIP;
    }
}
