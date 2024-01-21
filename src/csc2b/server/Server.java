package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Nkoana RRM
 * @version 7
 * BUKAServer class represents the main server for the ZEDEMHandler application.
 */
public class Server {

 private ServerSocket ss;
 private boolean running ;
 
 
 public Server(int port) {
	 try {
		 ss= new ServerSocket(port);
		 running=true;
		 startServer();
		 
	 }catch(IOException ex) {
		 ex.printStackTrace();
	 }
	 
 }
 
	private void startServer() {
	System.out.println("Starting server...");
	
	while(running) {
	try {
		Socket s= ss.accept();
		System.out.println("New connected client");
		ZEDEMHandler zh= new ZEDEMHandler(s);
		
		Thread t= new Thread(zh);
		t.start();
		
	} catch(IOException ex) {
		 ex.printStackTrace();
	 }
	}
	
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Server zs= new Server(2021);
	}

}
