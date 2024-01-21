package csc2b.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;



/**
 * ZEDEMHandler class represents the server-side handler for individual client connections.
 * It processes client requests and performs authentication, file listing, and file retrieval.
 * 
 * @author Nkoana RRM
 * @version 7
 */
public class ZEDEMHandler implements Runnable {
	
	private Socket s;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private PrintWriter pw;
	private BufferedReader br;

	
	/**
     * Constructor for the ZEDEMHandler.
     * 
     * @param s The socket representing the client connection.
     */
	public ZEDEMHandler(Socket s) {
		this.s=s;
		
		try {
			
			is= s.getInputStream();
			os= s.getOutputStream();
			
			dis= new DataInputStream(is);
			dos= new DataOutputStream(os);
			
			pw=new PrintWriter(os);
			br= new BufferedReader(new InputStreamReader(is));
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	
	}
	
	/**
     * Retrieves the filename associated with a given file ID from the audio list.
     * 
     * @param searchID The file ID to search for.
     * @return The filename associated with the file ID.
     */
	private String getFileNameID(String searchID) {
		String retr="";
		File pdfFileList= new File("data/server/List.txt");
		
		try {
			Scanner sc= new Scanner(pdfFileList);
			String line="";
			
			while(sc.hasNext()) {
				line= sc.nextLine();
				
				StringTokenizer st= new StringTokenizer(line);
				String id=st.nextToken();
				String fname=st.nextToken();
				
				if(id.equals(searchID)) {
					retr=fname;
				}	
		
			}
			sc.close();	
			
		}
		catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		
		return retr;
	}
	
	/**
     * Loads the Audio file list from the server.
     * 
     * @return A string containing the audio file list.
     */
	private String loadAudioList() {
		
		String ret="";
		
		try {
			Scanner sc= new Scanner(new File("./data/server/List.txt"));
			
			while(sc.hasNext()) {
				String pdf=sc.nextLine();
				ret+=pdf+"#";
			}
			
			System.out.println("Audio List loaded");
			sc.close();
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return ret;
		
	}


	
	 /**
	  *
     * Handles client requests and processes commands.
     */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("Handling client requests");
		boolean processing =true;
		
		
		try {
		while(processing) {
			
			String message=br.readLine();
			System.out.println("Message: "+message);
			
			
			StringTokenizer st=new StringTokenizer(message);
			String command=st.nextToken().toUpperCase();
			
			switch(command){
			
		//pull list
			case "PLAYLIST":
				pw.println(loadAudioList());
				pw.flush();
				break;
				
			case "BONJOUR":
				 String name = st.nextToken();
				    String password = st.nextToken();

				    if (isValidCredentials(name, password)) {
				        pw.println("JA Authentication successful");
				    } else {
				        pw.println("NEE Authentication failed");
				    }
				    pw.flush();
				    break;
				    
				    
			case "LOGOUT":
				  // Close the socket associated with this client
			    try {
			        s.close();
			        pw.flush();
			    } catch (IOException ex) {
			        ex.printStackTrace();
			    }
			    
			    // Exit the command processing loop for this client
			   processing = false;
				break;
				
			//download	
			case "ZEDEMGET":
			    String fileID = st.nextToken();
			    System.out.println("ID requested: " + fileID);

			    String fileName = "";

			    File fileList = new File("data/server/List.txt");
			    Scanner sc = new Scanner(fileList);
			    String line = "";

			    while (sc.hasNext()) {
			        line = sc.nextLine();
			        StringTokenizer token = new StringTokenizer(line);
			        String id = token.nextToken();
			        String fName = token.nextToken();

			        if (id.equals(fileID)) {
			            fileName = fName;
			        }
			    }
			    sc.close();

			    System.out.println("Name of the file requested: " + fileName);
			    File fileToReturn = new File("data/server/" + fileName);

			    // Sending file to client
			    if (fileToReturn.exists()) {
			        pw.println(fileToReturn.length());
			        pw.flush();

			        FileInputStream fis = new FileInputStream(fileToReturn);
			        byte[] buffer = new byte[1024];
			        int n = 0;

			        while ((n = fis.read(buffer)) > 0) {
			            dos.write(buffer, 0, n);
			            dos.flush();
			        }

			        fis.close();
			        System.out.println("File sent to client.");
			    } else {
			        // Handle the case when the file does not exist
			        pw.println("NEE File not found");
			        pw.flush();
			    }

			    break;

			}
		}
	}catch(IOException ex) {
	ex.printStackTrace();	
		
	}
}
	
	
	
	
	

	  /**
     * Validates user credentials.
     * 
     * @param name The username.
     * @param password The password.
     * @return true if the credentials are valid, false otherwise.
     */
    private boolean isValidCredentials(String name, String password) {
        // You can read users and passwords from a file (e.g., users.txt) and check if they match
    	
    	File userFile= new File("data/server/users.txt");
    	try {
    		Scanner sc=new Scanner(userFile);
    		String line="";
    		
    		
    		while(sc.hasNext()) {
    			line=sc.nextLine();
    			StringTokenizer st= new StringTokenizer(line);
    			
    			String Username=st.nextToken();
    			String Userpassword=st.nextToken();
    		    if (Username.equals(name) && Userpassword.equals(password)) {
                    return true; // Match found, valid credentials
                }
    			
    		}
    		
    	}catch(FileNotFoundException ex) {
    		ex.printStackTrace();	
    		
    	}
    	
    	
        return false;
    }

}
