package AdminUi;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import Components.Asset;
import Components.Message;
import LoginUi.LoginController;
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
import javafx.scene.paint.Color;
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
	@FXML
	private Label warrantyStatusLabel;
	
	public static final String[] reportStatuses = {"Broken","Under Maintenance","Ready To Use"};
	public static final String[] reportSoftwareStatuses = {"Active" , "Inactive" ,"Under Maintenance", "Ready To Use" };
	
	private Asset oldAsset;
	
	public void setOldAsset(Asset asset) {
		this.oldAsset = asset;
	}
	
	public Asset getOldAsset() {
		return oldAsset;
	}
	
	public ChoiceBox<String> getStatusChoiceBox(){
		return statusChoiceBox;
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {	
	}
	
	public void validateReportInfo(ActionEvent event) {
		if(statusChoiceBox.getValue()  == null) {
			invalidInfo.setText("You must Select a new status before Reporting!");
			animatedInvalidInfolabel();
			return;
		}
		
		if(!messageAreaTextField.getText().isBlank()) {
			Date date = java.sql.Date.valueOf(LocalDate.now());
			Message msg = new Message(-1,messageAreaTextField.getText(),oldAsset.getAsset_id(),LoginController.getLoggedUser().getUser_id(),date);
			AdminController controller = (AdminController)Helper.currentAdminLoader.getController();
			controller.addMessage(msg);
		}
		
		String category = oldAsset.getAsset_category();
		String type = oldAsset.getAsset_type();
		String model = oldAsset.getAsset_model();
		String status = statusChoiceBox.getValue();
		String room = oldAsset.getAsset_room();
		int warranty = oldAsset.getAsset_warranty();
		Date date = oldAsset.getAsset_purchase_date();
		Asset new_asset = new Asset(oldAsset.getAsset_id(),category,type,model,status,room,date,warranty);
		
		((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().updateAsset(oldAsset, new_asset);
		disposeWindow(event);
		((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().refreshList();
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
	
	public void checkWarrantyValidation() {
		LocalDate assetDate = oldAsset.getAsset_purchase_date().toLocalDate();
		LocalDate today = LocalDate.now();
		
		Period period = Period.between(assetDate, today);
        int monthsDifference = period.getMonths();
		
		if(monthsDifference >= oldAsset.getAsset_warranty() || oldAsset.getAsset_warranty() == 0) {
			warrantyStatusLabel.setText("Expired!");
			warrantyStatusLabel.setTextFill(Color.RED);
		}else {
			warrantyStatusLabel.setText("Valid!");
			warrantyStatusLabel.setTextFill(Color.DARKGREEN);
		}
	}
}
