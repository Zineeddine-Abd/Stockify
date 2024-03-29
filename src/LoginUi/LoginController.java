package LoginUi;
import application.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.*;

public class LoginController{
	public static final String fxmlAdmin = "/AdminUi/adminScene.fxml";
	public static final String fxmlTechnician = "/TechnicianUi/technicianScene.fxml";
	public static final String fxmlProfessor = "/ProfessorUi/professorScene.fxml";
	public static final String fxmlLogin = "/LoginUi/loginScene.fxml";
	
	private Stage stage;
	private Scene adminScene;
	private Scene technicianScene;
	private Scene professorScene;
	private Parent root;
	
	private static final String ADMIN = "Administrator";
	private static final String TECHNICIAN = "Technician";
	private static final String PROFESSOR = "Professor";
	
	public static final String[] permissions = {ADMIN,TECHNICIAN,PROFESSOR};
	
	private Connection con;
	public static final String url = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:5432/postgres?user=postgres.hjtaojbdkclkvvgfvzvh&password=ThisIsMyDatabasePassword";
	
	@FXML
	private Button loginButton;
	@FXML
	private Label incorrectInfo;
	//password text fields : 
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField showPasswordField;
	@FXML
	private CheckBox showPassBox;
	@FXML
	private CheckBox rememberMe;

	
	
	//Database linking for each user.
	private void assignUser(ActionEvent event) {
		try {
//			directAdmin(event);
			
			//Changes based on the driver and type of sqlDatabase used:
			
			Class.forName("org.postgresql.Driver");
	        Connection con = DriverManager.getConnection(url);
			
			String password = "'" + (showPassBox.isSelected() ? showPasswordField.getText() : passwordField.getText()) +"'" ;
			String username = "'" + usernameField.getText() + "'" ;
			
	        String sql = "SELECT * FROM users WHERE username=" + username + " AND pass_word="+password + " ;";
	        PreparedStatement statement = con.prepareStatement(sql);
	        ResultSet resultSet = statement.executeQuery();
	        
	        if(resultSet.next()) {
	        	switch(resultSet.getString("user_role")) {
	        		case ADMIN:
	        			directAdmin(event);
	        			break;
	        		case TECHNICIAN:
	        			directTechnician(event);
	        			break;
	        		case PROFESSOR:
	        			directProfessor(event);
	        			break;
	        		default:
	        			System.out.println("error!"); //idk what to put here - lokman.
	        	}
	        	return;
	        }else {
	        	incorrectInfo.setText("Invalid username or password!");
	        	animatedIncorrectInfolabel();
	        }
	        
		} catch (IOException | SQLException | ClassNotFoundException e) {
			displayErrorMessage("Error",e.getMessage());
		}finally {
			if (con != null) {
                try {
					con.close();
				} catch (SQLException e) {
					displayErrorMessage("Error",e.getMessage());
				}
            }
		}
	}
	
	public void showPassword(ActionEvent event) {
		String pass;
		if(showPassBox.isSelected()) {
			pass = passwordField.getText();
			showPasswordField.setText(pass);
			
			showPasswordField.setVisible(true);
			passwordField.setVisible(false);
		}else {
			pass = showPasswordField.getText();
			passwordField.setText(pass);
			
			showPasswordField.setVisible(false);
			passwordField.setVisible(true);
		}
	}
	
	public void rememberMe(ActionEvent event) {
		//to be designed later :)
	}
	
	public void loginClicked(ActionEvent event) {
		if(usernameField.getText().isBlank() || 
		isPassBlank())
		{
			incorrectInfo.setText("Invalid username or password!");
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
		return (passwordField.getText().isBlank() && showPasswordField.getText().isBlank());
	}
	
	public void directAdmin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlAdmin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		
		adminScene = new Scene(root);
		adminScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
		
		stage = new Stage();
		stage.setScene(adminScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		stage.initStyle(StageStyle.DECORATED);
		
		stage.show();
		centerStage(stage);
//		stage.setMaximized(true);

       
	}
	public void directTechnician(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlTechnician));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		
		technicianScene = new Scene(root);
		technicianScene.getStylesheets().add(this.getClass().getResource("/TechnicianUi/technician.css").toExternalForm());
		
		stage = new Stage();
		stage.setScene(technicianScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		stage.initStyle(StageStyle.DECORATED);
		
//		stage.setMaximized(true);
		stage.show();
		centerStage(stage);
	}
	public void directProfessor(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource(fxmlProfessor));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		
		professorScene = new Scene(root);
		professorScene.getStylesheets().add(this.getClass().getResource("/ProfessorUi/professor.css").toExternalForm());
		
		stage = new Stage();
		stage.setScene(professorScene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		stage.initStyle(StageStyle.DECORATED);
		
//		stage.setMaximized(true);
		stage.show();
		centerStage(stage);
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
	 
	 
	 double x, y;
	 public void getInitialMousePosition(MouseEvent event) {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		x =  event.getScreenX() - stage.getX();
		y =  event.getScreenY() - stage.getY();
	 }
	 
	 public void setNewRootPosition(MouseEvent event) {
		stage.setX(event.getScreenX() - x);
		stage.setY(event.getScreenY() - y);
	 }
	 
	 public void closeLoginScreen(MouseEvent event) {
		 stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		 stage.close();
	 }
	 
	 private void displayErrorMessage(String title, String message) {
		 Alert alert = new Alert(AlertType.ERROR);
		 alert.setTitle(title);
		 alert.setHeaderText(null);
		 alert.setContentText(message);
	     alert.showAndWait();
	 }
	   
}
