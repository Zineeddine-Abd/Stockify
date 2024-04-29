package AdminUi;


import java.net.URL;
import java.util.ResourceBundle;

import Components.User;
import LoginUi.LoginController;
import application.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	@FXML
	private TextField role;
	@FXML
	private CheckBox enableRememMe;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			User loggedUser = LoginController.getLoggedUser();
			enableRememMe.setSelected(LoginController.fileExists(LoginController.savedCredentialsFilePath));
			lastNameField.setText(loggedUser.getLast_name());
			firstNameField.setText(loggedUser.getFirst_name());
			emailField.setText(loggedUser.getEmail());
			usernameField.setText(loggedUser.getUsername());
			role.setText(loggedUser.getUser_role());
		}catch(NullPointerException e) {
			Helper.displayErrorMessage("Error", "Logged user Info not found!");
		}
	}
	
	public void disposeWindow(ActionEvent event) {
		if(!enableRememMe.isSelected()) {
			if(LoginController.deleteCredsFile()) {
				Helper.displayInfoMessage("Success", "Changes Applied Successfully");
			}
		}else {
			if(!LoginController.fileExists(LoginController.savedCredentialsFilePath)) {
				LoginController.createFileForUser(LoginController.getLoggedUser().getUsername(), LoginController.getLoggedUser().getPass_word());
			}
		}
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	
}
