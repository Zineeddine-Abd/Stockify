package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import AdminUi.AdminController;
import Components.Action;
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
						String first_name = userResultSet.getString("first_name");
						String last_name = userResultSet.getString("last_name");
						String user_role = userResultSet.getString("user_role");
						String user_salt = userResultSet.getString("user_salt");
						
						User newuser = new User(user_id,username,pass_word,email,first_name,last_name,user_role,user_salt);
						obsList.add(newuser);
					}
				}    
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
    }
	
	public static void addUser(ObservableList<User> obsList, User user) {
		try(Connection con = dataSource.getConnection()){
			String insertUser = "INSERT INTO users (username,pass_word,email,first_name,last_name,user_role,user_salt) VALUES(?,?,?,?,?,?,?);";
			try(PreparedStatement ps = con.prepareStatement(insertUser,Statement.RETURN_GENERATED_KEYS);){
				
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPass_word());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getFirst_name());
				ps.setString(5, user.getLast_name());
				ps.setString(6, user.getUser_role());
				ps.setString(7, user.getUser_salt());
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
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		createActionForUser(Helper.INSERTION_MODE, user);
	}
	
	public static void removeUser(ObservableList<User> obsList, List<User> selectedUsers) {
		for(User user : selectedUsers) {
			createActionForUser(Helper.DELETION_MODE, user);
		}
		
		try (Connection con = dataSource.getConnection()) {
			String deleteUser = "DELETE FROM users WHERE user_id=?";
			try(PreparedStatement ps = con.prepareStatement(deleteUser)){
				for(User user : selectedUsers) {
					ps.setInt(1, user.getUser_id());
					ps.executeUpdate();
					
					obsList.remove(user);
					
					if(isTableEmpty("users")) {					
						resetSequenceTo1(con, "public.users_user_id_seq");
					}
				}
				//log out all deleted users .//to be implemented later.
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		
	}
	
	public static void updateUser(ObservableList<User> obsList, User oldUser, User newUser) {
		try(Connection con = dataSource.getConnection()){
			String updateUser = "UPDATE users SET "
					+ "username = ?,"
					+ "pass_word = ?,"
					+ "email = ?,"
					+ "first_name = ?,"
					+ "last_name = ?,"
					+ "user_role = ? ,"
					+ "user_salt = ? "
					+ "WHERE user_id = ?";
			try(PreparedStatement ps = con.prepareStatement(updateUser)){
				
				ps.setString(1,newUser.getUsername());
				ps.setString(2,newUser.getPass_word());
				ps.setString(3,newUser.getEmail());
				ps.setString(4,newUser.getFirst_name());
				ps.setString(5, newUser.getLast_name());
				ps.setString(6,newUser.getUser_role());
				ps.setString(7, newUser.getUser_salt());
				ps.setInt(8,oldUser.getUser_id());
				ps.executeUpdate();
				
				
				
				oldUser.setUsername(newUser.getUsername());
				oldUser.setPass_word(newUser.getPass_word());
				oldUser.setEmail(newUser.getEmail());
				oldUser.setFirst_name(newUser.getFirst_name());
				oldUser.setLast_name(newUser.getLast_name());
				oldUser.setUser_role(newUser.getUser_role());
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		

		createActionForUser(Helper.UPDATE_MODE, oldUser);
	}
	
	
	public static User getUser(int id) {
		try (Connection con = DB_Utilities.getDataSource().getConnection())
		{
			String getAllUsersQuery = "SELECT * FROM users WHERE user_id=?";
			try(PreparedStatement psUsers = con.prepareStatement(getAllUsersQuery)){
				psUsers.setInt(1, id);
				try (ResultSet userResultSet = psUsers.executeQuery();){
					//Users:
					if(userResultSet.next()) {//while the reader still has a next row read it:
						int user_id = userResultSet.getInt("user_id");
						String username = userResultSet.getString("username");
						String pass_word = userResultSet.getString("pass_word");
						String email = userResultSet.getString("email");
						String first_name = userResultSet.getString("first_name");
						String last_name = userResultSet.getString("last_name");
						String user_role = userResultSet.getString("user_role");
						String user_salt = userResultSet.getString("user_salt");
						
						User newuser = new User(user_id,username,pass_word,email,first_name,last_name,user_role,user_salt);
						
						return newuser;
					}
				}    
			}
		} catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void createActionForUser(String action_type,User user) {
		Action action = new Action(0, action_type, user.toString() , Helper.USER, java.sql.Date.valueOf(LocalDate.now()));
		ObservableList<Action> recentActions = ((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().getrecentActionsObsList();
		DB_Actions.createAction(action,recentActions);
 }
}
