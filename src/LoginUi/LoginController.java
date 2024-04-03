package LoginUi;
import application.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;

public class LoginController{
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private static final String ADMIN = "Administrator";
	private static final String TECHNICIAN = "Technician";
	private static final String PROFESSOR = "Professor";
	
	public static final String[] permissions = {ADMIN,TECHNICIAN,PROFESSOR};
	
	
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
	private void assignUser(ActionEvent event) throws IOException {
		//directAdmin(event);
		//return;
		try (Connection con = DatabaseUtilities.getDataSource().getConnection()){
			//Changes based on the driver and type of sqlDatabase used:
			
			
			String password = "'" + (showPassBox.isSelected() ? showPasswordField.getText() : passwordField.getText()) +"'" ;
			String username = "'" + usernameField.getText() + "'" ;
			
	        String sql = "SELECT * FROM users WHERE username=" + username + " AND pass_word="+password + " ;";
	        
	        try(PreparedStatement statement = con.prepareStatement(sql);
	        	ResultSet resultSet = statement.executeQuery();){
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
	        }
	        
		} catch (SQLException e) {
			displayErrorMessage("Error",e.getMessage());
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
			try {  
				assignUser(event);
			} catch (Exception e) {
				e.printStackTrace();
			}	
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
		Helper.currentAdminLoader = new FXMLLoader(getClass().getResource(Helper.fxmlAdmin));
		root = Helper.currentAdminLoader.load();
		newStage(event, root);
	}
	public void directTechnician(ActionEvent event) throws IOException {
		Helper.currentTechnicianLoader = new FXMLLoader(getClass().getResource(Helper.fxmlTechnician));
		root = Helper.currentTechnicianLoader.load();
		newStage(event, root);
	}
	public void directProfessor(ActionEvent event) throws IOException {
		Helper.currentProfessorLoader = new FXMLLoader(getClass().getResource(Helper.fxmlProfessor));
		root = Helper.currentProfessorLoader.load();
		newStage(event, root);
	}
	
	
	private void newStage(ActionEvent event, Parent root) {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		
		scene = new Scene(root);
		
		stage = new Stage();
		stage.setScene(scene);
		stage.getIcons().add(Main.itAssetLogo);
		stage.setTitle("Stockify");
		stage.setOnCloseRequest(e ->{
			e.consume();
			stage.close();
			if(DatabaseUtilities.getDataSource() != null) {
				DatabaseUtilities.getDataSource().close();
			}
		});
		stage.centerOnScreen();
//		stage.setMaximized(true);
		stage.show();
	}
	
	 
	 Stage draggableStage;
	 double x, y;
	 public void getInitialMousePosition(MouseEvent event) {
		draggableStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		x =  event.getScreenX() - draggableStage.getX();
		y =  event.getScreenY() - draggableStage.getY();
	 }
	 
	 public void setNewRootPosition(MouseEvent event) {
		draggableStage.setX(event.getScreenX() - x);
		draggableStage.setY(event.getScreenY() - y);
	 }
	 
	 public void closeLoginScreen(MouseEvent event) {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		if(DatabaseUtilities.getDataSource() != null) {
			DatabaseUtilities.getDataSource().close();
		}
	 }
	 
	 private void displayErrorMessage(String title, String message) {
		 Alert alert = new Alert(AlertType.ERROR);
		 alert.setTitle(title);
		 alert.setHeaderText(null);
		 alert.setContentText(message);
	     alert.showAndWait();
	 }
	   
}
