package edu.gvsu.cis.cis656.client.comm;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class MessageSenderService implements Runnable {
	private String host;
	private int port;
	private String message;
	
	public MessageSenderService(String host, int port, String message){
		super();
		this.host = host;
		this.port = port;
		this.message = message;
	}
	
	@Override
	public void run() {
		PrintStream os;
		try {
			Socket friendSocket = new Socket(this.host, this.port);
			os = new PrintStream(friendSocket.getOutputStream());
			os.println(message);
			friendSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
