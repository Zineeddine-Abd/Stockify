package AdminUi;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import Components.Asset;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReportPopupController implements Initializable{
	
	@FXML
	private TextArea messageAreaTextField;
	
	@FXML
	private ChoiceBox<String> statusChoiceBox;
	@FXML
	private Button reportButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label invalidInfo;
	
	private String[] reportStatuses = {"Broken","Under Maintenance","Ready To Use"};
	
	private Asset oldAsset;
	
	public void setOldAsset(Asset asset) {
		this.oldAsset = asset;
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		statusChoiceBox.getItems().addAll(reportStatuses);
	}
	
	public void validateReportInfo(ActionEvent event) {
		if(statusChoiceBox.getValue()  == null) {
			invalidInfo.setText("You must Select a new status before Reporting!");
			animatedInvalidInfolabel();
			return;
		}
		
		int id = 0;
		String category = oldAsset.getAsset_category();
		String type = oldAsset.getAsset_type();
		String model = oldAsset.getAsset_model();
		String status = statusChoiceBox.getValue();
		String room = oldAsset.getAsset_room();
		int warranty = oldAsset.getAsset_warranty();
		int serial_number = oldAsset.getAsset_serial_number();
		Date date = oldAsset.getAsset_purchase_date();
		Asset new_asset = new Asset(id,category,type,model,status,room,date,warranty,serial_number);
		
		((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().updateAsset(oldAsset, new_asset);
		disposeWindow(event);
	}
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),invalidInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	
	
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
}
