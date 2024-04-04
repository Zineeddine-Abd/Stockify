package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import Components.User;
import application.DB_Users;
import application.DB_Utilities;
import application.Helper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	
    
    
    //creating new asset/user mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
  	@FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    private final String[] criteria = {"Name", "Password", "Email", "Full name", "Role"};
  	
  	//observable lists***************
  	ObservableList<User> allUsersObs;
    FilteredList<User> filteredUsers;
    SortedList<User> sortedUsers;
    //********************************
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allUsersObs = FXCollections.observableArrayList();
		
		
		DB_Users.refresh(allUsersObs);
		
		  //Users table:
        usersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        user_idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("user_id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        pass_wordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("pass_word"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        full_nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("full_name"));
        user_roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("user_role"));
        
        
        filteredUsers = new FilteredList<User>(allUsersObs);
        filterTableView();
        
        sortedUsers = new SortedList<User>(filteredUsers);
        sortedUsers.comparatorProperty().bind(usersTable.comparatorProperty());
        
        usersTable.setItems(sortedUsers);
        
        // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll(criteria);
        searchCriteriaComboBox.setValue(criteria[0]);
	}
	
	public void createNewUser(ActionEvent event) {
		Parent root;
		try {
			
			
			AdminController.currentNewUserLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewUser));
			root = AdminController.currentNewUserLoader.load();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
				
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
       DB_Users.addUser(allUsersObs, newuser);
	}
	
	  //Method to delete selected users from usersTable
    public void deleteSelectedUsers() {
    	ObservableList<User> selectedUsers = usersTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete user(s)?","This action cannot be undone and any logged in users deleted will be automatically logged out")) {
    		DB_Users.removeUser(allUsersObs, selectedUsers);
    	}
    }

    
    
    //Filtering methods******************************************************
    private void filterTableView() {
		searchTextField.textProperty().addListener((obs, oldTxt, newTxt)->{
			setFilterPredicate(newTxt);
		});
		searchCriteriaComboBox.valueProperty().addListener((obs, oldVal, newVal)->{
			setFilterPredicate(searchTextField.getText());
		});
	}

    private void setFilterPredicate(String txt) {
    	filteredUsers.setPredicate((user)-> {
			if(txt == null || txt.isBlank()) {
				return true;
			}else {
				switch (searchCriteriaComboBox.getValue()) {
				case "Name":
					if(user.getUsername().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Password":
		        	if(user.getPass_word().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Email":
		        	if(user.getEmail().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Full name":
		        	if(user.getFull_name().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Role":
		        	if(user.getUser_role().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		            
		        default:
		        	return false;
				}
				return false;
			}
		});
    }
}
