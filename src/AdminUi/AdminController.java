package AdminUi;

import Components.*;
import LoginUi.LoginController;

import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;

import java.util.ResourceBundle;
import java.util.Stack;

import javafx.animation.Animation.Status;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import application.DB_Messages;
import application.Helper;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;



public class AdminController implements Initializable{
	
	//sidebar mats:*****************
	@FXML
	private VBox sideBar;
	@FXML
	private Button collapser;
	@FXML
	private Pane bar;
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
	
	public static final String fxmlConfirmPassword = "/AdminUi/confirmPasswordScene.fxml";
	public static final String fxmlpopupAccountInfo = "/AdminUi/popupAccountInfoScene.fxml";
	
	public static FXMLLoader currentNewAssetLoader;
	public static FXMLLoader currentNewUserLoader;
	public static FXMLLoader currentNewRoomLoader;
	public static FXMLLoader currentConfirmPasswordLoader;
	public static FXMLLoader currentPopupAccountInfo;
	
	public static FXMLLoader currentReportPopupLoader;
	public static FXMLLoader currentReportDetailsPopupLoader;
	public static FXMLLoader currentMessagesLoader;
	
	
	//Scenes***********************************/
	
	@FXML
	private BorderPane allAssetsView;
	@FXML
	private AssetsTableController allAssetsViewController;
	
	public AssetsTableController getAllAssetsViewController() {
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
	@FXML
	private Button accountButton;
	//*****************************************/
	
	//Array of panes
	Pane[] views;
	Stack<Integer> recentViews = new Stack<>();
	Stack<Integer> forwardViews = new Stack<>();
	//Stack<String> recentSearchText = new Stack<>();
	//Stack<String> forwardSearchText = new Stack<>();
	
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
	
	//message obs list
	private ObservableList<Message> messagesList;
	
	public ObservableList<Message> getMessagesList(){
		return messagesList;
	}
	
	//SideBar Attributes*******************
	private boolean isOpenedSideBar = false;
	
	private Timeline openTimeLine = new Timeline();
	private Timeline closeTimeLine = new Timeline();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//init account button:
		accountButton.setText(LoginController.getLoggedUser().getFullName());
		//init messages list
		messagesList = FXCollections.observableArrayList();
		DB_Messages.refresh(messagesList);
		//initialize sub scenes after inject their controllers
		allUsersViewController.initUsers();
		allRoomsViewController.initRooms();
		dashboardViewController.setItems();
		
		
		//initialize views array
		views = new Pane[]{dashboardView ,allUsersView, allAssetsView,allRoomsView};
		recentViews.add(DASHBOARD_VIEW);
		
		updateNumberOfItems();
		selectView(DASHBOARD_VIEW);	
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
		//open
		isOpenedSideBar = true;
		KeyValue collapOpenKeyVal = new KeyValue(collapser.translateXProperty(), 0);
		KeyValue sideBarOpenKeyVal = new KeyValue(sideBar.translateXProperty(), 0);
		KeyValue barOpenVal = new KeyValue(bar.opacityProperty(), 0.5);
		
		KeyFrame openSideBarFrame = new KeyFrame(Duration.millis(300), collapOpenKeyVal, sideBarOpenKeyVal, barOpenVal);
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
		KeyValue collapCloseKeyVal = new KeyValue(collapser.translateXProperty(), closedCollapWidth);
		KeyValue sideBarCloseKeyVal = new KeyValue(sideBar.translateXProperty(), -sideBar.getWidth());
		KeyValue barCloseVal = new KeyValue(bar.opacityProperty(), 0);
		
		KeyFrame closeSideBarFrame = new KeyFrame(Duration.millis(300), collapCloseKeyVal, sideBarCloseKeyVal, barCloseVal); //add dashboardSpace
		KeyFrame closeMenuIconFrame = new KeyFrame(Duration.millis(350), e -> {
			if(!isOpenedSideBar)
				menuIcon.translateXProperty().set(shiftedMenuIconX);
		});
		
		closeTimeLine = new Timeline(closeSideBarFrame, closeMenuIconFrame);
		closeTimeLine.setOnFinished(e -> {
			bar.setVisible(false);
		});
		closeTimeLine.play();
		
	}
	
	public void closeWithBar() {
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
		
		ObservableList<Asset> allAssets = getAllAssetsViewController().getAllAssetsObs();
		long hardwaresCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Hardware")).count();
		long softwaresCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Software")).count();
		long accessoriesCount = allAssets.stream().filter(item -> item.getAsset_category().equals("Accessory")).count();
				
		
		getDashboardViewController().numRooms.setText(String.valueOf(roomsCount));
		getDashboardViewController().numHardware.setText(String.valueOf(hardwaresCount));
		getDashboardViewController().numSoftware.setText(String.valueOf(softwaresCount));
		getDashboardViewController().numAccessory.setText(String.valueOf(accessoriesCount));
	}
	
	public void triggerDashBoardPane() {
		if(!recentViews.peek().equals(DASHBOARD_VIEW)) {
			recentViews.add(DASHBOARD_VIEW);
		}
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
			updateNumberOfItems();
			selectView(DASHBOARD_VIEW);
			closeSideBar();
		
	}
	
	public void triggerAssetPane() {
		if(!recentViews.peek().equals(ASSETS_VIEW)) {
			recentViews.add(ASSETS_VIEW);
		}
			//String searchText = ((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.getText();
			//recentSearchText.add(searchText);
			selectView(ASSETS_VIEW);
			closeSideBar();
		
	}
	public void triggerUserPane() {
		if(!recentViews.peek().equals(USERS_VIEW)) {
			recentViews.add(USERS_VIEW);
		}
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
			selectView(USERS_VIEW);
			closeSideBar();
		
	}
	public void triggerRoomsPane() {
		if(!recentViews.peek().equals(ROOMS_VIEW)) {
			recentViews.add(ROOMS_VIEW);
		}
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
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
	
	public void goBackView() {
		if(recentViews.size() > 1) {
			int recentView = recentViews.pop();
			forwardViews.add(recentView);
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
			
//			if(recentViews.peek() == 2) {
//				String searchText = recentSearchText.pop();
//				forwardSearchText.add(searchText);
//				System.out.println("forwardSearchText Peek : " + forwardSearchText.peek());
//				((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText(searchText);
//			}
//			else {
//				((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
//			}
			selectView(recentViews.peek());
		}
	}
	
	public void goforwardView() {
		if(forwardViews.size() > 0) {
			int recentView = forwardViews.pop();
			recentViews.add(recentView);
			((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
			
//			if(recentViews.peek() == 2) {
//				String searchText;
//				if(forwardSearchText.size() != 0) {
//					System.out.println("in goForwardView : forwardSearchText Peek : " + forwardSearchText.peek());
//					searchText = forwardSearchText.pop();
//				}
//				else {
//					System.out.println("in goForwardView : recentSearchText Peek : " + recentSearchText.peek());
//					searchText = recentSearchText.pop();	
//				}
//				((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText(searchText);
//			}
//			else {
//				((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController().searchTextField.setText("");
//			}
			selectView(recentViews.peek());
		}
	}
	
	//Messages
	public void addMessage(Message msg) {
		DB_Messages.addMessage(messagesList, msg);
	}
	
	public void popupAccountInfo(ActionEvent event) {
		Parent root;
		try {
			currentPopupAccountInfo = new FXMLLoader(getClass().getResource(AdminController.fxmlpopupAccountInfo));
			root = AdminController.currentPopupAccountInfo.load();
			
			stage = new Stage();
			stage.setResizable(false);
			
				
			Scene createNewScene = new Scene(root);
			createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
			
			stage.setScene(createNewScene);
			stage.getIcons().add(Main.itAssetLogo);
			
			//make it as a dialog box
			Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parentStage);
			
			stage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
