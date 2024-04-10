package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Components.Room;
import javafx.collections.ObservableList;

public class DB_Rooms extends DB_Utilities{
	
	//id
    public static int last_id = 0;
    
    public static void refresh(ObservableList<Room> obsList) {
    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllRoomsQuery = "SELECT * FROM rooms";
			try(PreparedStatement psAssets = con.prepareStatement(getAllRoomsQuery)){
				try(ResultSet rs = psAssets.executeQuery()){
					while(rs.next()) {//while the reader still has a next row read it:
						int room_id = rs.getInt("room_id");
						String room_name = rs.getString("room_name");
						String room_type = rs.getString("room_type");
						
						Room room = new Room(room_id,room_type,room_name);
						obsList.add(room);
					}
				}
			}
		}catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}

    }

	public static void addRoom(ObservableList<Room> obsList, Room room) {
		try(Connection con = dataSource.getConnection()){
			String insertRoom = "INSERT INTO rooms (room_name, room_type) VALUES(?,?);";
			try(PreparedStatement ps = con.prepareStatement(insertRoom,Statement.RETURN_GENERATED_KEYS);){
				
				ps.setString(1, room.getRoom_name());
				ps.setString(2, room.getRoom_type());
				ps.executeUpdate();
				
				
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			        	//set the actual user id:
			        	 last_id = generatedKeys.getInt(1);
			             room.setRoom_id(last_id);
			             obsList.add(room);
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }
				
				
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void removeRoom(ObservableList<Room> obsList, ObservableList<Room> selectedRooms) {
		try (Connection con = dataSource.getConnection()) {
			String deleteRoom = "DELETE FROM rooms WHERE room_id=?";
			try(PreparedStatement ps = con.prepareStatement(deleteRoom)){
				for(Room room : selectedRooms) {
					ps.setInt(1, room.getRoom_id());
					ps.executeUpdate();
					
					obsList.remove(room);
				}
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	
	public static void updateRoom(ObservableList<Room> obsList, Room oldRoom, Room newRoom) {
		try(Connection con = dataSource.getConnection()){
			String updateRoom = "UPDATE rooms SET "
					+ "room_name = ?,"
					+ "room_type = ? "
					+ "WHERE room_id = ?";
			try(PreparedStatement ps = con.prepareStatement(updateRoom)){
				
//				ps.setInt(1, 0);
				ps.setString(1,newRoom.getRoom_name());
				ps.setString(2,newRoom.getRoom_type());
				ps.setInt(3,oldRoom.getRoom_id());
				ps.executeUpdate();
				
				
				
				oldRoom.setRoom_name(newRoom.getRoom_name());
				oldRoom.setRoom_type(newRoom.getRoom_type());
				
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	
	 public static ArrayList<String> getRooms() {
		 ArrayList<String> rooms = new ArrayList<String>();
		 try (Connection con = dataSource.getConnection()){
			 String getRoomNamesQuery = "SELECT * FROM rooms";
			 try(PreparedStatement ps = con.prepareStatement(getRoomNamesQuery)){
				 try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
						rooms.add(rs.getString("room_name"));
					}
				 }
			 }
		 }catch(SQLException e) {
			 Helper.displayErrorMessage("Error",e.getMessage());
		 }
		 
		 return rooms;
	 }
}
