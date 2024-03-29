package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import Components.User;
import LoginUi.LoginController;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewUserController implements Initializable{
	
//	private static Stage stage;
	
	private AdminController adminController;

//	public static void setStage(Stage res) {
//		stage = res;
//	}
	
	public AdminController getAdminController() {
		return adminController;
	}

	public void setAdminController(AdminController adminController) {
		this.adminController = adminController;
	}
	
	@FXML
	private ChoiceBox<String> permissions;
	@FXML
	private TextField fullNameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField confirmPasswordField;
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label invalidInfo;	
	
	//*********************all methods:***********************

	public void initialize(URL arg0, ResourceBundle arg1) {
		permissions.getItems().addAll(LoginController.permissions);
	}
	
	public void validateInformation(ActionEvent event) {
		//validate info of user here .
		if(permissions.getValue() == null) {
			invalidInfo.setText("You must select Permissions for this user!");
			animatedInvalidInfolabel();
			return;
		}
		if(!usernameField.getText().matches("^[0-9A-Za-z]+$")) {
			invalidInfo.setText("Invalid username! No Special Characters allowed!");
			animatedInvalidInfolabel();
			return;
		}
//		if(!fullNameField.getText().matches("")) {
//			invalidInfo.setText("Invalid name! no digits/special characters allowed!");
//			animatedInvalidInfolabel();
//			return;
//		}
		if(!emailField.getText().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
			invalidInfo.setText("Invalid email Format!");
			animatedInvalidInfolabel();
			return;
		}
//		if(confirmPasswordField.getText() != passwordField.getText()) {
//			invalidInfo.setText("Passwords Do Not match!");
//			animatedInvalidInfolabel();
//			return;
//		}
		
		int id=0;
		String username = usernameField.getText();
		String pass_word = passwordField.getText();
		String email = emailField.getText();
		String full_name = fullNameField.getText();
		String user_role = permissions.getValue();
		
		User newuser = new User(id,username,pass_word,email,full_name,user_role);
		adminController.addUser(newuser);
		
		disposeWindow(event);
	}
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),invalidInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

}
