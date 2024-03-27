package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javafx.util.Duration;
import java.util.ResourceBundle;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import LoginUi.LoginController;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

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
	@FXML
	private MenuButton createNewMenuBox;
	//****************************
	
	//constant for easy manipulation on code:
	private final String asset = "Asset";
	private final String user = "User";
	//choosing user or asset for creation:*****
	private MenuItem[] newOptions = {new MenuItem(asset),new MenuItem(user)};
	
	
	//*****************************************
	
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
		createNewMenuBox.getItems().clear();
		for(MenuItem item : newOptions){
			item.setOnAction(event->createNew(event));
		}
		createNewMenuBox.getItems().addAll(newOptions);
	}
	
	public void createNew(ActionEvent event){
		
		Parent root = null;
		fillFormula = new Stage();
		MenuItem source = (MenuItem)event.getSource();
		if(source.getText() == "Asset"){
			fillFormula.setTitle("Create New Asset:");
			try {
				root = FXMLLoader.load(getClass().getResource("/AdminUi/assetScene.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			AssetController.setStage(fillFormula);
		}else if(source.getText() == "User") {
			fillFormula.setTitle("Create New User:");
			try {
				root = FXMLLoader.load(getClass().getResource("/AdminUi/userScene.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			UserController.setStage(fillFormula);
		}
		
		createNewScene = new Scene(root);
		createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
		
		fillFormula.setScene(createNewScene);
		fillFormula.getIcons().add(Main.itAssetLogo);
		fillFormula.show();
		
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
	
	
	
	public void LogOut(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource(LoginController.fxmlLogin));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		loginScene = new Scene(root);
		stage.setScene(loginScene);
		stage.setTitle("Stockify - Login");
		
		stage.show();
	}

	
}
