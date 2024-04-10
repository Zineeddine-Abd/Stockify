package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

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
	
	
	//old asset
	private Room oldRoom;
	
	public void setOldRoom(Room room) {
		this.oldRoom = room;
	}
	//title label
	@FXML
	private Label titleLabel;
	
	public void setTitleLabelText(String text) {
		this.titleLabel.setText(text);
	}
	

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
	
		if(oldRoom == null) {
			newRoom(room);
		}else {
			updateRoom(room);
		}
	    
		disposeWindow(event);
	}
	
	private void newRoom(Room newRoom) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllRoomsViewController().addRoom(newRoom);
	}
	
	private void updateRoom(Room newRoom) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllRoomsViewController().updateRoom(oldRoom, newRoom);
	}
	
	void setInfos() {
		typeChoiceBox.setValue(oldRoom.getRoom_type());
		roomField.setText(oldRoom.getRoom_name());
	}
	
	
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),invalidInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}

}
