package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import AdminUi.AdminController;
import Components.Action;
import Components.Asset;
import Components.Room;
import LoginUi.LoginController;
import javafx.collections.ObservableList;

public class DB_Assets extends DB_Utilities{

	//id
    private static int last_id = 0;
    
    public static void refresh(ObservableList<Asset> obsList) {
    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllAssetsQuery = "SELECT * FROM assets";
			try(PreparedStatement psAssets = con.prepareStatement(getAllAssetsQuery)){
				//obsList.clear();
				try(ResultSet rs = psAssets.executeQuery()){
					while(rs.next()) {//while the reader still has a next row read it:
						int asset_id = rs.getInt("asset_id");
						String asset_category = rs.getString("asset_category");
						String asset_type = rs.getString("asset_type");
						String asset_model = rs.getString("asset_model");
						String asset_status = rs.getString("asset_status");
						String asset_room = rs.getString("asset_room");
						Date asset_purchase_date = rs.getDate("asset_purchase_date");
						int asset_warranty = rs.getInt("asset_warranty");
						int asset_serial_number = rs.getInt("asset_serial_number");
						
						Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty,asset_serial_number);
						obsList.add(asset);
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
    }
	
	public static void addAsset(ObservableList<Asset> obsList, Asset asset) {
		try(Connection con = dataSource.getConnection()){
			String insertAsset = "INSERT INTO assets (asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty,asset_serial_number) VALUES (?,?,?,?,?,?,?,?)";
			try(PreparedStatement ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS)){
				
//				ps.setInt(1, 0);
				ps.setString(1,asset.getAsset_category());
				ps.setString(2,asset.getAsset_type());
				ps.setString(3,asset.getAsset_model());
				ps.setString(4,asset.getAsset_status());
				ps.setString(5,asset.getAsset_room());
				ps.setDate(6,asset.getAsset_purchase_date());
				ps.setInt(7,asset.getAsset_warranty());
				ps.setInt(8,asset.getAsset_serial_number());
				ps.executeUpdate();
				
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			             //set the actual asset id:
			            last_id = generatedKeys.getInt(1);
			            asset.setAsset_id(last_id);
						obsList.add(asset);
			            
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }				
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void removeAsset(ObservableList<Asset> obsList, ObservableList<Asset> selectedAssets) {
		try (Connection con = dataSource.getConnection()) {
				String deleteAsset = "DELETE FROM assets WHERE asset_id=?";
				try(PreparedStatement ps = con.prepareStatement(deleteAsset)){
					for(Asset asset : selectedAssets) {
						ps.setInt(1, asset.getAsset_id());
						ps.executeUpdate();
						
						obsList.remove(asset);
						//reset the sequence if data:
						if(isTableEmpty("assets")) {					
							resetSequenceTo1(con);
						}
					}
				}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void updateAsset(ObservableList<Asset> obsList, Asset oldAsset, Asset newAsset) {
		try(Connection con = dataSource.getConnection()){
			String updateAsset = "UPDATE assets SET "
					+ "asset_category = ?,"
					+ "asset_type = ?,"
					+ "asset_model = ?,"
					+ "asset_status = ?,"
					+ "asset_room = ?,"
					+ "asset_purchase_date = ?,"
					+ "asset_warranty = ?,"
					+ "asset_serial_number = ? "
					+ "WHERE asset_id = ?";
			try(PreparedStatement ps = con.prepareStatement(updateAsset)){
				
//				ps.setInt(1, 0);
				ps.setString(1,newAsset.getAsset_category());
				ps.setString(2,newAsset.getAsset_type());
				ps.setString(3,newAsset.getAsset_model());
				ps.setString(4,newAsset.getAsset_status());
				ps.setString(5,newAsset.getAsset_room());
				ps.setDate(6,newAsset.getAsset_purchase_date());
				ps.setInt(7,newAsset.getAsset_warranty());
				ps.setInt(8,newAsset.getAsset_serial_number());
				ps.setInt(9,oldAsset.getAsset_id());
				ps.executeUpdate();
				
				
				
				oldAsset.setAsset_category(newAsset.getAsset_category());
				oldAsset.setAsset_type(newAsset.getAsset_type());
				oldAsset.setAsset_model(newAsset.getAsset_model());
				oldAsset.setAsset_status(newAsset.getAsset_status());
				oldAsset.setAsset_room(newAsset.getAsset_room());
				oldAsset.setAsset_purchase_date(newAsset.getAsset_purchase_date());
				oldAsset.setAsset_warranty(newAsset.getAsset_warranty());
				oldAsset.setAsset_serial_number(newAsset.getAsset_serial_number());
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
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
	
	 public static Asset getAsset(int id) {
			try (Connection con = DB_Utilities.getDataSource().getConnection())
			{
				String getAllUsersQuery = "SELECT * FROM assets WHERE asset_id=?";
				try(PreparedStatement ps = con.prepareStatement(getAllUsersQuery)){
					ps.setInt(1, id);
					try (ResultSet rs = ps.executeQuery();){
						//Users:
						if(rs.next()) {//while the reader still has a next row read it:
							int asset_id = rs.getInt("asset_id");
							String asset_category = rs.getString("asset_category");
							String asset_type = rs.getString("asset_type");
							String asset_model = rs.getString("asset_model");
							String asset_status = rs.getString("asset_status");
							String asset_room = rs.getString("asset_room");
							Date asset_purchase_date = rs.getDate("asset_purchase_date");
							int asset_warranty = rs.getInt("asset_warranty");
							int asset_serial_number = rs.getInt("asset_serial_number");
							
							Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty,asset_serial_number);
							
							return asset;
						}
					}    
				}
			} catch (SQLException e) {
				Helper.displayErrorMessage("Error",e.getMessage());
			}
			
			return null;
		}
	 
	 private static void createActionForAsset(String action_type,Asset related_asset) {
		Action action = new Action(0,related_asset.getAsset_id(),action_type,java.sql.Date.valueOf(LocalDate.now()),related_asset,LoginController.getLoggedUser().getUser_id());
		ObservableList<Action> recentActions = ((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().getrecentActionsObsList();
		DB_Actions.createAction(action,recentActions);
	 }
	
}
