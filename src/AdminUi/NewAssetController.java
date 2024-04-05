package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Components.Asset;
import application.DB_Assets;
import application.DB_Rooms;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;



public class NewAssetController implements Initializable{
	
	public static final String IN_INVENTORY = "In Inventory";
	public static final String IN_USE = "In Use";
	public static final String LOST_STOLEN = "Lost/Stolen";
	public static final String UNDER_MAINTENANCE = "Under Maintenance";
	public static final String BROKEN = "Broken";
	
	private final String HARDWARE = "Hardware";
	private final String SOFTWARE = "Software";
	private final String ACCESSORY = "Accessory";
	
	private String[] categories = {HARDWARE,SOFTWARE,ACCESSORY};
	private String[] hardware_type = {"Desktop" , "Laptop", "Projector", "Scanner", "Printer", "Switch" ,"Hub","Router","Modem" };
	private String[] software_type = {"Anti-Virus","License"};
	private String[] accessory_type = {"Keyboard","Mouse","Cable","HDD","SSD","RAM"};
	public static String[] statuses = {IN_INVENTORY,IN_USE,LOST_STOLEN};//all status for now , feel free to add any more statuses.
	
	//*******hardware models:*********************
	private String[] desktop_laptop_models = {"HP","Dell","Lenovo","Acer","Apple","MSI","Razer"};
	private String[] projector_scanner_printer_models = {"Canon" , "Epson" , "HP" , "Toshiba"};
	private String[] networking_equipment_models = {"Dell EMC" , "Cisco Systems", "TP-Link" , "D-Link" , "Juniper Networks" , "None of the Above"};
	//*******software models:*********************
	private String[] anti_virus_models = {"Kasperky","AVG","Avast"};

	
	//*******Accessory models:********************
	private String[] keyboard_mouse_models = {"HP","Razer","Logitech"};
	private String[] components_models = {"SanDisk","Samsung","Toshiba","GIGABYTE" , "Kingston Technology" , "ADATA Technology" , "Corsair"};
	private ArrayList<String> rooms = DB_Rooms.getRooms();

	@FXML
	private ChoiceBox<String> categoryChoiceBox;
	@FXML
	private ChoiceBox<String> typeChoiceBox;	
	@FXML
	private ChoiceBox<String> modelChoiceBox;
	@FXML
	private ChoiceBox<String> statusChoiceBox;
	@FXML
	private ChoiceBox<String> locationDropDownBox;
	
	@FXML
	private TextField serialField,warrantyField;
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label invalidInfo;
	
	//old asset
	private Asset oldAsset;
	
	public void setOldAsset(Asset asset) {
		this.oldAsset = asset;
	}
	//title label
	@FXML
	private Label titleLabel;
	
	public void setTitleLabelText(String text) {
		this.titleLabel.setText(text);
	}
	
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		
		categoryChoiceBox.getItems().addAll(categories); // add asset categories.
		locationDropDownBox.getItems().addAll(rooms);
		
		categoryChoiceBox.setOnAction(event->{
			typeChoiceBox.getItems().clear();
			if(categoryChoiceBox.getValue() == HARDWARE) {
				typeChoiceBox.getItems().addAll(hardware_type);
				
			}else if(categoryChoiceBox.getValue() == SOFTWARE) {
				typeChoiceBox.getItems().addAll(software_type);
				
			}else if(categoryChoiceBox.getValue() == ACCESSORY) {
				typeChoiceBox.getItems().addAll(accessory_type);
			}
		});
		typeChoiceBox.setOnAction(event->{
			modelChoiceBox.getItems().clear();
			if(typeChoiceBox.getValue() == "Desktop" || typeChoiceBox.getValue() == "Laptop") {
				modelChoiceBox.getItems().addAll(desktop_laptop_models);
			}else if(typeChoiceBox.getValue() == "Projector" || typeChoiceBox.getValue() == "Scanner" || typeChoiceBox.getValue() == "Printer") {
				modelChoiceBox.getItems().addAll(projector_scanner_printer_models);
			}else if(typeChoiceBox.getValue() == "Hub" || typeChoiceBox.getValue() == "Switch" || typeChoiceBox.getValue() == "Router" || typeChoiceBox.getValue() == "Modem") {
				modelChoiceBox.getItems().addAll(networking_equipment_models);
			}else if(typeChoiceBox.getValue() == "Keyboard" || typeChoiceBox.getValue() == "Mouse") {
				modelChoiceBox.getItems().addAll(keyboard_mouse_models);
			}else if(typeChoiceBox.getValue() == "HDD" || typeChoiceBox.getValue() == "SSD" || typeChoiceBox.getValue() == "RAM") {
				modelChoiceBox.getItems().addAll(components_models);
			}else if(typeChoiceBox.getValue() == "Anti-Virus") {
				modelChoiceBox.getItems().addAll(anti_virus_models);
			}
		});
		statusChoiceBox.getItems().addAll(statuses);
	}
	
	public void validateInformation(ActionEvent event) throws IOException {
		if(categoryChoiceBox.getValue() == null) {
			invalidInfo.setText("You must select a category!");//category
			animatedInvalidInfolabel();
			return;
		}
		if(typeChoiceBox.getValue() == null) {
			invalidInfo.setText("You must select a type!");//type
			animatedInvalidInfolabel();
			return;
		}
		if(modelChoiceBox.getValue() == null && (typeChoiceBox.getValue() != "Cable" && typeChoiceBox.getValue() != "License")) {
			invalidInfo.setText("You must select a model!");//model
			animatedInvalidInfolabel();
			return;
		}
		
		if(!serialField.getText().matches("[0-9]+$")) {//Serial Number.
			invalidInfo.setText("Invalid Serial number!");
			animatedInvalidInfolabel();
			return;
		}
		
		try {				
			if(!warrantyField.getText().matches("[0-9]+$") || Integer.parseInt(warrantyField.getText()) < 0 ) {//warranty in months.
				invalidInfo.setText("Invalid warranty value!");
				animatedInvalidInfolabel();
				return;
			}
		}catch(Exception e) {
			//only triggered when parsing the text in warranty (in case the user enters a string and for the above comparaison statement this block is triggered)
			invalidInfo.setText("Invalid warranty value!");
			animatedInvalidInfolabel();
			return;
		}
		
		if(statusChoiceBox.getValue() == null) {//status
			invalidInfo.setText("You must select a status!");
			animatedInvalidInfolabel();
			return;
		}
		
		if(locationDropDownBox.getValue() == null) {//Location
			invalidInfo.setText("Invalid Location!");
			animatedInvalidInfolabel();
			return;
		}
		int id = 0;
		String category = categoryChoiceBox.getValue();
		String type = typeChoiceBox.getValue();
		String model = modelChoiceBox.getValue();
		String status = statusChoiceBox.getValue();
		String room = locationDropDownBox.getValue();
		int warranty = Integer.parseInt(warrantyField.getText());
		int serial_number = Integer.parseInt(serialField.getText());
		
		Date date = java.sql.Date.valueOf(LocalDate.now());
		
		Asset new_asset = new Asset(id,category,type,model,status,room,date,warranty,serial_number);
		
		if(oldAsset == null) {
			newAsset(new_asset);
		}else {
			updateAsset(new_asset);
		}
	    
		disposeWindow(event);
	}
	
	
	private void newAsset(Asset newAsset) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().addAsset(newAsset);
	}
	
	private void updateAsset(Asset newAsset) {
		((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().updateAsset(oldAsset, newAsset);
	}
	
	void setInfos() {
		categoryChoiceBox.setValue(oldAsset.getAsset_category());
		typeChoiceBox.setValue(oldAsset.getAsset_category());
		modelChoiceBox.setValue(oldAsset.getAsset_model());
		statusChoiceBox.setValue(oldAsset.getAsset_status());
		locationDropDownBox.setValue(oldAsset.getAsset_room());
		warrantyField.setText(String.valueOf(oldAsset.getAsset_warranty()));
		serialField.setText(String.valueOf(oldAsset.getAsset_serial_number()));
	}
	
	private void clearInputs() {
		//	assetTypeInput.clear();
//        modelInput.clear();
        serialField.clear();
        warrantyField.clear();
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
