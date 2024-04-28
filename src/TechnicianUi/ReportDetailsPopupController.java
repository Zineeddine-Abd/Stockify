package TechnicianUi;


import Components.Message;
import Components.User;
import application.DB_Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReportDetailsPopupController {
	
	@FXML
	private TextArea messageAreaTextField;
	
	private Message message;
	public void setMessage(Message msg) {
		this.message = msg;
	}
	
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	public void setItems() {
		User messageAuthor = DB_Users.getUser(message.getMessage_author());
		messageAreaTextField.setText(message.getMessage() + "\n\nAuthor: " + messageAuthor.getFullName() + "\n\nEmail : " + messageAuthor.getEmail());
	}

}
