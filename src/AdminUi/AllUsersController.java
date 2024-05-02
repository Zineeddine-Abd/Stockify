package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import Components.User;
import application.DB_Sessions;
import application.DB_Users;
import application.Helper;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;
    
    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> user_roleColumn;
    
    @FXML
    private TableColumn<User, String> editColumn;
    
	//Top
    @FXML
	private Button newUserButton;
    
    //creating new asset/user mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	public boolean confirmedPassword = false;
  	//*************************************************
  	@FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    private final String[] criteria = {"Id","Name", "Email", "First name", "Last name", "Role"};
  	
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
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("first_name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("last_name"));
        user_roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("user_role"));
        
        editColumn.setReorderable(false);
		editColumn.setCellFactory((col)->{
			TableCell<User, String> cell = new TableCell<User, String>(){
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if(empty) {
						setGraphic(null);
					}else {
						FontAwesomeIconView edit = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
						edit.setGlyphSize(18);
						edit.setCursor(Cursor.HAND);
						
						edit.hoverProperty().addListener((obs, oldVal, newVal) -> {
								if(newVal) {
									edit.setFill(Color.GREEN);
								}else {
									edit.setFill(Color.BLACK);
								}
						});
				
						edit.setOnMouseClicked(event -> popupUpdateUser(event, this.getTableRow().getItem()));
						setGraphic(edit);
					}
				}
			};
			return cell;
		});
		
		filteredUsers = new FilteredList<User>(allUsersObs);
        filterTableView();
        
        sortedUsers = new SortedList<User>(filteredUsers);
        sortedUsers.comparatorProperty().bind(usersTable.comparatorProperty());
        
        usersTable.setItems(sortedUsers);
         
        // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll(criteria);
        searchCriteriaComboBox.setValue(criteria[0]);
	}
	
	public void initUsers() {
		
	}
	
	public void popupNewUser(ActionEvent event) {
		Parent root;
		try {
			AdminController.currentNewUserLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewUser));
			root = AdminController.currentNewUserLoader.load();
			NewUserController controller = (NewUserController)AdminController.currentNewUserLoader.getController();
			controller.setOldUser(null);
			controller.setTitleLabelText("New User");
			
			
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
	
	
	public void popupUpdateUser(MouseEvent event, User user) {
		Parent root;
		
		popupConfirmPassword(event, ConfirmPasswordController.UPDATE_MODE);
		
		if(!confirmedPassword) {
			return;
		}
		confirmedPassword = false;
		try {
			AdminController.currentNewUserLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewUser));
			root = AdminController.currentNewUserLoader.load();
			NewUserController controller = (NewUserController)AdminController.currentNewUserLoader.getController();
			controller.setOldUser(user);
			controller.setTitleLabelText("Update User");
			controller.setInfos();
			
			
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
	
	
	public void popupConfirmPassword(MouseEvent event,int mode) {
		Parent root;
		try {
			AdminController.currentConfirmPasswordLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlConfirmPassword));
			root = AdminController.currentConfirmPasswordLoader.load();
			ConfirmPasswordController controller = (ConfirmPasswordController)AdminController.currentConfirmPasswordLoader.getController();
			controller.setCurrentMode(mode);
			
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
			
			fillFormula.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(User newuser) {
       DB_Users.addUser(allUsersObs, newuser);
	}
	
    public void deleteSelectedUsers(MouseEvent event) {
    	
    	List<User> selectedUsers = usersTable.getSelectionModel().getSelectedItems().stream().toList();
    	
    	if(selectedUsers.isEmpty()) {
    		Helper.displayErrorMessage("Error", "You need to select a user first.");
    		return;
    	}
    	
    	popupConfirmPassword(event, ConfirmPasswordController.DELETION_MODE);
		
		if(!confirmedPassword) {
			return;
		}
		confirmedPassword = false;
		
    	
    	if(SelectedUsersAreLoggedIn(selectedUsers)) {
    		Helper.displayErrorMessage("Error", "One or more of the selected users is currently logged in! deletion cancelled.");
    		return;
    	}
    	
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete user(s)?","This action cannot be undone and any logged in users deleted will be automatically logged out")) {
    		DB_Users.removeUser(allUsersObs, selectedUsers);
    	}
    	
    	usersTable.getSelectionModel().clearSelection();
    }
    
    public void updateUser(User oldUser, User newUser) {
		DB_Users.updateUser(allUsersObs, oldUser, newUser);
		usersTable.refresh();
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
		        case "Email":
		        	if(user.getEmail().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "First name":
		        	if(user.getFirst_name().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Role":
		        	if(user.getUser_role().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Last name":
		        	if(user.getLast_name().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Id":
		        	if(String.valueOf(user.getUser_id()).contains(txt.toLowerCase())) {
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
    
    public void refreshUsersList() {
    	allUsersObs.clear();
    	DB_Users.refresh(allUsersObs);
    	
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean SelectedUsersAreLoggedIn(List<User> selectedUsers) {
    	for(User user: selectedUsers) {
    		if(DB_Sessions.sessionExists(user.getUser_id())) {
    			return true;
    		}
    	}
    	
    	return false;
    }
}
