package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zaxxer.hikari.HikariDataSource;

import AdminUi.AllAssetsController;
import AdminUi.AllUsersController;
import Components.Asset;
import Components.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DatabaseUtilities {
	
	
	private static Properties properties;
	private static HikariDataSource dataSource;
	static {	
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/database.properties"));
			dataSource = new HikariDataSource();
			dataSource.setDriverClassName(properties.getProperty("driver.class.name"));
			dataSource.setJdbcUrl(properties.getProperty("db.url"));
			//tmp
			dataSource.setMinimumIdle(10);
			dataSource.setMaximumPoolSize(1000);
			
			dataSource.setLoginTimeout(5);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static HikariDataSource getDataSource() {
		return dataSource;
	}
	
	
	
	
	public static void insertItemIntoDatabase(Object item) {
		
		try (Connection con = dataSource.getConnection()){
			
			if(item instanceof Asset) {
				String insertAsset = "INSERT INTO assets (asset_category,asset_type,asset_model,asset_status,asset_location,asset_purchase_date,asset_warranty,asset_serial_number) VALUES (?,?,?,?,?,?,?,?)";
				try(PreparedStatement ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS)){
					Asset asset = (Asset)item;
					
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
				}
			}else if(item instanceof User) {
				String insertUser = "INSERT INTO users (username,pass_word,email,full_name,user_role) VALUES(?,?,?,?,?);";
				try(PreparedStatement ps = con.prepareStatement(insertUser,Statement.RETURN_GENERATED_KEYS);){
					User user = (User)item;
					
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public static void deleteItemFromDatabase(Object target) {
		
		try (Connection con = dataSource.getConnection()) {
			
			if(target instanceof Asset) {
				String deleteAsset = "DELETE FROM assets WHERE asset_id=?";
				try(PreparedStatement ps = con.prepareStatement(deleteAsset)){
					Asset asset = (Asset)target;
				
				
					ps.setInt(1, asset.getAsset_id());
					ps.executeUpdate();
					//reset the sequence if data:
					if(isTableEmpty("assets")) {					
						resetSequenceTo1(con);
					}
				}
				
			}else if(target instanceof User) {
				String deleteUser = "DELETE FROM users WHERE user_id=?";
				try(PreparedStatement ps = con.prepareStatement(deleteUser)){
					User user = (User)target;
				
					ps.setInt(1, user.getUser_id());
					ps.executeUpdate();
				
					//log out all deleted users .
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void resetSequenceTo1(Connection con) {
        try (Statement stmt = con.createStatement()){	
        	String sequenceName = "public.assets_asset_id_seq";
        	String alterSequenceQuery = "ALTER SEQUENCE " + sequenceName + " RESTART WITH 1";
			stmt.executeUpdate(alterSequenceQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean isTableEmpty(String table) {
		try (Connection con = dataSource.getConnection();
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
			}
		return false;
	}
	
	
	
	
	 private static void displaySQLErrorMessage(String title, String message) {
		 Alert alert = new Alert(AlertType.ERROR);
		 alert.setTitle(title);
		 alert.setHeaderText(null);
		 alert.setContentText(message);
	     alert.showAndWait();
	 }
}
