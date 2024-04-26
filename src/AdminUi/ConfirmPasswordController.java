package AdminUi;

import Components.User;
import LoginUi.LoginController;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConfirmPasswordController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button confirmPassButton;
	
	@FXML
	private PasswordField passField;
	@FXML
	private Label invalidInfo;
	public final static int DELETION_MODE = 1;
	public final static int UPDATE_MODE = 2;
	
	private int currentMode;
	
	public void setCurrentMode(int mode) {
		this.currentMode = mode;
	}
	
	
	public void confirmGivenPassword(ActionEvent event) {
		String hashedPass = LoginController.hashPassword(passField.getText(), LoginController.getLoggedUser().getUser_salt());
		
		if(hashedPass.equals(LoginController.getLoggedUser().getPass_word())) {
			((AdminController)Helper.currentAdminLoader.getController()).getAllUsersViewController().confirmedPassword = true;
			disposeWindow(event);
		}else {
			invalidInfo.setText("Passwords Do Not Match!");
			animatedInvalidInfolabel();
		}
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
