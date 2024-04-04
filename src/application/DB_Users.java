package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Components.User;
import javafx.collections.ObservableList;

public class DB_Users extends DB_Utilities{
	
	//id
    private static int last_id = 0;
    
    public static void refresh(ObservableList<User> obsList) {
    	try (Connection con = DB_Utilities.getDataSource().getConnection())
		{
			String getAllUsersQuery = "SELECT * FROM users";
			try(PreparedStatement psUsers = con.prepareStatement(getAllUsersQuery)){
				try (ResultSet userResultSet = psUsers.executeQuery();){
					//Users:
					while(userResultSet.next()) {//while the reader still has a next row read it:
						int user_id = userResultSet.getInt("user_id");
						String username = userResultSet.getString("username");
						String pass_word = userResultSet.getString("pass_word");
						String email = userResultSet.getString("email");
						String full_name = userResultSet.getString("full_name");
						String user_role = userResultSet.getString("user_role");
						
						User newuser = new User(user_id,username,pass_word,email,full_name,user_role);
						obsList.add(newuser);
					}
				}    
			}
		} catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
    }
	
	public static void addUser(ObservableList<User> obsList, User user) {
		try(Connection con = dataSource.getConnection()){
			String insertUser = "INSERT INTO users (username,pass_word,email,full_name,user_role) VALUES(?,?,?,?,?);";
			try(PreparedStatement ps = con.prepareStatement(insertUser,Statement.RETURN_GENERATED_KEYS);){
				
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPass_word());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getFull_name());
				ps.setString(5, user.getUser_role());
				ps.executeUpdate();
				
				
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			        	 //set the actual user id:
			             last_id = generatedKeys.getInt(1);
			             user.setUser_id(last_id);
				obsList.add(user);
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void removeUser(ObservableList<User> obsList, ObservableList<User> selectedUsers) {
		try (Connection con = dataSource.getConnection()) {
			String deleteUser = "DELETE FROM users WHERE user_id=?";
			try(PreparedStatement ps = con.prepareStatement(deleteUser)){
				for(User user : selectedUsers) {
					ps.setInt(1, user.getUser_id());
					ps.executeUpdate();
					
					obsList.remove(user);
				}
				
				//log out all deleted users .//to be implemented later.
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
}
