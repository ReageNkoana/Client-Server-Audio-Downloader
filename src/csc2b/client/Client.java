package csc2b.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Nkoana RRM
 * @version 7
 * This is the clients main , where i call the ZEDEMClientPane
 */
public class Client extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		
		ZEDEMClientPane root= new ZEDEMClientPane(stage);
		Scene scene= new Scene(root,500,500);
		stage.setScene(scene);
		stage.setTitle("ZEDEM audio");
		stage.show();
		
	}

}
