package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HelpController implements Initializable{

	@FXML
	private HBox hbox;
	
	//user manual mats:
	@FXML
	private Label userManualLabel;
	@FXML
	private Button viewUserManualButton;
	@FXML
	private ImageView userManualQrCode;
	@FXML
	private VBox userManualVbox;
	//maintenance manual mats:
	@FXML
	private Label maintenanceManualLabel;
	@FXML
	private Button viewMaintenanceManualButton;
	@FXML
	private ImageView maintenanceManualQrCode;
	@FXML
	private VBox maintenanceManualVbox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewUserManualButton.prefWidthProperty().bind(hbox.widthProperty().divide(2).add(-35));
		userManualLabel.prefWidthProperty().bind(hbox.widthProperty().divide(2).add(-35));
		
		viewMaintenanceManualButton.prefWidthProperty().bind(hbox.widthProperty().divide(2).add(-35));
		maintenanceManualLabel.prefWidthProperty().bind(hbox.widthProperty().divide(2).add(-35));
	}
	
}
