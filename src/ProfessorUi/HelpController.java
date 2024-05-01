package ProfessorUi;

import java.net.URL;
import javafx.application.HostServices;
import java.util.ResourceBundle;

import application.Main;
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
	private Label copyrightsLabel;
	@FXML
	private Label StockifyHelpLabel;
	@FXML
	private Label ScanMeLabel;
	@FXML
	private Button GoToSiteButton;
	@FXML
	private ImageView UserManualQrCode;
	@FXML
	private VBox ManualVbox;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ManualVbox.prefWidthProperty().bind(hbox.widthProperty());
		ManualVbox.prefHeightProperty().bind(hbox.heightProperty());
		StockifyHelpLabel.prefWidthProperty().bind(ManualVbox.widthProperty());
		copyrightsLabel.prefWidthProperty().bind(ManualVbox.widthProperty());
		copyrightsLabel.translateYProperty().bind(ManualVbox.heightProperty().add(-670));
		//ScanMeLabel.prefWidthProperty().bind(ManualVbox.widthProperty());
		GoToSiteButton.prefWidthProperty().bind(ManualVbox.widthProperty());
		
		HostServices hostServices = Main.services;
		
		GoToSiteButton.setOnAction(event->{
			hostServices.showDocument("https://sites.google.com/view/stockify-help/home");
		});
		
	}
	
}
