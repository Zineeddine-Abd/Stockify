package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Components.User;
import application.DatabaseUtilities;
import application.Helper;
import application.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllUsersController implements Initializable{

	 //Table_view users Components:******************************
    @FXML
    private TableView<User> usersTable;
    
    public TableView<User> getUsersTable(){
    	return usersTable;
    }

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
    
	//Top
    @FXML
	private Button newUserButton;
	
    
    //id
    public static int last_id = 0;
    
    //creating new asset/user mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		  //Users table:
        usersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        user_idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("user_id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        pass_wordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("pass_word"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        full_nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("full_name"));
        user_roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("user_role"));
		
	}
	
	public void createNewUser(ActionEvent event) {
		Parent root;
		try {
			
			
			AdminController.currentNewUserLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewUser));
			root = AdminController.currentNewUserLoader.load();
			
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
			
			//make it as a dialog box
			Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			fillFormula.initModality(Modality.WINDOW_MODAL);
			fillFormula.initOwner(parentStage);
			
			fillFormula.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addUser(User newuser) {
		DatabaseUtilities.insertItemIntoDatabase(newuser);
        newuser.setUser_id(last_id);
        usersTable.getItems().add(newuser);
	}
	
	  //Method to delete selected users from usersTable
    public void deleteSelectedUsers() {
    	ObservableList<User> selectedUsers = usersTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete user(s)?","This action cannot be undone and any logged in users deleted will be automatically logged out")) {
    		for(User item : selectedUsers) {
    			DatabaseUtilities.deleteItemFromDatabase(item);
    		}
    		
    		usersTable.getItems().removeAll(selectedUsers);
    	}
    }

    
    
    
}
