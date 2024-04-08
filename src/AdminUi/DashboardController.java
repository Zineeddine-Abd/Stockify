package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import application.Helper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;



public class DashboardController implements Initializable{
	@FXML
	public Label numRooms;
	@FXML
	public Label numHardware;
	@FXML
	public Label numSoftware;
	@FXML
	public Label numAccessory;
	
	
	//All Assets Section Mats:******************/
		@FXML
		private Button HardwareButton;
		@FXML
		private Button SoftwareButton;
		@FXML
		private Button AccesoriesButton;
		@FXML
		private Button RoomsButton;
		//*****************************************/
		
		
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
		}
		
		public void triggerHardwarePane() {
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchCriteriaComboBox.setValue("Category");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("Hardware");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().filterTableView();
			((AdminController)Helper.currentAdminLoader.getController()).triggerAssetPane();
		}
		
		public void triggerSoftwarePane() {
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchCriteriaComboBox.setValue("Category");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("Software");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().filterTableView();
			((AdminController)Helper.currentAdminLoader.getController()).triggerAssetPane();
		}
		
		public void triggerAccessoryPane() {
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchCriteriaComboBox.setValue("Category");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("Accessory");
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().filterTableView();
			((AdminController)Helper.currentAdminLoader.getController()).triggerAssetPane();
		}
		
		public void triggerRoomsPane() {
			((AdminController)Helper.currentAdminLoader.getController()).triggerRoomsPane();
		}

		
		
	
}
