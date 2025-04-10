package application;

import java.util.Optional;

import LoginUi.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Helper {
	
	public static final String INSERTION_MODE = "Insertion";
	public static final String UPDATE_MODE = "Update";
	public static final String DELETION_MODE = "Deletion";
	
	public static final String ASSET = "Asset";
	public static final String USER = "User";
	public static final String ROOM = "Room";
	
	public static final String HARDWARE = "Hardware";
	public static final String SOFTWARE = "Software";
	
	public static final String fxmlAdmin = "/AdminUi/adminScene.fxml";
	public static final String fxmlTechnician = "/TechnicianUi/technicianScene.fxml";
	public static final String fxmlProfessor = "/ProfessorUi/professorScene.fxml";
	public static final String fxmlLogin = "/LoginUi/loginScene.fxml";
	
	public static FXMLLoader currentLoginLoader;
	public static FXMLLoader currentAdminLoader;
	public static FXMLLoader currentTechnicianLoader;
	public static FXMLLoader currentProfessorLoader;
	
	public static final int CLICK_THRESHOLD = 2;
	public static boolean exception_thrown = false;

	public static boolean displayConfirmMessge(String message,String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(message);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        	return true;
        } else {
        	return false;
        }
    }
	
	public static void displayErrorMessage(String title, String message) {
		try {			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		}catch(IllegalStateException e) {
			return;
		}
    }
	
	public static void displayInfoMessage(String title , String message) {
		try {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(title);
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();
		}catch(IllegalStateException e) {
			return;
		}
	}
	
	public static void closeResources() {
		if(LoginController.getLoggedUser() != null) {
			DB_Sessions.terminateCurrentSession(LoginController.getLoggedUser().getUser_id());
		}
		if(DB_Utilities.getDataSource() != null) {
			DB_Utilities.getDataSource().close();
		}
	}
}
