package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Session;
import Components.User;
import LoginUi.LoginController;
import javafx.collections.ObservableList;

public class DB_Sessions extends DB_Utilities{
	
	public static void createSession(int logged_user_id) {
		try(Connection con = dataSource.getConnection()){
			String insertRoom = "INSERT INTO sessions (logged_user_id) VALUES(?);";
			try(PreparedStatement ps = con.prepareStatement(insertRoom,Statement.RETURN_GENERATED_KEYS);){
				ps.setInt(1, logged_user_id);
				ps.executeUpdate();
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static boolean sessionExists(int logged_user_id) {
		try(Connection con = dataSource.getConnection()){
			String insertRoom = "SELECT * FROM sessions WHERE logged_user_id=?;";
			try(PreparedStatement ps = con.prepareStatement(insertRoom,Statement.RETURN_GENERATED_KEYS);){
				ps.setInt(1, logged_user_id);
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()) {
						LoginController.setSession(new Session(logged_user_id,rs.getString("user_session_UUID")));
						return true;
					}
				}
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		return false;
	}
	
	public static void terminateCurrentSession(int logged_user_id) {
		try(Connection con = dataSource.getConnection()){
			String insertRoom = "DELETE FROM sessions WHERE logged_user_id=?;";
			try(PreparedStatement ps = con.prepareStatement(insertRoom,Statement.RETURN_GENERATED_KEYS);){
				ps.setInt(1, logged_user_id);
				ps.executeUpdate();
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	
}
