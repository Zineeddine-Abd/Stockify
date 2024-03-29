package AdminUi;

import Components.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
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
import application.DatabaseUtilities;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
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
	
	
	//Table_View assets components:********************
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
    private TableColumn<Asset, Date> assetPurchaseDateColumn;

    @FXML
    private TableColumn<Asset, Integer> assetWarrantyColumn;

    @FXML
    private TableColumn<Asset, Integer> assetSerialNumberColumn;
    
    //Table_view users Components:******************************
    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> user_idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;
    
    @FXML
    private TableColumn<User, String> pass_wordColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> full_nameColumn;

    @FXML
    private TableColumn<User, String> user_roleColumn;
    
	
	//logging out mats:***********
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	//****************************
	
	//creating new asset/user mats:********************
	private Stage fillFormula;
	private Scene createNewScene;
	//*************************************************
	
	//attribute to retrieve the last id inserted into the DB.
	public static int last_id=0;
	
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
<<<<<<< HEAD
		
		//estalish a connection to the SupaBase Database For :
		
=======
		//estalish a connection to the SupaBase Database.
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
>>>>>>> 6f95ec010fa0424cffb9d0f3e0ffd7d448d12a1a
		try {
			ArrayList<Asset> bufferListAssets = new ArrayList<Asset>();
			ArrayList<User> bufferListUsers = new ArrayList<User>();
			
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(LoginController.url);
			String getAllAssetsQuery = "SELECT * FROM assets";
<<<<<<< HEAD
			PreparedStatement ps = con.prepareStatement(getAllAssetsQuery);
			ResultSet rs = ps.executeQuery();
			//Assets:
=======
			ps = con.prepareStatement(getAllAssetsQuery);
			rs = ps.executeQuery();
>>>>>>> 6f95ec010fa0424cffb9d0f3e0ffd7d448d12a1a
			while(rs.next()) {//while the reader still has a next row read it:
				int asset_id = rs.getInt("asset_id");
				String asset_category = rs.getString("asset_category");
				String asset_type = rs.getString("asset_type");
				String asset_model = rs.getString("asset_model");
				String asset_status = rs.getString("asset_status");
				String asset_location = rs.getString("asset_location");
				Date asset_purchase_date = rs.getDate("asset_purchase_date");
				int asset_warranty = rs.getInt("asset_warranty");
				int asset_serial_number = rs.getInt("asset_serial_number");
				Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_location,asset_purchase_date,asset_warranty,asset_serial_number);
				bufferListAssets.add(asset);
			}
			assetsTable.getItems().addAll(bufferListAssets);
			bufferListAssets.clear();
			
			String getAllUsersQuery = "SELECT * FROM users";
			PreparedStatement ps2 = con.prepareStatement(getAllUsersQuery);
			ResultSet userResultSet = ps2.executeQuery();
			//Users:
			while(userResultSet.next()) {//while the reader still has a next row read it:
				int user_id = userResultSet.getInt("user_id");
				String username = userResultSet.getString("username");
				String pass_word = userResultSet.getString("pass_word");
				String email = userResultSet.getString("email");
				String full_name = userResultSet.getString("full_name");
				String user_role = userResultSet.getString("user_role");
				
				User newuser = new User(user_id,username,pass_word,email,full_name,user_role);
				
				bufferListUsers.add(newuser);
			}
			
			usersTable.getItems().addAll(bufferListUsers);
			bufferListUsers.clear();
			
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
	        
//	        assetPurchaseDateColumn.setCellFactory(DatePickerTableCell.forTableColumn());
//	        assetWarrantyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//	        assetSerialNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
	        
	        //Users table:
	        usersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	        user_idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("user_id"));
	        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
	        pass_wordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("pass_word"));
	        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
	        full_nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("full_name"));
	        user_roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("user_role"));
	        
	        
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closePreparedStatement(ps);
			DatabaseUtilities.closeConnnection(con);
		}
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
        DatabaseUtilities.insertItemIntoDatabase(newAsset);
        newAsset.setAsset_id(last_id);
        assetsTable.getItems().add(newAsset);
    }
	public void addUser(User newuser) {
		DatabaseUtilities.insertItemIntoDatabase(newuser);
        newuser.setUser_id(last_id);
        usersTable.getItems().add(newuser);
	}

	
    private void displayErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean displayConfirmMessge(String message,String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(message);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        	return true;
        } else {
        	return false;
        }
    }
   

    // Method to delete selected rows from the TableView
    public void deleteSelectedAssets() {
    	
    	ObservableList<Asset> selectedAssets = assetsTable.getSelectionModel().getSelectedItems();
    	
    	if(displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 
    		for(Asset item : selectedAssets) {
    			DatabaseUtilities.deleteItemFromDatabase(item);
    		}
    		
    		assetsTable.getItems().removeAll(selectedAssets);
    	}
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
