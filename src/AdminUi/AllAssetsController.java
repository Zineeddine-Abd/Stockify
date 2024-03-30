package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import Components.Asset;
import application.DatabaseUtilities;
import application.Helper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableColumn<Asset, String> locationColumn;
    
    @FXML
    private TableColumn<Asset, Date> assetPurchaseDateColumn;

    @FXML
    private TableColumn<Asset, Integer> assetWarrantyColumn;

    @FXML
    private TableColumn<Asset, Integer> assetSerialNumberColumn;
	
	
	@FXML
	private Button newAssetButton;
	
	//id
    public static int last_id = 0;
    
    //creating new asset/user mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
  	@FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    
    ObservableList<Asset> allAssets;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		assetsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assetIdColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_id"));
        assetCategoryColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_category"));
        assetTypeColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_type"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_model"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_status"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_location"));
        assetPurchaseDateColumn.setCellValueFactory(new PropertyValueFactory<Asset, Date>("asset_purchase_date"));
        assetWarrantyColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_warranty"));
        assetSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_serial_number"));
        
        //assetIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
//	    assetPurchaseDateColumn.setCellFactory(DatePickerTableCell.forTableColumn());
//	    assetWarrantyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//	    assetSerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
        
     // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll("Asset Category", "Asset Type", "Model", "Status", "Location");
        searchCriteriaComboBox.setValue("Asset Category");
        allAssets = assetsTable.getItems();
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
        DatabaseUtilities.insertItemIntoDatabase(newAsset);
        newAsset.setAsset_id(last_id);
        assetsTable.getItems().add(newAsset);
        //allAssets.add(newAsset);
    }
	
	 // Method to delete selected assets from assetsTable
    public void deleteSelectedAssets() {
    	
    	ObservableList<Asset> selectedAssets = assetsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 
    		for(Asset item : selectedAssets) {
    			DatabaseUtilities.deleteItemFromDatabase(item);
    		}
    		
    		assetsTable.getItems().removeAll(selectedAssets);
    	}
    	
    	allAssets.removeAll(selectedAssets);
    }
    
    
    @FXML
	private void handleSearchAction(ActionEvent event) {
	    String selectedCriteria = searchCriteriaComboBox.getValue();
	    String searchText = searchTextField.getText();
	    filterTableView(selectedCriteria, searchText);
	}
	
	
	private void filterTableView(String criteria, String searchText) {
	    
	    if (criteria == null || searchText == null || searchText.isEmpty()) {
	        // If criteria or searchText is null or empty, show all items
	        assetsTable.setItems(allAssets);
	        return;
	    }

	    ObservableList<Asset> filteredList = FXCollections.observableArrayList();
	    switch (criteria) {
	        case "Asset Category":
	            for (Asset asset : allAssets) {
	                if (asset.getAsset_category().toLowerCase().contains(searchText.toLowerCase())) {
	                    filteredList.add(asset);
	                }
	            }
	            break;
	        case "Asset Type":
	            for (Asset asset : allAssets) {
	                if (asset.getAsset_type().toLowerCase().contains(searchText.toLowerCase())) {
	                    filteredList.add(asset);
	                }
	            }
	            break;
	        case "Model":
	            for (Asset asset : allAssets) {
	                if (asset.getAsset_model().toLowerCase().contains(searchText.toLowerCase())) {
	                    filteredList.add(asset);
	                }
	            }
	            break;
	        case "Status":
	            for (Asset asset : allAssets) {
	                if (asset.getAsset_status().toLowerCase().contains(searchText.toLowerCase())) {
	                    filteredList.add(asset);
	                }
	            }
	            break;
	        case "Location":
	            for (Asset asset : allAssets) {
	                if (asset.getAsset_location().toLowerCase().contains(searchText.toLowerCase())) {
	                    filteredList.add(asset);
	                }
	            }
	            break;
	        // Add cases for other criteria
	    }

	    // Update TableView with filtered list
	    assetsTable.setItems(filteredList);
	}
}

