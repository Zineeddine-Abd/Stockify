package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import AdminUi.AllAssetsController;
import AdminUi.AllUsersController;
import Components.Asset;
import Components.User;
import LoginUi.LoginController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatabaseUtilities {
	
	static Connection con;
	
	public static void insertItemIntoDatabase(Object item) {
		PreparedStatement ps = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(LoginController.url);
			
			
			if(item instanceof Asset) {
				Asset asset = (Asset)item;
				String insertAsset = "INSERT INTO assets (asset_category,asset_type,asset_model,asset_status,asset_location,asset_purchase_date,asset_warranty,asset_serial_number) VALUES (?,?,?,?,?,?,?,?)";
				ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS);
//				ps.setInt(1, 0);
				ps.setString(1,asset.getAsset_category());
				ps.setString(2,asset.getAsset_type());
				ps.setString(3,asset.getAsset_model());
				ps.setString(4,asset.getAsset_status());
				ps.setString(5,asset.getAsset_location());
				ps.setDate(6,asset.getAsset_purchase_date());
				ps.setInt(7,asset.getAsset_warranty());
				ps.setInt(8,asset.getAsset_serial_number());
				ps.executeUpdate();
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			             int lastInsertedId = generatedKeys.getInt(1);
			             //set the actual asset id:
			             AllAssetsController.last_id = lastInsertedId;
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }
				
			}else if(item instanceof User) {
				User user = (User)item;
				String insertUser = "INSERT INTO users (username,pass_word,email,full_name,user_role) VALUES(?,?,?,?,?);";
				ps = con.prepareStatement(insertUser,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPass_word());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getFull_name());
				ps.setString(5, user.getUser_role());
				ps.executeUpdate();
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			             int lastInsertedId = generatedKeys.getInt(1);
			             //set the actual user id:
			             AllUsersController.last_id = lastInsertedId;
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closePreparedStatement(ps);
			closeConnnection(con);
		}
	}
	
	
	
	
	
	
	
	public static void deleteItemFromDatabase(Object target) {
		PreparedStatement ps = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(LoginController.url);
			
			if(target instanceof Asset) {
				Asset asset = (Asset)target;
				String deleteAsset = "DELETE FROM assets WHERE asset_id=?";
				ps = con.prepareStatement(deleteAsset);
				ps.setInt(1, asset.getAsset_id());
				ps.executeUpdate();
				//reset the sequence if data:
				if(isTableEmpty("assets")) {					
					resetSequenceTo1();
				}
			}else if(target instanceof User) {
				User user = (User)target;
				String deleteUser = "DELETE FROM users WHERE user_id=?";
				ps = con.prepareStatement(deleteUser);
				ps.setInt(1, user.getUser_id());
				ps.executeUpdate();
				
				//log out all deleted users .
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closePreparedStatement(ps);
			closeConnnection(con);
		}
	}
	
	private static void resetSequenceTo1() {
		Statement stmt = null;
        try {
        	stmt = con.createStatement();
        	String sequenceName = "public.assets_asset_id_seq";
        	String alterSequenceQuery = "ALTER SEQUENCE " + sequenceName + " RESTART WITH 1";
			stmt.executeUpdate(alterSequenceQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeStatement(stmt);
		}
	}
	
	private static boolean isTableEmpty(String table) {
		try (Connection con = DriverManager.getConnection(LoginController.url);
			    Statement stmt = con.createStatement()) {
			    String countQuery = "SELECT COUNT(*) FROM " + table;
			    try (ResultSet rs = stmt.executeQuery(countQuery)) {
			        rs.next();
			        int rowCount = rs.getInt(1);
			        if (rowCount == 0) {
			        	return true;
			        } else {
			            return false;
			        }
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}finally {
				//closeConnnection(con);
			}
		return false;
	}
	
	
	public static void closeConnnection(Connection connection) {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				displaySQLErrorMessage("Error", e.getMessage());
			}
		}
	}
	
	public static void closePreparedStatement(PreparedStatement preparedStatement) {
		if(preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				displaySQLErrorMessage("Error", e.getMessage());
			}
		}
	}
	
	public static void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				displaySQLErrorMessage("Error", e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet resultSet) {
		if(resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				displaySQLErrorMessage("Error", e.getMessage());
			}
		}
	}
	
	
	 private static void displaySQLErrorMessage(String title, String message) {
		 Alert alert = new Alert(AlertType.ERROR);
		 alert.setTitle(title);
		 alert.setHeaderText(null);
		 alert.setContentText(message);
	     alert.showAndWait();
	 }
}
