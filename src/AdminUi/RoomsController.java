package AdminUi;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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
  	@FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchCriteriaComboBox;
    
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
						String room_type = rs.getString("room_type");
						String room_name = rs.getString("room_name");
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
        
		
        filteredRooms = new FilteredList<Room>(allRooms);
        filterTableView();
        
        sortedRooms = new SortedList<Room>(filteredRooms);
        sortedRooms.comparatorProperty().bind(roomsTable.comparatorProperty());
        
        roomsTable.setItems(sortedRooms);
        
        // Initialize search criteria ComboBox
        searchCriteriaComboBox.getItems().addAll("Name", "Type");
        searchCriteriaComboBox.setValue("Name");
	}
	
	public void popupNewRoom(ActionEvent event) {
		
	}
	
    public void deleteSelectedRooms() {
    	
    	ObservableList<Room> selectedRooms = roomsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 
    		for(Room item : selectedRooms) {
    			DatabaseUtilities.deleteItemFromDatabase(item);
    		}
    		
    		allRooms.removeAll(selectedRooms);
    	}
    }
	
	public void addRoom(Room room) {
		DatabaseUtilities.insertItemIntoDatabase(room);
		room.setRoom_id(last_id);
		allRooms.add(room);
	}
	
	
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
