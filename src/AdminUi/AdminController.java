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
import javafx.util.Duration;
import java.util.Optional;
import java.util.ResourceBundle;
import Components.Asset;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import LoginUi.LoginController;
import application.DatabaseUtilities;
import application.Helper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.util.ArrayList;



public class AdminController implements Initializable{
	
	//sidebar mats:*****************
	@FXML
	private VBox sideBar;
	
//	@FXML
//	private BorderPane dashboard; // Main Border pane (holds all content here (dashboard for now))
	
	@FXML
	private Button collapser;
	@FXML
	private Pane bar;
	@FXML
	private Pane topBar;
	@FXML
	private FontAwesomeIconView menuIcon;
	//****************************
	
	//All Assets Section Mats:******************/
	@FXML
	private Button AllAssetsButton;
	@FXML
	private Button AllUsersButton;
	@FXML
	private Button dashboardButton;
	//*****************************************/
	
	//User and Asset Loaders
	public static final String fxmlNewAsset = "/AdminUi/newAssetScene.fxml";
	public static final String fxmlNewUser = "/AdminUi/newUserScene.fxml";
	public static FXMLLoader currentNewAssetLoader;
	public static FXMLLoader currentNewUserLoader;
	
	
	//Scenes***********************************/
	
	@FXML
	private BorderPane allAssetsView;
	@FXML
	private AllAssetsController allAssetsViewController;
	
	public AllAssetsController getAllAssetsViewController() {
		return allAssetsViewController;
	}
	
	@FXML
	private BorderPane allUsersView;
	@FXML
	private AllUsersController allUsersViewController;
	
	public AllUsersController getAllUsersViewController() {
		return allUsersViewController;
	}
	
	@FXML
	private BorderPane dashboardView;
	@FXML
	private DashboardController dashboardViewController;
	
	public DashboardController getDashboardViewController() {
		return dashboardViewController;
	}
	//*****************************************/
	
	//Array of panes
	Pane[] views;
	//Set only one sence (constants)
	
	public static int DASHBOARD_VIEW = 0;
	public static int USERS_VIEW = 1;
	public static int ASSETS_VIEW = 2;
	public static int SETTING_VIEW = 3;
	public static int BACKUP_VIEW = 4;
	
	
    
	//logging out mats:***********
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	//****************************
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//initialize views array
		views = new Pane[]{dashboardView ,allUsersView, allAssetsView};
		
		// --> here we will set the default view (dashboardview) 	
		
		//All Assets VBox Visibility set to false:
		selectView(DASHBOARD_VIEW);
		
		// Initialize table columns
		//estalish a connection to the SupaBase Database For :
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ArrayList<Asset> bufferListAssets = new ArrayList<Asset>();
			ArrayList<User> bufferListUsers = new ArrayList<User>();
			
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(LoginController.url);
			String getAllAssetsQuery = "SELECT * FROM assets";
			ps = con.prepareStatement(getAllAssetsQuery);
			rs = ps.executeQuery();
			//Assets:

			ps = con.prepareStatement(getAllAssetsQuery);
			rs = ps.executeQuery();

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
			allAssetsViewController.getAssetsTable().getItems().addAll(bufferListAssets);
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
			
			allUsersViewController.getUsersTable().getItems().addAll(bufferListUsers);
			bufferListUsers.clear();
	        
	        
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
	
	

	
	private boolean isOpenedSideBar = false;
	
	private Timeline openTimeLine;
	private Timeline closeTimeLine;
	
	public void toggleSidebar(){	
		if(isOpenedSideBar) {
			if(openTimeLine != null) {
				openTimeLine.stop();
			}
			closeSideBar();
		}else {
			if(closeTimeLine != null) {
				closeTimeLine.stop();
			}
			openSideBar();
		}
    }
	
	public void openSideBar() {
		
		bar.setVisible(true);
		topBar.setVisible(true);
		//open
		isOpenedSideBar = true;
		KeyValue collapOpenKeyVal = new KeyValue(collapser.translateXProperty(), 0);
		KeyValue sideBarOpenKeyVal = new KeyValue(sideBar.translateXProperty(), 0);
		KeyValue barOpenVal = new KeyValue(bar.opacityProperty(), 0.5);
		KeyValue topBarOpenVal = new KeyValue(topBar.opacityProperty(), 0.5);
		
		KeyFrame openSideBarFrame = new KeyFrame(Duration.millis(300), collapOpenKeyVal, sideBarOpenKeyVal, barOpenVal, topBarOpenVal);
		KeyFrame openMenuIconFrame = new KeyFrame(Duration.millis(50), e -> {
			if(isOpenedSideBar)
				menuIcon.translateXProperty().set(0);
		});
		openTimeLine = new Timeline(openSideBarFrame, openMenuIconFrame);
		openTimeLine.play();
	}
	
	
	public void closeSideBar() {
		double closedCollapWidth = 40;
		double shiftedMenuIconX = collapser.getWidth()-menuIcon.getLayoutX()-closedCollapWidth+9;
		
		//close
		isOpenedSideBar = false;
		KeyValue collapCloseKeyVal = new KeyValue(collapser.translateXProperty(), -collapser.getWidth()+closedCollapWidth);
		KeyValue sideBarCloseKeyVal = new KeyValue(sideBar.translateXProperty(), -sideBar.getWidth());
		KeyValue barCloseVal = new KeyValue(bar.opacityProperty(), 0);
		KeyValue topBarCloseVal = new KeyValue(topBar.opacityProperty(), 0);
		
		KeyFrame closeSideBarFrame = new KeyFrame(Duration.millis(300), collapCloseKeyVal, sideBarCloseKeyVal, barCloseVal, topBarCloseVal); //add dashboardSpace
		KeyFrame closeMenuIconFrame = new KeyFrame(Duration.millis(350), e -> {
			if(!isOpenedSideBar)
				menuIcon.translateXProperty().set(shiftedMenuIconX);
		});
		
		closeTimeLine = new Timeline(closeSideBarFrame, closeMenuIconFrame);
		closeTimeLine.setOnFinished(e -> {
			bar.setVisible(false);
			topBar.setVisible(false);
		});
		closeTimeLine.play();
		
	}
	
	public void closeWithBar() {
		if(closeTimeLine != null && closeTimeLine.getStatus() != Status.RUNNING) {
			closeSideBar();
		}
	}
	
	//not now !
	public void LogOut(ActionEvent event) throws IOException {
		Helper.currentLoginLoader = new FXMLLoader(getClass().getResource(Helper.fxmlLogin));
		root = Helper.currentLoginLoader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loginScene = new Scene(root);
		stage.setScene(loginScene);
		stage.setTitle("Stockify - Login");
		
		stage.show();
	}
	//
	
	public void triggerDashBoardPane(ActionEvent event) {
		//set all visiblity to false.
		selectView(DASHBOARD_VIEW);
		closeSideBar();
	}
	
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
