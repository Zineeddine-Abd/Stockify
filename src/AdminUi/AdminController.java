package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import LoginUi.LoginController;

public class AdminController implements Initializable{
	
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	
	//logging out of admin:
	public void LogOut(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(LoginController.fxmlLogin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loginScene = new Scene(root);
		stage.setScene(loginScene);
		stage.setTitle("Stockify - Login");
		
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
