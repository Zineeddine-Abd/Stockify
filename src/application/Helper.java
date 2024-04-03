package application;

import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Helper {
	
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
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
