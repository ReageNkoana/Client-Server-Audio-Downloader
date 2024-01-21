package csc2b.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * @author Nkoana RRM
 * @version 7
 * ZEDEMClientPane is responsible for handling the gui and actions of the buttons
 */
public class ZEDEMClientPane extends GridPane {

	
	private Stage primaryStage;
	private String[] listAudio;
	
	
	//streams
	private Socket s;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private BufferedReader br;
	private PrintWriter pw; 
	
	
	//gui loging
	
	private Button btnConnect;
	private Label lblName;
	private TextField txtName;
	private Label lblPassword;
	private TextField txtPassword;
	private Button btnLogin;
	
	//gui download
	private Button btnList;
	private Label lblId;
	private TextField txtId;
	private Button btnDownload;
	private TextArea listArea;
	private TextArea responseArea;
	private Button btnLogout;
	

	
	
	
	public ZEDEMClientPane(Stage primaryStage) {
		// TODO Auto-generated constructor stub
		this.primaryStage=primaryStage;
		InitializeGui();
		
	}
	
	
    /*
     * initializes gui
     */
	private void InitializeGui() {
		
		SetUpLogingGui();
		 
		 
		 
		btnConnect.setOnAction((e)->{
			
			try {
				
				s= new Socket("localhost",2021);
				is= s.getInputStream();
				os=s.getOutputStream();
				dis= new DataInputStream(is);
				dos= new DataOutputStream(os);
				br= new BufferedReader(new InputStreamReader(is));
				pw= new PrintWriter(os); 
				
				
				
				
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		});
		
		
		//loging button code
		btnLogin.setOnAction((e)->{
			
			String name = txtName.getText();
			String passw= txtPassword.getText();
			
			
			sendCommand(pw, "BONJOUR "+ name +" "+ passw);
			String response= readResponse(br);
			
			if (response !=null) {
				
				if(response.startsWith("JA")) {
					System.out.println("Logged in"+ response);
					
					getChildren().clear();
					SetupDownloadGui();
					
				}else if (response.startsWith("NEE")) {
					System.out.println("Could not log in. Response: "+response);
					
				}else {
					System.out.println("Unexpected server response"+ response);
				}
				
				
			}else {
				
				System.out.println("Null response fromserver:" + response);
			}
			
			
		});
		
	}
	
	
	   /*
     * Gui for login
     */
	private void SetUpLogingGui() {
		setHgap(10);
		setVgap(10);
		
		btnConnect= new Button("Connect");
		lblName= new Label("Enter Name:");
		txtName= new TextField("User");
		lblPassword= new Label("Enter password:");
		txtPassword=new TextField("Pass235");
		btnLogin= new Button("Login");
		 
		 
		 add(btnConnect,0,0);
		 add(lblName,0,1);
		 add(txtName,0,2);
		 add(lblPassword,0,3);
		 add(txtPassword,0,4);
		 add(btnLogin,0,5);
		 
		
	}
	
	
	
	  /*
     * GUI for the download of the audio
     */
	private void SetupDownloadGui() {
		setHgap(10);
		setVgap(10);
		
		btnList= new Button("LIST");
		lblId= new Label("Enter ID:");
		txtId= new TextField();
		btnDownload= new Button("DOWNLOAD");
		listArea=new TextArea("List: ");
		responseArea=new TextArea("Response: ");
		btnLogout=new Button("LOGOUT");
		
		 add(btnList,0,0);
		 add(lblId,0,1);
		 add(txtId,0,2);
		 add(btnDownload,0,3);
		 add(listArea,0,4);
		 add(responseArea,0,5);
		 add(btnLogout,0,6);
		 
		 
		 
		 //get LIST
		 
			btnList.setOnAction((e)->{
				sendCommand(pw,"PLAYLIST");
				String response="";
				
				response=readResponse(br);
				System.out.println(response);
				
				listAudio=response.split("#");
				
				for(int i=0; i<listAudio.length;i++) {
					listArea.appendText(listAudio[i]+"\n"+"\n");
				}
				
			});
			
	        
			//download
			  btnDownload.setOnAction((e) -> {
		            try {
		                // Get the ID from the TextField and convert it to an integer
		                int retrieveID = Integer.parseInt(txtId.getText());

		                // Send the PDFRET command to the server with the ID
		                sendCommand(pw, "ZEDEMGET " + retrieveID);

		                // Server will respond with the file size and file content
		                String response = readResponse(br);
		                int fileSize = Integer.parseInt(response);

		                // Check if the response indicates an error (e.g., 500 <Message>)
		                if (response.startsWith("NEE")) {
		                    responseArea.appendText("Error: " + response.substring(4)); // Display the error message
		                } else {
		                    // Get the filename from the list of data
		                    String getFilename = null; 
		                    for (String s : listAudio) {
		                        StringTokenizer tokenizer = new StringTokenizer(s);
		                        if (tokenizer.hasMoreTokens()) {
		                            String id = tokenizer.nextToken();
		                            if (id.equals(Integer.toString(retrieveID)) && tokenizer.hasMoreTokens()) {
		                                getFilename = tokenizer.nextToken();
		                                break;
		                            }
		                        }
		                    }

		                    // Get the file downloaded from the server
		                    File fileDownloaded = new File("data/client/" + getFilename); // Save to the client folder
		                    FileOutputStream fos = new FileOutputStream(fileDownloaded);

		                    byte[] buffer = new byte[1024];
		                    int bytesRead;
		                    int totalBytes = 0;

		                    while (totalBytes != fileSize) {
		                        bytesRead = dis.read(buffer, 0, buffer.length);
		                        fos.write(buffer, 0, bytesRead);
		                        fos.flush();
		                        totalBytes += bytesRead;
		                    }

		                    fos.close();
		                    responseArea.appendText("File saved: " + getFilename+"\n"+  "Size: " + fileSize + " bytes\n");
		                }
		            } catch (NumberFormatException ex) {
		                responseArea.appendText("Invalid ID. Please enter a valid numeric ID."+"\n");
		            } catch (IOException ex) {
		                ex.printStackTrace();
		                responseArea.appendText("Error while downloading the file: " + ex.getMessage()+"\n");
		            }
		        });

		 
				 //get Logout
				 
				btnLogout.setOnAction((e)->{
					try {
					sendCommand(pw,"LOGOUT");
					s.close();
					}catch(IOException ex) {
						ex.printStackTrace();
					}finally {
						System.exit(0);
					}
					
					
				});
		 
		
	}
	
	
	
	  /*
     * read command lines from server
     * @param BufferedReader br
     * @return response from server
     */
	private String readResponse(BufferedReader br) {
		
		String response="";
		
		try {
			response= br.readLine();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
		return response;
		
	}

	
	

    /* Send a command to the server (utility method)
     * @aram PrintWriter pw
     * @param String msg
     */
	private void sendCommand(PrintWriter pw, String msg) {
		pw.println(msg);
		pw.flush();
		
		
	}
}
