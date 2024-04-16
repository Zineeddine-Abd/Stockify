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
							int action_author_id = rs.getInt("action_author");
							
							int cor_obj_id = rs.getInt("cor_obj_id");
							String cor_obj_type = rs.getString("cor_obj_type");
							
							action = new Action(action_id,action_type,cor_obj_id,cor_obj_type,date,action_author_id);
							
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
				String insertMessage = "INSERT INTO actions (action_type,cor_obj_id,cor_obj_type,action_date,action_author) VALUES (?,?,?,?,?)";
				try(PreparedStatement ps = con.prepareStatement(insertMessage,Statement.RETURN_GENERATED_KEYS)){
					
					ps.setString(1, action.getAction_type());
					
					if(action.getRelated_object() instanceof Asset) {
						Asset asset = (Asset) action.getRelated_object();
						
						ps.setInt(2, asset.getAsset_id());
						ps.setString(3,Helper.ASSET);
						
					}else if(action.getRelated_object() instanceof User) {
						User user = (User) action.getRelated_object();
						
						ps.setInt(2, user.getUser_id());
						ps.setString(3,Helper.USER);
						
					}else if(action.getRelated_object() instanceof Room) {
						Room room = (Room) action.getRelated_object();
						
						ps.setInt(2, room.getRoom_id());
						ps.setString(3,Helper.ROOM);
						
					}
					
					ps.setDate(4, action.getAction_date());
					ps.setInt(5, action.getActionAuthor().getUser_id());
					
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
