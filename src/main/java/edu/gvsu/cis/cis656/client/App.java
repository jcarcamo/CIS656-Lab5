package edu.gvsu.cis.cis656.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import edu.gvsu.cis.cis656.client.comm.MessageListenerService;
import edu.gvsu.cis.cis656.client.comm.MessageSenderService;
import edu.gvsu.cis.cis656.dht.PresenceServiceImpl;
import edu.gvsu.cis.cis656.dht.RegistrationInfo;
import edu.gvsu.cis.cis656.utils.Utils;

public class App {
    
    
	public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
        	if(args.length < 1){
        		System.out.println("Wrong # of arguments");
        		System.out.println("usage:\n\tjava -jar openchord-chat-client -master|-notmaster {username} {bootstrap-host[:port]}");
        		System.exit(-1);
        	}
        	BufferedReader is = new BufferedReader(new InputStreamReader(System.in) );
            String masterHost = "localhost";
            int masterPort = 8080;
            
            boolean isMaster = args[0].intern() == "-master" ? true: false;
        	String userName = args[1];
            if(args.length == 2){
            	String[] hostAndPort =  args[2].split(":");
            	if(hostAndPort.length == 1){
            		masterHost = hostAndPort[0];
            	} else if(hostAndPort.length == 2){
            		masterHost = hostAndPort[0];
            		try{
                		masterPort = Integer.parseInt(hostAndPort[1]);
                	}catch(NumberFormatException nfe){
                		System.out.println("Port param must be a number");
                		System.exit(-1);
                	}
            	}else if(hostAndPort.length != 0){
            		System.out.println("Wrong # of arguments");
            		System.exit(-1);
            	}
            }
        	
            String line;
            String message;
            boolean finished = false;
            RegistrationInfo friend;
            ServerSocket listenerServer = null;
            
            listenerServer = new ServerSocket(0);
            
            Thread listenerService = new Thread(new MessageListenerService(listenerServer));
            listenerService.start();
            String localIP = Utils.getIPAddress();
            
            PresenceServiceImpl openchordClient = new PresenceServiceImpl(masterHost, masterPort, isMaster);
            
            RegistrationInfo myself = new RegistrationInfo(userName, localIP, listenerServer.getLocalPort(), true);
            
            if(!openchordClient.register(myself)){
            	System.out.println("User " + userName + " already connected, please use another name");
            	listenerService.interrupt();
            	System.exit(-1);
            }
            
            System.out.println("Chat App for " + myself.getName());
            
            while(true) {
        		System.out.println("Please type a command, or 'help' to see the list of available commands ");	
                line = is.readLine();
                String[] lineArray = line.split(" ");
                
                switch(lineArray[0]){
                case "talk":
                	if(lineArray.length > 2){
	                	System.out.println("Sending Message to " + lineArray[1]);
	                	friend = openchordClient.lookup(lineArray[1]);
	                	if(friend != null && friend.getStatus() == true){
	                		if(friend.getName().equalsIgnoreCase(myself.getName())){
	                			System.out.println("Funny that you're talking to yourself, but we allow that ;)");
	                		}
	                		message = Utils.stringFromArray(2, lineArray);
	                		message = "(DM) " + myself.getName() + " says: " + message;
	                		new Thread(new MessageSenderService(friend.getHost(), friend.getPort(), message)).start();
	                	}else{
	                		System.out.println("Friend unavailable");
	                	}
                	}else{
                		System.out.println("Invalid number of arguments for talk. Correct syntax is: \ntalk {name} {message}");
                	}
                	
                	break;
                case "busy":
                	if(myself.getStatus()){
                		System.out.println("Your are busy now");
                		myself.setStatus(false);
                        openchordClient.updateRegistrationInfo(myself);
                	}else{
                		System.out.println("No action taken, you were already busy");
                	}
                	break;
                case "available":
                	if(!myself.getStatus()){
	                    System.out.println("Your are available now");
	                    myself.setStatus(true);
	                    openchordClient.updateRegistrationInfo(myself);
                	}else{
                		System.out.println("No action taken, you were already available");
                	}
                    break;
                case "help":
                    System.out.println("Here's a list of commands:");
                    System.out.println("talk {name} {message}: Sends a message to your friend if available");
                    System.out.println("busy: Sets your status to busy");
                    System.out.println("available: Sets your status to available");
                    System.out.println("exit: Close the Chat App");
                	break;
                case "exit":
                	listenerService.interrupt();
                	openchordClient.unregister(userName);
                	finished = true;
                	break;
                default:
                	System.out.println("Not a recognized command. Type 'help' to see the list of available commands");
                	break;
                	
                }
                if(finished){
                	break;
                }
            }
        	System.out.println("Bye");
        	System.exit(0);
             
        } catch (IOException e) {
        	e.printStackTrace();
        	System.exit(-1);
        } catch (Exception e) {
            System.err.println("Chat App exception:");
            e.printStackTrace();
        }
    }
}
