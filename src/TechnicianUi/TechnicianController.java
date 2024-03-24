package TechnicianUi;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import LoginUi.LoginController;

public class TechnicianController {
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	
	//logging out of admin:
	public void LogOut(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(LoginController.fxmlLogin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loginScene = new Scene(root);
		stage.setScene(loginScene);
		
		stage.show();
	}
}
