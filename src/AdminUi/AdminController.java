package AdminUi;

import Components.*;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AdminController implements Initializable{

	
	@FXML
    private TableView<Asset> assetsTable;

    @FXML
    private TableColumn<Asset, String> assetIdColumn;

    @FXML
    private TableColumn<Asset, String> assetTypeColumn;

    @FXML
    private TableColumn<Asset, String> modelColumn;

    @FXML
    private TableColumn<Asset, String> statusColumn;

    @FXML
    private TableColumn<Asset, String> locationColumn;
    
    @FXML
    private TextField assetTypeInput;

    @FXML
    private TextField modelInput;

    @FXML
    private TextField statusInput;

    @FXML
    private TextField locationInput;
    
    @FXML
    private TextField assetIdInput;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Initialize table columns
        assetIdColumn.setCellValueFactory(cellData -> cellData.getValue().assetIdProperty());
        assetTypeColumn.setCellValueFactory(cellData -> cellData.getValue().assetTypeProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
       
		
	}
	
	public void showHardwareAssets() {
        // Logic to populate the TableView with sample hardware assets
        assetsTable.getItems().clear(); // Clear previous data

        // Add sample hardware assets
        assetsTable.getItems().addAll(
                new HardwareAsset("1", "PC", "Dell", "Working", "Room 101"),
                new HardwareAsset("2", "Printer", "HP", "Needs Maintenance", "Room 103")
                // Add more hardware assets as needed
        );
    }

    public void showSoftwareAssets() {
        // Logic to populate the TableView with sample software assets
        assetsTable.getItems().clear(); // Clear previous data

        // Add sample software assets
        assetsTable.getItems().addAll(
                new SoftwareAsset("1", "Operating System", "Windows 10", "Installed", "Room 101"),
                new SoftwareAsset("2", "Office Suite", "Microsoft Office", "Installed", "Room 101")
                // Add more software assets as needed
        );
    }

    public void addAsset() {
        // Get user input
        String assetId = assetIdInput.getText().trim();
        String assetType = assetTypeInput.getText().trim();
        String model = modelInput.getText().trim();
        String status = statusInput.getText().trim();
        String location = locationInput.getText().trim();

        // Check if ID field is empty
        if (assetId.isEmpty()) {
            // Display error message
            displayErrorMessage("Error", "Please enter an asset ID.");
            return;
        }

        // Check if entered ID already exists
        for (Asset asset : assetsTable.getItems()) {
            if (asset.getAssetId().equals(assetId)) {
                // Display error message
                displayErrorMessage("Error", "Asset with ID " + assetId + " already exists.");
                return;
            }
        }

        // Create and add new asset
        Asset newAsset;
        if (assetType.equals("Hardware")) {
            newAsset = new HardwareAsset(assetId, assetType, model, status, location);
        } else {
            newAsset = new SoftwareAsset(assetId, assetType, model, status, location);
        }
        assetsTable.getItems().add(newAsset);

        // Clear input fields
        clearInputs();
    }

    private void displayErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void removeAssetById() {
        String assetIdToRemove = assetIdInput.getText();
        Asset assetToRemove = null;
        for (Asset asset : assetsTable.getItems()) {
            if (asset.getAssetId().equals(assetIdToRemove)) {
                assetToRemove = asset;
                break;
            }
        }
        if (assetToRemove != null) {
            assetsTable.getItems().remove(assetToRemove);
        }
    }

    public void modifyAssetById() {
        String assetIdToModify = assetIdInput.getText();
        for (Asset asset : assetsTable.getItems()) {
            if (asset.getAssetId().equals(assetIdToModify)) {
                String assetType = assetTypeInput.getText();
                String model = modelInput.getText();
                String status = statusInput.getText();
                String location = locationInput.getText();

                // Update asset with new data
                asset.setAssetType(assetType);
                asset.setModel(model);
                asset.setStatus(status);
                asset.setLocation(location);

                // Refresh TableView to reflect changes
                assetsTable.refresh();
                break;
            }
        }
    }

    private String generateAssetId() {
        // Generate a new unique asset ID here, e.g., based on current timestamp
        return String.valueOf(System.currentTimeMillis());
    }

    private void clearInputs() {
        assetTypeInput.clear();
        modelInput.clear();
        statusInput.clear();
        locationInput.clear();
        assetIdInput.clear();
    }

}
