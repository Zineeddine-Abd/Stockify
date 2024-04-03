package AdminUi;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Components.Asset;
import Components.Room;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NewRoomController implements Initializable{
	
	@FXML
	private ChoiceBox<String> typeChoiceBox;
	@FXML
	private TextField roomField;
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label invalidInfo;
	
	private String[] room_type = {"TP","TD","Emphy"};
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		
		typeChoiceBox.getItems().addAll(room_type);
		
	}
	
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	public void validateInformation(ActionEvent event) {
		if(typeChoiceBox.getValue() == null) {
			invalidInfo.setText("Select the type of the room first!");
			animatedInvalidInfolabel();
			return;
		}
		
		if(!roomField.getText().matches("^[a-zA-Z0-9]+$")) {
			invalidInfo.setText("Invalid room!");
			animatedInvalidInfolabel();
			return;
		}
		
		int room_id = 0;
		String room_type = typeChoiceBox.getValue();
		String room_name = roomField.getText().toUpperCase();
		Room room = new Room(room_id,room_type,room_name);
	
		((AdminController)Helper.currentAdminLoader.getController()).getRoomsController().addRoom(room);
	    
		disposeWindow(event);
	}
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),invalidInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}

}
