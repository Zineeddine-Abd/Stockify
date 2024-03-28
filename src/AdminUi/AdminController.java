package AdminUi;

import Components.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javafx.util.Duration;

import java.util.List;
import java.util.ResourceBundle;

import Components.Asset;
import Components.HardwareAsset;
import Components.SoftwareAsset;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import LoginUi.LoginController;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;


public class AdminController implements Initializable{
	
	//sidebar mats:*****************
	@FXML
	private VBox sideBar;
	@FXML
	private BorderPane dashboard; // Main Border pane (holds all content here (dashboard for now))
	@FXML
	private Button collapser;
	@FXML
	private Pane pan;
	@FXML
	private FontAwesomeIconView menuIcon;
	//****************************
	
	//All Assets Section Mats:******************/
	@FXML
	private Button AllAssetsButton;
	@FXML
	private Button AllUsersButton;
	//*****************************************/
	
	//User and Asset Loaders
	FXMLLoader newUserLoader;
	FXMLLoader newAssetLoader;
	//
	
	
	
	//constant for easy manipulation on code:
	private final String asset = "Asset";
	private final String user = "User";
	//choosing user or asset for creation:*****
	private MenuItem[] newOptions = {new MenuItem(asset),new MenuItem(user)};
	@FXML
	private VBox allAssetsPane;
	@FXML
	private Button newAssetButton;
	
	@FXML
	private VBox allUsersPane;
	@FXML
	private Button newUserButton;
	
	//temporary make views array and views' constants compatible
	private Pane dashboardView;
	//Array of panes
	Pane[] views;
	//Set only one sence (constants)
	
	public static int DASHBOARD_VIEW = 0;
	public static int USERS_VIEW = 1;
	public static int ASSETS_VIEW = 2;
	public static int SETTING_VIEW = 3;
	public static int BACKUP_VIEW = 4;
	
	
	//Table_View components:********************
	@FXML
    private TableView<Asset> assetsTable;

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
    private TableColumn<Asset, String> assetPurchaseDateColumn;

    @FXML
    private TableColumn<Asset, String> assetWarrantyColumn;

    @FXML
    private TableColumn<Asset, String> assetSerialNumberColumn;
    

    
	
	//logging out mats:***********
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	//****************************
	
	//creating new asset/user mats:********************
	private Stage fillFormula;
	private Scene createNewScene;
	
	//**********************all methods:*****************************************
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//initialize views array
		dashboardView = new Pane();
		views = new Pane[]{dashboardView ,allUsersPane, allAssetsPane};
		
		// --> here we will set the default view (dashboardview) 
		     //
		//
		
		
		//All Assets VBox Visibility set to false:
		allAssetsPane.setVisible(false);
		allUsersPane.setVisible(false);
		// Initialize table columns
		// Set the cell value factories for each column
		 // Create sample data
        List<Asset> sampleData = Arrays.asList(
                new Asset(1, "Category A", "Type A", "Model 1", "Active", "Location 1", "1 year", "SN001"),
                new Asset(2, "Category B", "Type B", "Model 2", "Inactive", "Location 2", "2 years", "SN002"),
                new Asset(3, "Category C", "Type C", "Model 3", "Maintenance", "Location 3", "3 years", "SN003")
        );
        // Convert sample data to observable list
        ObservableList<Asset> data = FXCollections.observableArrayList(sampleData);
        
        assetsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Set the data to the TableView
        assetIdColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_id"));
        assetCategoryColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_category"));
        assetTypeColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_type"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_model"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_status"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_location"));
        assetPurchaseDateColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_purchase_date"));
        assetWarrantyColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_warranty"));
        assetSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_serial_number"));
        
        //assetIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetCategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        modelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetPurchaseDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetWarrantyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        assetSerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        
        
        assetsTable.setItems(data);
        
       
	}
	
	public void createNewAsset(ActionEvent event){
		
		Parent root;
		
			
			try {
				newAssetLoader =new FXMLLoader(getClass().getResource("/AdminUi/assetScene.fxml"));
				
				root = newAssetLoader.load();
				((AssetController)newAssetLoader.getController()).setAdminController(this);
				
				fillFormula = new Stage();
				fillFormula.setResizable(false);
				
				fillFormula.setTitle("Create New Asset:");
				
				//AssetController.setStage(fillFormula);
				createNewScene = new Scene(root);
				createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
		
				fillFormula.setScene(createNewScene);
				fillFormula.getIcons().add(Main.itAssetLogo);
				fillFormula.show();
				
				
//				Dialog<ButtonType> dialog = new Dialog<>();
//				dialog.setDialogPane((DialogPane)root);
//				dialog.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		
		
	}
	
	public void addAsset(Asset newAsset) {
        assetsTable.getItems().add(newAsset);
        // Clear input fields
       // clearInputs();
    }

	
    private void displayErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
   

    // Method to delete selected rows from the TableView
    public void deleteSelectedAssets() {
        ObservableList<Asset> selectedAssets = assetsTable.getSelectionModel().getSelectedItems();
        assetsTable.getItems().removeAll(selectedAssets);
    }
    
    
	public void createNewUser(ActionEvent event) {
		Parent root;
		try {
			newUserLoader = new FXMLLoader(getClass().getResource("/AdminUi/userScene.fxml"));
			root = newUserLoader.load();
			((UserController)newUserLoader.getController()).setAdminController(this);
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
				fillFormula.setTitle("Create New User:");
	//			try {
	//				root = FXMLLoader.load(getClass().getResource("/AdminUi/userScene.fxml"));
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
				
				//UserController.setStage(fillFormula);
				
			createNewScene = new Scene(root);
			createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
			
			fillFormula.setScene(createNewScene);
			fillFormula.getIcons().add(Main.itAssetLogo);
			
			//((Stage)((Node)event.getSource()).getScene().getWindow()).set
			
			fillFormula.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
private boolean isOpenedSideBar = false;
	
	public void toggleSidebar(){	
		if(isOpenedSideBar) {
			closeSideBar();
		}else {
			openSideBar();
		}
    }
	
	public void openSideBar() {
		
		pan.setVisible(true);
		//open
		isOpenedSideBar = true;
		KeyValue collapOpenKeyVal = new KeyValue(collapser.translateXProperty(), 0);
		KeyValue sideBarOpenKeyVal = new KeyValue(sideBar.translateXProperty(), 0);
		KeyValue panOpenVal = new KeyValue(pan.opacityProperty(), 0.5);
		
		KeyFrame openSideBarFrame = new KeyFrame(Duration.millis(300), collapOpenKeyVal, sideBarOpenKeyVal, panOpenVal);
		KeyFrame openMenuIconFrame = new KeyFrame(Duration.millis(50), e -> {
			if(isOpenedSideBar)
				menuIcon.translateXProperty().set(0);
		});
		
		Timeline openTimeLine = new Timeline(openSideBarFrame, openMenuIconFrame);
		openTimeLine.setOnFinished(e -> {
			if(isOpenedSideBar)
				pan.setVisible(true);
		});
		openTimeLine.play();
	}
	
	
	public void closeSideBar() {
		double closedCollapWidth = 40;
		double shiftedMenuIconX = collapser.getWidth()-menuIcon.getLayoutX()-closedCollapWidth+9;
		
		//close
		isOpenedSideBar = false;
		KeyValue collapCloseKeyVal = new KeyValue(collapser.translateXProperty(), -collapser.getWidth()+closedCollapWidth);
		KeyValue sideBarCloseKeyVal = new KeyValue(sideBar.translateXProperty(), -sideBar.getWidth());
		KeyValue panCloseVal = new KeyValue(pan.opacityProperty(), 0);
		
		KeyFrame closeSideBarFrame = new KeyFrame(Duration.millis(300), collapCloseKeyVal, sideBarCloseKeyVal, panCloseVal); //add dashboardSpace
		KeyFrame closeMenuIconFrame = new KeyFrame(Duration.millis(350), e -> {
			if(!isOpenedSideBar)
				menuIcon.translateXProperty().set(shiftedMenuIconX);
		});
		
		Timeline closeTimeLine = new Timeline(closeSideBarFrame, closeMenuIconFrame);
		closeTimeLine.setOnFinished(e -> pan.setVisible(false));
		closeTimeLine.play();
		
	}
	
	//not now !
	public void LogOut(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource(LoginController.fxmlLogin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loginScene = new Scene(root);
		stage.setScene(loginScene);
		stage.setTitle("Stockify - Login");
		
		stage.show();
	}
	//
	
	public void triggerAssetPane(ActionEvent event) {
		//set all visiblity to false.
		selectView(ASSETS_VIEW);
		
		closeSideBar();
	}
	public void triggerUserPane(ActionEvent event) {
		//set all panes visiblity to false 
		selectView(USERS_VIEW);
		
		closeSideBar();
	}

	
	public void selectView(int view) {
		for(int i = 0; i < views.length; i++) {
			if(i != view) {
				views[i].setVisible(false);
			}else {
				views[i].setVisible(true);
			}
		}
	}
}
