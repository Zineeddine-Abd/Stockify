package TechnicianUi;

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
import javafx.scene.paint.Color;
import javafx.util.Callback;



public class DashboardController implements Initializable{
	@FXML
	public Label numRooms;
	@FXML
	public Label numHardware;
	@FXML
	public Label numSoftware;
	@FXML
	public Label numAssets;
	@FXML
	private HBox pans;
	@FXML
	private BorderPane recentActionsPane;
	@FXML
	private BorderPane warningsPane;
	
	
	//All Assets Section Mats:******************/
		@FXML
		private Button assetsButton;
		@FXML
		private Button HardwareButton;
		@FXML
		private Button SoftwareButton;
		@FXML
		private Button RoomsButton;
		@FXML
		private ListView<Asset> reportedAssetsList;
		private ObservableList<Asset> reportedAssetsObs;
		
		public ObservableList<Asset> getReportedAssetsList(){
			return reportedAssetsObs;
		}
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
			
			assetsButton.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
			numAssets.prefWidthProperty().bind(pans.widthProperty().divide(4).add(-35));
		}
		
		public void setItems() {
			//Reported Assets List:
			reportedAssetsObs = DB_Messages.getReportedAssets(((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().getAllAssetsObs());
			reportedAssetsList.setItems(reportedAssetsObs);
			actionsObs = FXCollections.observableArrayList();
			DB_Actions.refresh(actionsObs);
			recentActions.setItems(actionsObs);
			
			setCellFactories();
			
		}
		
		public void setCellFactories() {
			
			reportedAssetsList.setCellFactory(new Callback<ListView<Asset>, ListCell<Asset>>() {
	            @Override
	            public ListCell<Asset> call(ListView<Asset> param) {
	                return new ListCell<Asset>() {
	                    @Override
	                    protected void updateItem(Asset item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if (empty || item == null) {
	                            setText(null);
	                        } else if (isSelected()) {
	                            setTextFill(Color.WHITE);
	                            setStyle("-fx-background-color: #0096c9;"); 
	                        }else{
	                        	if (item.getAsset_status().equals("Broken") || item.getAsset_status().equals("Inactive")) {
	                                setStyle("-fx-background-color: #FB9494;");
	                            } else if (item.getAsset_status().equals("Under Maintenance")) {
	                                setStyle("-fx-background-color: #FFB266;");
	                            } else if (item.getAsset_status().equals("Ready To Use") || item.getAsset_status().equals("Active")) {
	                                setStyle("-fx-background-color: #B2FF66;");
	                            } else {
	                                setStyle("");
	                            }
	                        	
	                        	setTextFill(Color.BLACK);
	                        	setText(item.toString());
	                        	AssetsTableController controller = ((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController();
	                            setOnMouseClicked(event->controller.showMessagesList(event, this.getListView().getSelectionModel().getSelectedItem()));
	                            
	                        }
	                    }
	                };
	            }
	        });
			
			recentActions.setCellFactory(new Callback<ListView<Action>, ListCell<Action>>() {
	            @Override
	            public ListCell<Action> call(ListView<Action> param) {
	                return new ListCell<Action>() {
	                    @Override
	                    protected void updateItem(Action item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if (empty || item == null) {
	                            setText(null);
	                            setStyle("-fx-background-color: #FFFFFF;");
	                        } else if (isSelected()) {
	                            setTextFill(Color.WHITE);
	                            setStyle("-fx-background-color: #0096c9;"); 
	                        }else{
	                        	if (item.getAction_type().equals("Update")) {
	                                setStyle("-fx-background-color: #f5ed7d;");
	                            }else if(item.getAction_type().equals("Insertion")) {
	                            	setStyle("-fx-background-color: #B2FF66");
	                            }else if(item.getAction_type().equals("Deletion")) {
	                            	setStyle("-fx-background-color: #FB9494");
	                            } else {
	                                setStyle("");
	                            }
	                        	
	                        	setTextFill(Color.BLACK);
	                        	setText(item.toString());
	                            
	                        }
	                    }
	                };
	            }
	        });
		}
		
		public void refreshList() {
			setCellFactories();
			
			reportedAssetsObs = DB_Messages.getReportedAssets(((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().getAllAssetsObs());
			reportedAssetsList.setItems(reportedAssetsObs);
			
			getrecentActionsObsList().clear();
			DB_Actions.refresh(getrecentActionsObsList());
		}
		
		public void triggerHardwarePane() {
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().filterTableView();
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().setHardwareColumns(null);
			((TechnicianController)Helper.currentTechnicianLoader.getController()).triggerAssetPane();
		}
		
		public void triggerSoftwarePane() {
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().filterTableView();
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().setSoftwareColumns(null);
			((TechnicianController)Helper.currentTechnicianLoader.getController()).triggerAssetPane();
		}
		
		public void triggerAssetsPane() {
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().filterTableView();
			((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().setAssetColumns(null);
			((TechnicianController)Helper.currentTechnicianLoader.getController()).triggerAssetPane();
		}
		
		public void triggerRoomsPane() {
			((TechnicianController)Helper.currentTechnicianLoader.getController()).triggerRoomsPane();
		}


	
}
