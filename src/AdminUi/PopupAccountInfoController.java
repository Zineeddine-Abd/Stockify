package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import Components.User;
import LoginUi.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupAccountInfoController implements Initializable{
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField usernameField;
	@FXML
	private Button cancelButton;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		User loggedUser = LoginController.getLoggedUser();
		lastNameField.setText(loggedUser.getLast_name());
		firstNameField.setText(loggedUser.getFirst_name());
		emailField.setText(loggedUser.getEmail());
		usernameField.setText(loggedUser.getUsername());
	}
	
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	
}
