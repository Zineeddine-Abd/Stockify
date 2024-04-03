package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Components.Asset;
import Components.Room;
import application.DatabaseUtilities;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RoomsController implements Initializable{
	@FXML
	private TableView<Room> roomsTable;
	@FXML
	private TableColumn<Room,Integer> roomIdColumn;
	@FXML
	private TableColumn<Room,String> roomTypeColumn;
	@FXML
	private TableColumn<Room,String> roomNameColumn;
	
	//********Observable Lists:***************
    ObservableList<Room> allRooms;
    FilteredList<Room> filteredRooms;
    SortedList<Room> sortedRooms;
	//****************************************
    
    //creating new room mats:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
  	
  	@FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    private String[] criteria = {"Name", "Type"};
    private Room lastSelectedRoom = new Room(-1,null,null); // dummy values just to set the first selected item.
	//id
    public static int last_id = 0;
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		allRooms = FXCollections.observableArrayList();
		
		try(Connection con = DatabaseUtilities.getDataSource().getConnection()){
			String getAllRoomsQuery = "SELECT * FROM rooms";
			try(PreparedStatement psAssets = con.prepareStatement(getAllRoomsQuery)){
				try(ResultSet rs = psAssets.executeQuery()){
					while(rs.next()) {//while the reader still has a next row read it:
						int room_id = rs.getInt("room_id");
						String room_name = rs.getString("room_name");
						String room_type = rs.getString("room_type");
						
						Room room = new Room(room_id,room_type,room_name);
						allRooms.add(room);
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		roomsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("room_id"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("room_type"));
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("room_name"));
        
        roomsTable.setOnMouseClicked(event->{
        	if(lastSelectedRoom == null || roomsTable.getSelectionModel().getSelectedItem() == null) {
        		return;
        	}
        	if(lastSelectedRoom.getRoom_id() == roomsTable.getSelectionModel().getSelectedItem().getRoom_id()) {        		
        		AllAssetsController currentAssetController = ((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController();
        		currentAssetController.getSearchTextField().setText("" + roomsTable.getSelectionModel().getSelectedItem().getRoom_id());
        		currentAssetController.getSearchCriteriaComboBox().setValue("Location");
        		((AdminController)Helper.currentAdminLoader.getController()).triggerAssetPane();
        		roomsTable.getSelectionModel().clearSelection();
        	}else {
        		lastSelectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        	}
        });
		
        filteredRooms = new FilteredList<Room>(allRooms);
        filterTableView();
        
        sortedRooms = new SortedList<Room>(filteredRooms);
        sortedRooms.comparatorProperty().bind(roomsTable.comparatorProperty());
        
        roomsTable.setItems(sortedRooms);
        
        // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll(criteria);
        searchCriteriaComboBox.setValue(criteria[0]);
	}
	
	public void popupNewRoom(ActionEvent event) {
		Parent root;
		
		try {
			AdminController.currentNewRoomLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewRoom));
			
			root = AdminController.currentNewRoomLoader.load();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Create New Room:");
			
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
	
	public void addRoom(Room newRoom) {
		try {
			DatabaseUtilities.insertItemIntoDatabase(newRoom);
			newRoom.setRoom_id(last_id);
			if(!Helper.exception_thrown) {				
				allRooms.add(newRoom);
			}else {
				//reset the flag to false.
				Helper.exception_thrown = false;
			}
		} catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		
	}
	
    public void deleteSelectedRooms() {
    	
    	ObservableList<Room> selectedRooms = roomsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 
    		for(Room item : selectedRooms) {
    			try{
    				DatabaseUtilities.deleteItemFromDatabase(item);
    				allRooms.remove(item);
    			}catch(SQLException e) {
    				Helper.displayErrorMessage("Error", e.getMessage());
    			}
    		}
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
		
		filteredRooms.setPredicate((room)-> {
			if(txt == null || txt.isBlank()) {
				return true;
			}else {
				switch (searchCriteriaComboBox.getValue()) {
		        case "Type":
		        	if((room).getRoom_type().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Name":
		        	if((room).getRoom_name().toLowerCase().contains(txt.toLowerCase())) {
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
