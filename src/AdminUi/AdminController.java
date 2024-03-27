package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import javafx.util.Duration;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import LoginUi.LoginController;
import application.Main;

public class AdminController implements Initializable{
	
	//sidebar mats:*****************
	@FXML
	private VBox sideBar;
	@FXML
	private BorderPane dashboard; // Main Border pane (holds all content here (dashboard for now))
	@FXML
	private Button collapser;
	//****************************
	
	//constant for easy manipulation on code:
	private final String asset = "Asset";
	private final String user = "User";
	//choosing user or asset for creation:*****
	private MenuItem[] newOptions = {new MenuItem(asset),new MenuItem(user)};
	
	@FXML
	private MenuButton createNewMenuBox;
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
	
	
	
	public void toggleSidebar(){
        TranslateTransition openSidebar = new TranslateTransition(Duration.millis(500), sideBar);
        openSidebar.setToX(0);
        
        TranslateTransition closeSidebar = new TranslateTransition(Duration.millis(500), sideBar);

        if (sideBar.getTranslateX() != 0) {
            openSidebar.play();
        } else {
            closeSidebar.setToX(-sideBar.getWidth());
            closeSidebar.play();
        }
        
        closeSidebar.setOnFinished(event->{
        	collapser.setText("");
        	collapser.setPrefWidth(22);
        	dashboard.setLeft(null);
        });
        
        collapser.setText("Menu");
        collapser.setPrefWidth(202);
        dashboard.setLeft(sideBar);
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
