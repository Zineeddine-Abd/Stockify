package TechnicianUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Components.Asset;
import Components.Hardware;
import Components.Software;
import application.DB_Rooms;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;



public class NewAssetController implements Initializable{
	
	public static final String IN_INVENTORY = "In Inventory";
	public static final String IN_USE = "In Use";
	public static final String LOST_STOLEN = "Lost/Stolen";
	public static final String UNDER_MAINTENANCE = "Under Maintenance";
	public static final String BROKEN = "Broken";
	public static final String READY_TO_USE = "Ready To Use";
//CATEGORIES:
	private final String HARDWARE = "Hardware";
	private final String SOFTWARE = "Software";
	private final String ACCESSORY = "Accessory/External Peripherals";
	private final String NETWORKING = "Networking Equipment";
	private String[] categories = {HARDWARE,SOFTWARE,ACCESSORY,NETWORKING};
//TYPES:
	private String[] hardware_type = {"Desktop" ,"Monitor", "Laptop", "Projector", "Scanner", "Printer"};
	private String[] software_type = {"Anti-Virus", "IDE" , "Operating System"};
	private String[] accessory_type = {"Keyboard","Mouse","Cable","HDD","SSD","RAM","Motherboard","CPU"};
	private String[] networking_type = {"Switch" ,"Hub","Router","Modem" ,"Network Attached Storage","Firewall","Load Balancer"};
	public static String[] statuses = {IN_INVENTORY,IN_USE,LOST_STOLEN};//all status for now , feel free to add any more statuses.
//MODELS:
	//hardware models:
	private String[] monitor_models = {"HP","MSI","LG","Condor","Dell","Asus","Lenovo","Acer","Apple"};
	private String[] projector_scanner_printer_models = {"Canon" , "Epson" , "HP" , "Toshiba"};
	//networking models:
	private String[] SHRM_models = {"Dell EMC" , "Cisco Systems", "TP-Link" , "D-Link" , "Juniper Networks"};
	private String[] firewall_models = {"Cisco","Palo Alto Networks","Fortinet","McAfee","Sophos"};
	private String[] loadBalancer_models = {"F5 Networks","A10 Networks","HAProxy"};
	private String[] nas_models = {"Synology","QNAP Systems","Asustor","Netgear","Seagate"};
	//software models:
	private String[] anti_virus_models = {"Kasperky","AVG","Avast"};
	private String[] ide_models = {"Eclipse IDE" , "IntelliJ Idea" , "NetBeans" , "VSCode" , "CodeBlocks" , "Anaconda"};
	private String[] os_models = {"Windows XP","Windows 7" , "Windows 8" , "Windows 10" , "Windows 11" , "Ubuntu" , "Linux mint" , "Kali Linux","Debian","Fedora"};
	//Accessory models:
	private String[] keyboard_mouse_models = {"HP","Razer","Logitech","Corsair","Kingston","SteelSeries","MSI","Asus"};
	private String[] components_models = {"Asus","MSI","SanDisk","Samsung","Toshiba","GIGABYTE" , "Kingston Technology" , "ADATA Technology" , "Corsair"};
	private String[] mb_cpu_models = {"Intel","AMD","GIGABYTE","MSI","Asus"};
	
	
	private ArrayList<String> rooms = DB_Rooms.getRooms();
	
	public static final int ASSET_MODE = 0;
	public static final int HARDWARE_MODE = 1;
	public static final int SOFTWARE_MODE = 2;
	

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
	private TextField warrantyField;
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label invalidInfo;
	@FXML
	private DatePicker assetPurchaseDate;
	
	
	@FXML
	private VBox parentVBox;
	@FXML
	private VBox hardwareVbox;
	@FXML
	private VBox softwareVbox;
	@FXML
	private TextField serialNumField;
	@FXML
	private TextField softLicenseKeyField;
	@FXML
	private TextField softVersionField;
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
		
		
		parentVBox.getChildren().removeAll(hardwareVbox, softwareVbox);
		
		parentVBox.heightProperty().addListener((obs, oldVal, newVal) -> {
			Scene mainScene = parentVBox.getScene();
			Stage mainStage = (Stage) mainScene.getWindow();
			mainStage.setHeight(mainStage.getHeight() + newVal.doubleValue() - oldVal.doubleValue());
		});
		
		categoryChoiceBox.getItems().addAll(categories); // add asset categories.
		locationDropDownBox.getItems().addAll(rooms);
		
		
		categoryChoiceBox.setOnAction(event->{
			typeChoiceBox.getItems().clear();
			
			if(categoryChoiceBox.getValue() == HARDWARE) {
				typeChoiceBox.getItems().addAll(hardware_type);
				
				if(!parentVBox.getChildren().contains(hardwareVbox)) {
					parentVBox.getChildren().add(hardwareVbox);
				}
				
				if(parentVBox.getChildren().contains(softwareVbox)) {					
					parentVBox.getChildren().remove(softwareVbox);
				}
				
			}else if(categoryChoiceBox.getValue() == SOFTWARE) {
				typeChoiceBox.getItems().addAll(software_type);
				
				if(!parentVBox.getChildren().contains(softwareVbox)) {
					parentVBox.getChildren().add(softwareVbox);
				}
				if(parentVBox.getChildren().contains(hardwareVbox)) {					
					parentVBox.getChildren().remove(hardwareVbox);
				}
			}else if(categoryChoiceBox.getValue() == ACCESSORY) {
				typeChoiceBox.getItems().addAll(accessory_type);
			}else if(categoryChoiceBox.getValue() == NETWORKING) {
				typeChoiceBox.getItems().addAll(networking_type);
			}
		});
		
		
		typeChoiceBox.setOnAction(event->{
			modelChoiceBox.getItems().clear();
			
			if(typeChoiceBox.getValue() == "Desktop" || typeChoiceBox.getValue() == "Laptop" || typeChoiceBox.getValue() == "Monitor") {
				modelChoiceBox.getItems().addAll(monitor_models);
			}else if(typeChoiceBox.getValue() == "Projector" || typeChoiceBox.getValue() == "Scanner" || typeChoiceBox.getValue() == "Printer") {
				modelChoiceBox.getItems().addAll(projector_scanner_printer_models);
			}else if(typeChoiceBox.getValue() == "Hub" || typeChoiceBox.getValue() == "Switch" || typeChoiceBox.getValue() == "Router" || typeChoiceBox.getValue() == "Modem") {
				modelChoiceBox.getItems().addAll(SHRM_models);
			}else if(typeChoiceBox.getValue() == "Keyboard" || typeChoiceBox.getValue() == "Mouse") {
				modelChoiceBox.getItems().addAll(keyboard_mouse_models);
			}else if(typeChoiceBox.getValue() == "HDD" || typeChoiceBox.getValue() == "SSD" || typeChoiceBox.getValue() == "RAM") {
				modelChoiceBox.getItems().addAll(components_models);
			}else if(typeChoiceBox.getValue() == "Motherboard" || typeChoiceBox.getValue() == "CPU") {
				modelChoiceBox.getItems().addAll(mb_cpu_models);
			}else if(typeChoiceBox.getValue() == "Anti-Virus") {
				modelChoiceBox.getItems().addAll(anti_virus_models);
			}else if(typeChoiceBox.getValue() == "IDE") {
				modelChoiceBox.getItems().addAll(ide_models);
			}else if(typeChoiceBox.getValue() == "Operating System") {
				modelChoiceBox.getItems().addAll(os_models);
			}else if(typeChoiceBox.getValue() == "Firewall") {
				modelChoiceBox.getItems().addAll(firewall_models);
			}else if(typeChoiceBox.getValue() == "Load Balancer") {
				modelChoiceBox.getItems().addAll(loadBalancer_models);
			}else if(typeChoiceBox.getValue() == "Network Attached Storage") {
				modelChoiceBox.getItems().addAll(nas_models);
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
		
		if(assetPurchaseDate.getValue() == null) {
			invalidInfo.setText("Invalid Date!");
			animatedInvalidInfolabel();
			return;
		}
		
		String category = categoryChoiceBox.getValue();
		String type = typeChoiceBox.getValue();
		String model = modelChoiceBox.getValue();
		String status = statusChoiceBox.getValue();
		String room = locationDropDownBox.getValue();
		int warranty = Integer.parseInt(warrantyField.getText());
		Date date = java.sql.Date.valueOf(assetPurchaseDate.getValue());
		Asset new_asset = new Asset(0,category,type,model,status,room,date,warranty);
		
		try {
			
			switch(categoryChoiceBox.getValue()) {
				case "Hardware":
					if(!serialNumField.getText().matches("^[a-zA-Z0-9]+$") && serialNumField.getText() != null) {
						invalidInfo.setText("Invalid Serial Number!");
						animatedInvalidInfolabel();
						return;
					}
					String serial_num = serialNumField.getText();
					Hardware new_hard = new Hardware(new_asset,serial_num);
					
					if(oldAsset == null) {
						//do nothing for 
					}else {
						updateAsset(new_hard);
					}
					disposeWindow(event);
					return;
				case "Software":
					
					if(!softLicenseKeyField.getText().matches("^[a-zA-Z0-9]+$") && softLicenseKeyField.getText() != null) {
						invalidInfo.setText("Invalid License Key!");
						animatedInvalidInfolabel();
						return;
					}
					
					if(!softVersionField.getText().matches("^[\\d.,]+$") && softVersionField.getText() != null) {
						invalidInfo.setText("Invalid Version!");
						animatedInvalidInfolabel();
						return;
					}
					
					String licenseKey = softLicenseKeyField.getText();
					String version = softVersionField.getText();
					
					Software new_soft = new Software(new_asset,licenseKey,version);
					
					if(oldAsset == null) {
						//do nothing cuz no new for you technician !!!!.
					}else {
						updateAsset(new_soft);
					}
					disposeWindow(event);
					return;
			}
			
			if(oldAsset == null) {
				//do nothing cuz no new for technician
			}else {
				updateAsset(new_asset);
			}
		}catch(NullPointerException e) {
			Helper.displayErrorMessage("Error", e.getMessage());
		}
		
		disposeWindow(event);
	}
	
	private void updateAsset(Asset newAsset) {
		((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().updateAsset(oldAsset, newAsset);
	}
	
//	private void updateHardware(Hardware hardware) {
//		((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().updateHardware(oldAsset,hardware);
//	}
//	
//	private void updateSoftware(Software software) {
//		((TechnicianController)Helper.currentTechnicianLoader.getController()).getAllAssetsViewController().updateSoftware(oldAsset, software);
//	}
	
	void setInfos() {
		categoryChoiceBox.setValue(oldAsset.getAsset_category());
		typeChoiceBox.setValue(oldAsset.getAsset_type());
		modelChoiceBox.setValue(oldAsset.getAsset_model());
		statusChoiceBox.setValue(oldAsset.getAsset_status());
		locationDropDownBox.setValue(oldAsset.getAsset_room());
		warrantyField.setText(String.valueOf(oldAsset.getAsset_warranty()));
		assetPurchaseDate.setValue(oldAsset.getAsset_purchase_date().toLocalDate());
		switch(oldAsset.getAsset_category()) {
		case "Hardware":
			Hardware hardware = (Hardware) oldAsset;
			serialNumField.setText(hardware.getHardware_serial_number());
			break;
		case "Software":
			Software software = (Software) oldAsset;
			softLicenseKeyField.setText(software.getSoftware_license_key());
			softVersionField.setText(software.getSoftware_version());
			break;
		}
	}
	
	public void setHardwareFields() {
		if(parentVBox.getChildren().contains(hardwareVbox)) {
			parentVBox.getChildren().remove(hardwareVbox);
		}
		categoryChoiceBox.setValue("Hardware");
	}
	public void setSoftwareFields() {
		if(parentVBox.getChildren().contains(softwareVbox)) {
			parentVBox.getChildren().remove(softwareVbox);
		}
		
		categoryChoiceBox.setValue("Software");
	}
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),invalidInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		parentVBox.getChildren().removeAll(hardwareVbox,softwareVbox);
		stage.close();
	}

}
