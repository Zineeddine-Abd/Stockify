package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Components.Asset;
import Components.User;
import LoginUi.LoginController;

public class DatabaseUtilities {
	
	public static void insertItemIntoDatabase(Object item) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(LoginController.url);
			
			if(item instanceof Asset) {
				Asset asset = (Asset)item;
				String getAllAssetsQuery = "INSERT INTO assets (asset_id,asset_category,asset_type,asset_model,asset_status,asset_location,asset_purchase_date,asset_warranty,asset_serial_number) VALUES (?,?,?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(getAllAssetsQuery);
				ps.setInt(1, 0);
				ps.setString(2,asset.getAsset_category());
				ps.setString(3,asset.getAsset_type());
				ps.setString(4,asset.getAsset_model());
				ps.setString(5,asset.getAsset_status());
				ps.setString(6,asset.getAsset_location());
				ps.setDate(7,asset.getAsset_purchase_date());
				ps.setInt(8,asset.getAsset_warranty());
				ps.setInt(9,asset.getAsset_serial_number());

			}else if(item instanceof User) {
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public static void deleteItemFromDatabase(Object target) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(LoginController.url);
			
			if(target instanceof Asset) {
				Asset asset = (Asset)target;
				String getAllAssetsQuery = "DELETE FROM assets WHERE asset_id="+asset.getAsset_id();
				PreparedStatement ps = con.prepareStatement(getAllAssetsQuery);
				ps.executeUpdate();
			}else if(target instanceof User) {
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
