package AdminUi;

import java.net.URL;
import java.util.ResourceBundle;

import Components.Action;
import Components.Asset;
import application.DB_Actions;
import application.DB_Messages;
import application.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;



public class DashboardController implements Initializable{
	@FXML
	public Label numRooms;
	@FXML
	public Label numHardware;
	@FXML
	public Label numSoftware;
	@FXML
	public Label numAccessory;
	@FXML
	private HBox pans;
	@FXML
	private BorderPane recentActionsPane;
	@FXML
	private BorderPane warningsPane;
	
	
	//All Assets Section Mats:******************/
		@FXML
		private Button HardwareButton;
		@FXML
		private Button SoftwareButton;
		@FXML
		private Button AccesoriesButton;
		@FXML
		private Button RoomsButton;
		@FXML
		private ListView<Asset> reportedAssetsList;
		private ObservableList<Asset> reportedAssetsObs;
		@FXML
		private ListView<Action> recentActions;
		private ObservableList<Action> actionsObs;
		
		public ObservableList<Action> getrecentActionsObsList() {
			return this.actionsObs;
		}
		//*****************************************/
		
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			warningsPane.prefWidthProperty().bind(pans.widthProperty().divide(2).add(-35));
			recentActionsPane.prefWidthProperty().bind(pans.widthProperty().divide(2).add(-35));
			
			HardwareButton.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			numHardware.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			
			RoomsButton.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			numRooms.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			
			SoftwareButton.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			numSoftware.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			
			AccesoriesButton.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			numAccessory.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
		}
		
		public void setItems() {
			//Reported Assets List:
			reportedAssetsObs = DB_Messages.getReportedAssets();
			reportedAssetsList.setItems(reportedAssetsObs);
			reportedAssetsList.setCellFactory(new Callback<ListView<Asset>, ListCell<Asset>>() {
	            @Override
	            public ListCell<Asset> call(ListView<Asset> param) {
	                return new ListCell<Asset>() {
	                    @Override
	                    protected void updateItem(Asset item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if (empty || item == null) {
	                            setText(null);
	                        } else {
	                        	setText(item.toString());
	                        	AllAssetsController controller = ((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController();
	                            setOnMouseClicked(event->controller.showMessagesList(event, this.getListView().getSelectionModel().getSelectedItem()));
	                        }
	                    }
	                };
	            }
	        });
			
			
			actionsObs = FXCollections.observableArrayList();
			DB_Actions.refresh(actionsObs);
			recentActions.setItems(actionsObs);
		}
		
		public void refreshList() {
			reportedAssetsObs = DB_Messages.getReportedAssets();
			reportedAssetsList.setItems(reportedAssetsObs);
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
