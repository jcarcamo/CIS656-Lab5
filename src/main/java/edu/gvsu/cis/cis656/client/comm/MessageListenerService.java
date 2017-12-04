package edu.gvsu.cis.cis656.client.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageListenerService implements Runnable {
	private ServerSocket listenerServer = null;
	
	public MessageListenerService(ServerSocket listenerServer ){
		super();
		this.listenerServer = listenerServer;
	}
	
	@Override
	public void run() {
		Socket senderSocket = null;   
		while(!Thread.currentThread().isInterrupted()) {
            try {
                senderSocket = listenerServer.accept();
                Thread thread  = new Thread(new ProcessIncomingRequest(senderSocket));
                thread.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
}
