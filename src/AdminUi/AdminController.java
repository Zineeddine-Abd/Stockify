package AdminUi;

import Components.*;

import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;

import Components.Asset;
import javafx.animation.Animation.Status;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import application.Helper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;



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
	@FXML
	private Button roomsButton;
	//*****************************************/
	
	//User and Asset Loaders
	public static final String fxmlNewAsset = "/AdminUi/newAssetScene.fxml";
	public static final String fxmlNewUser = "/AdminUi/newUserScene.fxml";
	public static final String fxmlNewRoom = "/AdminUi/newRoomScene.fxml";
	
	public static final String fxmlReport = "/AdminUi/reportPopupScene.fxml";
	public static final String fxmlReportDetails = "/AdminUi/reportDetailsPopupScene.fxml";
	public static final String fxmlMessages = "/AdminUi/messages.fxml";
	
	public static FXMLLoader currentNewAssetLoader;
	public static FXMLLoader currentNewUserLoader;
	public static FXMLLoader currentNewRoomLoader;
	
	public static FXMLLoader currentReportPopupLoader;
	public static FXMLLoader currentReportDetailsPopupLoader;
	public static FXMLLoader currentMessagesLoader;
	
	
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
	
	@FXML
	private BorderPane allRoomsView;
	@FXML
	private AllRoomsController allRoomsViewController;
	
	public AllRoomsController getAllRoomsViewController() {
		return allRoomsViewController;
	}
	//*****************************************/
	
	//Array of panes
	Pane[] views;
	//Set only one sence (constants)
	
	public static int DASHBOARD_VIEW = 0;
	public static int USERS_VIEW = 1;
	public static int ASSETS_VIEW = 2;
	public static int ROOMS_VIEW = 3;
	public static int BACKUP_VIEW = 4;
	
	
    
	//logging out mats:***********
	private Stage stage;
	private Scene loginScene;
	private Parent root;
	//****************************
	
	//SideBar Attributes*******************
	private boolean isOpenedSideBar = false;
	
	private Timeline openTimeLine;
	private Timeline closeTimeLine;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//initialize views array
		views = new Pane[]{dashboardView ,allUsersView, allAssetsView,allRoomsView};
		
		// --> here we will set the default view (dashboardview) 	
		
		//All Assets VBox Visibility set to false:
		selectView(DASHBOARD_VIEW);
		
		// Initialize table columns
		//estalish a connection to the SupaBase Database For :
		
	}
	
	
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
		if(closeTimeLine == null) {
			closeTimeLine = new Timeline();
		}
		if(closeTimeLine.getStatus() != Status.RUNNING) {
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
	
	public void updateNumberOfItems() {
		
		ObservableList<Room> allRooms = getAllRoomsViewController().allRooms;
		long roomsCount = allRooms.size();
		
		ObservableList<Asset> allAssets = getAllAssetsViewController().allAssetsObs;
		long hardwaresCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Hardware")).count();
		long softwaresCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Software")).count();
		long accessoriesCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Accessory")).count();
				
		
		getDashboardViewController().numRooms.setText(String.valueOf(roomsCount));
		getDashboardViewController().numHardware.setText(String.valueOf(hardwaresCount));
		getDashboardViewController().numSoftware.setText(String.valueOf(softwaresCount));
		getDashboardViewController().numAccessory.setText(String.valueOf(accessoriesCount));
	}
	
	public void triggerDashBoardPane() {
		//set all visiblity to false.
		updateNumberOfItems();
		selectView(DASHBOARD_VIEW);
		closeSideBar();
	}
	
	public void triggerAssetPane() {
		//set all visiblity to false.
		selectView(ASSETS_VIEW);
		closeSideBar();
	}
	public void triggerUserPane() {
		//set all panes visiblity to false 
		selectView(USERS_VIEW);
		closeSideBar();
	}
	public void triggerRoomsPane() {
		selectView(ROOMS_VIEW);
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
