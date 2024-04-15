package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import Components.Action;
import Components.Asset;
import Components.Message;
import Components.Room;
import Components.User;
import javafx.collections.ObservableList;


public class DB_Actions {
	
	public static int last_id=0;
	
	public static void refresh(ObservableList<Action> obsList) {
		 Action action;
	    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
				String getAllActionsQuery = "SELECT * FROM actions";
				try(PreparedStatement psActions = con.prepareStatement(getAllActionsQuery)){
					try(ResultSet rs = psActions.executeQuery()){
						while(rs.next()) {//while the reader still has a next row read it:
							int action_id = rs.getInt("action_id");
							String action_type = rs.getString("action_type");
							Date date = rs.getDate("action_date");
							int action_author = rs.getInt("action_author");
							
							//determine which object is specified in the action.
							int related_action_asset_id = rs.getInt("cor_asset");
							if(rs.wasNull()) {
								int related_action_user_id = rs.getInt("cor_user");
								if(rs.wasNull()) {
									int related_action_room_id = rs.getInt("cor_room");
									Room room = DB_Rooms.getRoom(related_action_room_id);
									action = new Action(action_id, related_action_room_id, action_type, date, room,action_author);
								}else {
									User user = DB_Users.getUser(related_action_user_id);
									action = new Action(action_id, related_action_user_id, action_type, date, user,action_author);
								}
							}else {
								Asset asset = DB_Assets.getAsset(related_action_asset_id);
								action = new Action(action_id, related_action_asset_id, action_type, date, asset,action_author);
							}
							obsList.add(action);
						}
					}
				}
			}catch (SQLException e) {
				e.printStackTrace();
				Helper.displayErrorMessage("Error",e.getMessage());
			}
	    }
	 
	 public static void createAction(Action action,ObservableList<Action> obsList) {
		 try(Connection con = DB_Utilities.getDataSource().getConnection()){
				String insertMessage = "INSERT INTO actions (action_type,cor_asset,cor_user,cor_room,action_date,action_author) VALUES (?,?,?,?,?,?)";
				try(PreparedStatement ps = con.prepareStatement(insertMessage,Statement.RETURN_GENERATED_KEYS)){
					
					ps.setString(1, action.getAction_type());
					if(action.getRelated_object() instanceof Asset) {
						ps.setInt(2, action.getAction_related_obj_id());
						ps.setNull(3, Types.INTEGER);
						ps.setNull(4, Types.INTEGER);
					}else if(action.getRelated_object() instanceof User) {
						ps.setNull(2, Types.INTEGER);
						ps.setInt(3, action.getAction_related_obj_id());
						ps.setNull(4, Types.INTEGER);
					}else if(action.getRelated_object() instanceof Room) {
						ps.setNull(2, Types.INTEGER);
						ps.setNull(3, Types.INTEGER);
						ps.setInt(4, action.getAction_related_obj_id());
					}
					ps.setDate(5, action.getAction_date());
					ps.setInt(6, action.getAction_author());
					
					ps.executeUpdate();
					
					try(ResultSet generatedKeys = ps.getGeneratedKeys()){
				         if(generatedKeys.next()) {
				            last_id = generatedKeys.getInt(1);
				            action.setAction_id(last_id);
							obsList.add(action);
				         } else {
				        	 Helper.displayErrorMessage("Error","Failed to retrieve last inserted ID.");
				         }
				    }
				}
			}catch(SQLException e) {
				Helper.displayErrorMessage("Error",e.getMessage());
			}
	 }
}
