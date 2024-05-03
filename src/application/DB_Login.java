package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Components.User;

public class DB_Login extends DB_Utilities{
	
	public static User getAssociatedUser(String email) {
		email.trim();
		User user = null;
		try (Connection con = getDataSource().getConnection()){
			try(PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE email=?")){
				stmt.setString(1, email);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						int user_id = rs.getInt("user_id");
						String username = rs.getString("username");
						String pass_word = rs.getString("pass_word");
						String first_name = rs.getString("first_name");
						String last_name = rs.getString("last_name");
						String user_role = rs.getString("user_role");
						String user_salt = rs.getString("user_salt");
						
						user = new User(user_id,username,pass_word,email,first_name,last_name,user_role,user_salt);
					}
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}
