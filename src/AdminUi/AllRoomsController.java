package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Components.Room;
import application.DB_Rooms;
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
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllRoomsController implements Initializable{
	@FXML
	private TableView<Room> roomsTable;
	@FXML
	private TableColumn<Room,Integer> roomIdColumn;
	@FXML
	private TableColumn<Room,String> roomTypeColumn;
	@FXML
	private TableColumn<Room,String> roomNameColumn;
	@FXML
	private TableColumn<Room,String> editColumn;
	
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
   // private Room lastSelectedRoom = new Room(-1,null,null); // dummy values just to set the first selected item.
    
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		allRooms = FXCollections.observableArrayList();
		DB_Rooms.refresh(allRooms);
			
		roomsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roomIdColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("room_id"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("room_type"));
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<Room, String>("room_name"));
        editColumn.setCellFactory((col)->{
			TableCell<Room, String> cell = new TableCell<Room, String>(){
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
				
						edit.setOnMouseClicked(event -> popupUpdateRoom(event, this.getTableRow().getItem()));
						
						
						
						FontAwesomeIconView assets = new FontAwesomeIconView(FontAwesomeIcon.DESKTOP);
						assets.setGlyphSize(18);
						assets.setCursor(Cursor.HAND);
						assets.hoverProperty().addListener((obs, oldVal, newVal) -> {
							if(newVal) {
								assets.setFill(Color.BLUE);
							}else {
								assets.setFill(Color.BLACK);
							}
						});
						assets.setOnMouseClicked(event -> {     		
				        	AllAssetsController currentAssetController = ((AdminController)Helper.currentAdminLoader.getController()).getAllAssetsViewController();
				        	currentAssetController.getSearchTextField().setText("" + this.getTableRow().getItem().getRoom_name());
				        	currentAssetController.getSearchCriteriaComboBox().setValue("Location");
				        	((AdminController)Helper.currentAdminLoader.getController()).triggerAssetPane();
				        	roomsTable.getSelectionModel().clearSelection();
						});
						
						HBox box = new HBox(edit, assets);
						HBox.setMargin(edit, new Insets(2, 2, 0, 3));
						HBox.setMargin(assets, new Insets(2, 2, 0, 3));
						
						setGraphic(box);
					}
				}
			};
			return cell;
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
	public void initRooms() {
		
	}
	
	
	
	public void popupNewRoom(ActionEvent event) {
		Parent root;
		
		try {
			AdminController.currentNewRoomLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewRoom));
			
			root = AdminController.currentNewRoomLoader.load();
			NewRoomController controller = (NewRoomController)AdminController.currentNewRoomLoader.getController();
			controller.setOldRoom(null);
			controller.setTitleLabelText("New Room");
			
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
	
	
	public void popupUpdateRoom(MouseEvent event, Room room) {
		Parent root;
		
		try {
			AdminController.currentNewRoomLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewRoom));
			
			root = AdminController.currentNewRoomLoader.load();
			NewRoomController controller = (NewRoomController)AdminController.currentNewRoomLoader.getController();
			controller.setOldRoom(room);
			controller.setTitleLabelText("Update Room");
			controller.setInfos();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Update Room:");
			
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
		DB_Rooms.addRoom(allRooms, newRoom);
	}
	
    public void deleteSelectedRooms() {
    	
    	ObservableList<Room> selectedRooms = roomsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 
    		DB_Rooms.removeRoom(allRooms, selectedRooms);
    	}
    }
    
    public void updateRoom(Room oldRoom, Room newRoom) {
		DB_Rooms.updateRoom(allRooms, oldRoom, newRoom);
		roomsTable.refresh();
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
	
	public void refreshRoomsList() {
		allRooms.clear();
    	DB_Rooms.refresh(allRooms);
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
