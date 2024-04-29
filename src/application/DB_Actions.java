package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Action;
import javafx.collections.ObservableList;


public class DB_Actions {
	
	public static int last_id=0;
	public static final int MAX_ACTIONS = 30;
	
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
							String action_info = rs.getString("action_info");
							String cor_obj_type = rs.getString("cor_obj_type");
							
							action = new Action(action_id,action_type,action_info,cor_obj_type,date);
							
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
				String insertMessage = "INSERT INTO actions (action_type,action_info,cor_obj_type,action_date) VALUES (?,?,?,?)";
				try(PreparedStatement ps = con.prepareStatement(insertMessage,Statement.RETURN_GENERATED_KEYS)){
					
					ps.setString(1, action.getAction_type());
					ps.setString(2, action.getAction_info());
					
					if(action.getCor_obj_type() == Helper.ASSET) {
						ps.setString(3, Helper.ASSET);
					}else if(action.getCor_obj_type() == Helper.USER) {
						ps.setString(3, Helper.USER);
					}else if(action.getCor_obj_type() == Helper.ROOM) {
						ps.setString(3, Helper.ROOM);
					}
					
					ps.setDate(4, action.getAction_date());
					
					ps.executeUpdate();
					
					try(ResultSet generatedKeys = ps.getGeneratedKeys()){
				         if(generatedKeys.next()) {
				            last_id = generatedKeys.getInt(1);
				            action.setAction_id(last_id);				            
				            obsList.add(action);
				            if(obsList.size() > MAX_ACTIONS) {
				            	deleteAction(obsList);
				            	obsList.remove(action);
				            }
				         } else {
				        	 Helper.displayErrorMessage("Error","Failed to retrieve last inserted ID.");
				         }
				    }
					
					
				}
			}catch(SQLException e) {
				Helper.displayErrorMessage("Error",e.getMessage());
			}
	 }
	 
	 public static void deleteAction(ObservableList<Action> obsList) {
		 try (Connection con = DB_Utilities.getDataSource().getConnection()){
			 String sql = "DELETE FROM actions WHERE action_id NOT IN (SELECT action_id FROM (SELECT action_id FROM actions ORDER BY action_date DESC LIMIT ?) AS tmp)";
				 try(PreparedStatement stmt = con.prepareStatement(sql)) {
			            stmt.setInt(1, MAX_ACTIONS);
			            stmt.executeUpdate();
			 }
		 } catch (SQLException e) {
			 
		}
	 }
}
