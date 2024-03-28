package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

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

public class UserController implements Initializable{
	
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
	private TextField field1,field2,field3,field4,field5;
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
		//someone implement the logic im lazy the text fields above are corresponding to each in the scene one by order.except permissions which is a choice box.
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText("Successfully created new User but did not add it to database. check userController to implement it yourself - lokmam");
		alert.showAndWait();
		
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
