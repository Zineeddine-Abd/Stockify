package LoginUi;
import application.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;

public class LoginController implements Initializable{
	public static final String fxmlAdmin = "/AdminUi/adminScene.fxml";
	public static final String fxmlTechnician = "/TechnicianUi/technicianScene.fxml";
	public static final String fxmlProfessor = "/ProfessorUi/professorScene.fxml";
	public static final String fxmlLogin = "/LoginUi/loginScene.fxml";
	
	private Stage stage;
	private Scene adminScene;
	private Scene technicianScene;
	private Scene professorScene;
	private Parent root;
	
	
	private final String ADMIN = "Administrator";
	private final String TECHNICIAN = "Technician";
	private final String PROFESSOR = "Professor";
	private String[] roles = {ADMIN,TECHNICIAN,PROFESSOR};
	
	@FXML
	private Button loginButton;
	private String pass ;
	@FXML
	private Label incorrectInfo;
	@FXML
	private ChoiceBox<String> selectRoleChoiceBox;
	//password text fields : 
	@FXML
	private PasswordField passwordfield;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField showpasswordfield;
	
	//Check boxes : 
	@FXML
	private CheckBox showPassBox;
	@FXML
	private CheckBox rememberMe;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		
		//***********************************************************
		selectRoleChoiceBox.getItems().addAll(roles);// drop down box
		passwordfield.setVisible(true); // password field.
		showpasswordfield.setVisible(false);//textPass field.
		incorrectInfo.setVisible(true);//incorrect info label .
	}
	
	
	//Database linking for each user.
	private void assignUser(ActionEvent event) {
		String role = selectRoleChoiceBox.getValue(); // get the selected dropdown box role.
		try {
			switch(role) {
			case ADMIN:
				directAdmin(event);
				break;
			case TECHNICIAN:
				directTechnician(event);
				break;
			case PROFESSOR:
				directProfessor(event);
				break;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showPassword(ActionEvent event) {
		if(showPassBox.isSelected()) {
			pass = passwordfield.getText();
			showpasswordfield.setText(pass);
			
			showpasswordfield.setVisible(true);
			passwordfield.setVisible(false);
		}else {
			pass = showpasswordfield.getText();
			passwordfield.setText(pass);
			
			showpasswordfield.setVisible(false);
			passwordfield.setVisible(true);
		}

	}
	
	public void rememberMe(ActionEvent event) {
		//to be designed later :)
	}
	
	public void loginClicked(ActionEvent event) {
		if(usernameField.getText().isBlank() || 
		isPassBlank() || selectRoleChoiceBox.getValue() == null)
		{	
			if(selectRoleChoiceBox.getValue() == null) {				
				incorrectInfo.setText("Choose your account type first!");
			}else {
				incorrectInfo.setText("Invalid username or password!");
			}
			animatedIncorrectInfolabel();
		}else {
			assignUser(event);
		}
	}
	private void animatedIncorrectInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),incorrectInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	
	private boolean isPassBlank() {
		if(passwordfield.getText().isBlank() && !showpasswordfield.getText().isBlank()) {
			return false;
		}
		if(!passwordfield.getText().isBlank() && showpasswordfield.getText().isBlank()) {
			return false;
		}
		if(!passwordfield.getText().isBlank() && !showpasswordfield.getText().isBlank()) {
			return false;
		}
		return true;
	}
	
	public void directAdmin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlAdmin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		adminScene = new Scene(root);
		adminScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
		
		stage.setScene(adminScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		
		centerStage(stage);

        stage.show();
	}
	public void directTechnician(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlTechnician));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		technicianScene = new Scene(root);
		technicianScene.getStylesheets().add(this.getClass().getResource("/TechnicianUi/technician.css").toExternalForm());
		
		stage.setScene(technicianScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		
		stage.show();
	}
	public void directProfessor(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlProfessor));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		professorScene = new Scene(root);
		professorScene.getStylesheets().add(this.getClass().getResource("/ProfessorUi/professor.css").toExternalForm());
		
		stage.setScene(professorScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		
		stage.show();
	}
	
	
	 private void centerStage(Stage stage) {
	        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
	        double stageWidth = stage.getWidth();
	        double stageHeight = stage.getHeight();

	        // Calculate the center position
	        double centerX = (screenWidth - stageWidth) / 2;
	        double centerY = (screenHeight - stageHeight) / 2;

	        // Set the stage position
	        stage.setX(centerX);
	        stage.setY(centerY);
	    }
}
