package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import Components.Asset;
import application.DB_Assets;
import application.DB_Utilities;
import application.Helper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllAssetsController implements Initializable{

	//Table_View assets components:********************
	@FXML
    private TableView<Asset> assetsTable;
	
	public TableView<Asset> getAssetsTable(){
		return assetsTable;
	}

    @FXML
    private TableColumn<Asset, Integer> assetIdColumn;

    @FXML
    private TableColumn<Asset, String> assetCategoryColumn;
    
    @FXML
    private TableColumn<Asset, String> assetTypeColumn;

    @FXML
    private TableColumn<Asset, String> modelColumn;

    @FXML
    private TableColumn<Asset, String> statusColumn;

    @FXML
    private TableColumn<Asset, String> roomColumn;
    
    @FXML
    private TableColumn<Asset, Date> assetPurchaseDateColumn;

    @FXML
    private TableColumn<Asset, Integer> assetWarrantyColumn;

    @FXML
    private TableColumn<Asset, Integer> assetSerialNumberColumn;
	
	
	@FXML
	private Button newAssetButton;
	
    
    //creating new asset/user mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
  	@FXML
    private TextField searchTextField;
  	
  	public TextField getSearchTextField() {
  		return searchTextField;
  	}
    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    
    public ChoiceBox<String> getSearchCriteriaComboBox(){
    	return searchCriteriaComboBox;
    }
    
    private final String[] criteria = {"Category", "Type", "Model", "Status", "Location"};
    
    //observable lists***************
    ObservableList<Asset> allAssetsObs;
    FilteredList<Asset> filteredAssets;
    SortedList<Asset> sortedAssets;
    //********************************
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allAssetsObs = FXCollections.observableArrayList();
		
		DB_Assets.refresh(allAssetsObs);
	
		
		assetsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assetIdColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_id"));
        assetCategoryColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_category"));
        assetTypeColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_type"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_model"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_status"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_room"));
        assetPurchaseDateColumn.setCellValueFactory(new PropertyValueFactory<Asset, Date>("asset_purchase_date"));
        assetWarrantyColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_warranty"));
        assetSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_serial_number"));
        
        //assetIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
//	    assetPurchaseDateColumn.setCellFactory(DatePickerTableCell.forTableColumn());
//	    assetWarrantyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//	    assetSerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
        filteredAssets = new FilteredList<Asset>(allAssetsObs);
        filterTableView();
        
        sortedAssets = new SortedList<Asset>(filteredAssets);
        sortedAssets.comparatorProperty().bind(assetsTable.comparatorProperty());
        
        assetsTable.setItems(sortedAssets);
        
        // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll(criteria);
        searchCriteriaComboBox.setValue(criteria[0]);
	}

	
	public void createNewAsset(ActionEvent event){
		Parent root;
			
		try {
			AdminController.currentNewAssetLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewAsset));
			
			root = AdminController.currentNewAssetLoader.load();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Create New Asset:");
			
			//AssetController.setStage(fillFormula);
			createNewScene = new Scene(root);
			createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
	
			fillFormula.setScene(createNewScene);
			fillFormula.getIcons().add(Main.itAssetLogo);
			
			//make it as a dialog box
			Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			fillFormula.initModality(Modality.WINDOW_MODAL);
			fillFormula.initOwner(parentStage);
			
			fillFormula.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addAsset(Asset newAsset) {
       DB_Assets.addAsset(allAssetsObs, newAsset);
    }
	
	 
    public void deleteSelectedAssets() {
    	
    	ObservableList<Asset> selectedAssets = assetsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 	
    		DB_Assets.removeAsset(allAssetsObs, selectedAssets);
    	}
    }
    
    
    //Filtering methods******************************************************
	private void filterTableView() {
		searchTextField.textProperty().addListener((obs, oldTxt, newTxt)->{
			setFilterPredicate(newTxt);
		});
		searchCriteriaComboBox.valueProperty().addListener((obs, oldVal, newVal)->{
			setFilterPredicate(searchTextField.getText());
		});
	}
	
	private void setFilterPredicate(String txt) {
		
		filteredAssets.setPredicate((asset)-> {
			if(txt == null || txt.isBlank()) {
				return true;
			}else {
				switch (searchCriteriaComboBox.getValue()) {
				case "Category":
					if(asset.getAsset_category().toLowerCase().contains(txt.toLowerCase())) {	
						return true;
					}
		            break;
		        case "Type":
		        	if(asset.getAsset_type().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Model":
		        	if(asset.getAsset_model().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Status":
		        	if(asset.getAsset_status().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Location":
		        	if(asset.getAsset_room().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		            
		        default:
		        	return false;
				}
				return false;
			}
		});
	}
}

