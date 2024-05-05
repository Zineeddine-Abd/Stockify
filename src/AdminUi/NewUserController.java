package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import Components.User;
import LoginUi.LoginController;
import LoginUi.ResetPasswordController;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewUserController implements Initializable{
	@FXML
	private VBox rootVboxPasswords;
	@FXML
	private ChoiceBox<String> permissions;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField firstNameField;
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
	
	@FXML
	private Label oldPassword;
	@FXML
	private Label newPassword;
	
	//old user
	private User oldUser;
	
	public void setOldUser(User user) {
		this.oldUser = user;
	}
	//title label
	@FXML
	private Label titleLabel;
	
	public void setTitleLabelText(String text) {
		this.titleLabel.setText(text);
	}
	
	//*********************all methods:***********************

	public void initialize(URL arg0, ResourceBundle arg1) {
		
		rootVboxPasswords.heightProperty().addListener((obs, oldVal, newVal) -> {
			Scene mainScene = rootVboxPasswords.getScene();
			Stage mainStage = (Stage) mainScene.getWindow();
			mainStage.setHeight(mainStage.getHeight() + newVal.doubleValue() - oldVal.doubleValue());
		});
		
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
		if(!firstNameField.getText().matches("^[a-zA-Z]+$")) {
			invalidInfo.setText("Invalid First Name! No Special Characters allowed!");
			animatedInvalidInfolabel();
			return;
		}
		if(!lastNameField.getText().matches("^[a-zA-Z]+$")) {
			invalidInfo.setText("Invalid First Name! No Special Characters allowed!");
			animatedInvalidInfolabel();
			return;
		}
		if(!emailField.getText().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
			invalidInfo.setText("Invalid email Format!");
			animatedInvalidInfolabel();
			return;
		}
		
		//check only when creating a new user.
		if(oldUser == null) {	
			if(!confirmPasswordField.getText().equals(passwordField.getText())) {
				invalidInfo.setText("Passwords Do Not match!");
				animatedInvalidInfolabel();
				return;
			}
		}else {
			//compare the old entered password with the actual password:
			String oldUserHashedPass = oldUser.getPass_word();
			String oldEnteredHashedPass = LoginController.hashPassword(passwordField.getText(), oldUser.getUser_salt());
			
			if(!oldUserHashedPass.equals(oldEnteredHashedPass)) {
				invalidInfo.setText("Invalid Old Password!");
				animatedInvalidInfolabel();
				return;
			}
		}
		
		String username = usernameField.getText();
		String pass_word = confirmPasswordField.getText();
		String email = emailField.getText();
		String first_name = firstNameField.getText();
		String last_name = lastNameField.getText();
		String user_role = permissions.getValue();
		
		String newHashedPassword = LoginController.hashPassword(pass_word);
		String newHashedPasswordSalt = LoginController.getHashedPasswordSalt();
		
		User newuser = new User(0,username,newHashedPassword,email,first_name,last_name,user_role,newHashedPasswordSalt);
		
		if(oldUser == null) {
			newUser(newuser);
		}else {
			updateUser(newuser);
		}
		
		disposeWindow(event);
	}
	
	private void newUser(User newUser) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllUsersViewController().addUser(newUser);
	}
	
	private void updateUser(User newUser) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllUsersViewController().updateUser(oldUser, newUser);
	}
	
	//only used for updating a user.
	void setInfos() {
		oldPassword.setText("Old Password:");
		newPassword.setText("New Password:");
		
		usernameField.setText(oldUser.getUsername());
		emailField.setText(oldUser.getEmail());
		firstNameField.setText(oldUser.getFirst_name());
		lastNameField.setText(oldUser.getLast_name());
		permissions.setValue(oldUser.getUser_role());
		
		rootVboxPasswords.getChildren().removeAll(oldPassword,newPassword,passwordField,confirmPasswordField);
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
