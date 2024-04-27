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
import Components.Hardware;
import Components.Software;
import javafx.collections.ObservableList;

public class DB_Assets extends DB_Utilities{
	//id
    private static int last_id = 0;
    
    public static void refresh(ObservableList<Asset> obsList) {
    	try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllAssetsQuery = "SELECT * FROM assets";
			try(PreparedStatement psAssets = con.prepareStatement(getAllAssetsQuery)){
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
						
						Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty);
						
						if(asset.getAsset_category().equals(Helper.HARDWARE)) {
							Hardware hardware = DB_Hardwares.getHardware(asset);
							obsList.add(hardware);
						}else if(asset.getAsset_category().equals(Helper.SOFTWARE)) {
							Software software = DB_Softwares.getSoftware(asset);
							obsList.add(software);
						}else {
							obsList.add(asset);
						}
						
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
			String insertAsset = "INSERT INTO assets (asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty) VALUES (?,?,?,?,?,?,?)";
			try(PreparedStatement ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS)){
				
				ps.setString(1,asset.getAsset_category());
				ps.setString(2,asset.getAsset_type());
				ps.setString(3,asset.getAsset_model());
				ps.setString(4,asset.getAsset_status());
				ps.setString(5,asset.getAsset_room());
				ps.setDate(6,asset.getAsset_purchase_date());
				ps.setInt(7,asset.getAsset_warranty());
				ps.executeUpdate();
				
				
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
			         if (generatedKeys.next()) {
			             //set the actual asset id:
			            last_id = generatedKeys.getInt(1);
			            asset.setAsset_id(last_id);
			            
			            if(asset instanceof Hardware) {
			            	DB_Hardwares.addHardware((Hardware)asset);
			            }else if(asset instanceof Software) {
			            	DB_Softwares.addSoftware((Software)asset);
			            }
			            
						obsList.add(asset);
			            
			         } else {
			             System.out.println("Failed to retrieve last inserted ID.");
			         }
			    }
				
				createActionForAsset(Helper.INSERTION_MODE, asset);
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void removeAsset(ObservableList<Asset> obsList, ObservableList<Asset> selectedAssets) {
		for(Asset asset : selectedAssets) {
			createActionForAsset(Helper.DELETION_MODE, asset);
		}
		
		try (Connection con = dataSource.getConnection()) {
				String deleteAsset = "DELETE FROM assets WHERE asset_id=?";
				try(PreparedStatement ps = con.prepareStatement(deleteAsset)){
					
					for(Asset asset : selectedAssets) {
						ps.setInt(1, asset.getAsset_id());
						ps.executeUpdate();
						
						obsList.remove(asset);
						//reset the sequence if data:
						if(isTableEmpty("assets")) {					
							resetSequenceTo1(con, "public.assets_asset_id_seq");
						}
					}
				}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		
	}
	
	public static void updateAsset(Asset oldAsset, Asset newAsset) {
		try(Connection con = dataSource.getConnection()){
			String updateAsset = "UPDATE assets SET "
					+ "asset_category = ?,"
					+ "asset_type = ?,"
					+ "asset_model = ?,"
					+ "asset_status = ?,"
					+ "asset_room = ?,"
					+ "asset_purchase_date = ?,"
					+ "asset_warranty = ? "
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
				ps.setInt(8,oldAsset.getAsset_id());
				ps.executeUpdate();
				
				
				oldAsset.setAsset_category(newAsset.getAsset_category());
				oldAsset.setAsset_type(newAsset.getAsset_type());
				oldAsset.setAsset_model(newAsset.getAsset_model());
				oldAsset.setAsset_status(newAsset.getAsset_status());
				oldAsset.setAsset_room(newAsset.getAsset_room());
				oldAsset.setAsset_purchase_date(newAsset.getAsset_purchase_date());
				oldAsset.setAsset_warranty(newAsset.getAsset_warranty());
				
				if(newAsset instanceof Hardware) {
	            	DB_Hardwares.updateHardware((Hardware)oldAsset, (Hardware)newAsset);
	            }else if(newAsset instanceof Software) {
	            	DB_Softwares.updateSoftware((Software)oldAsset, (Software)newAsset);
	            }
				
				createActionForAsset(Helper.UPDATE_MODE, oldAsset);
				
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
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
							
							Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty);
							
							return asset;
						}
					}    
				}
			} catch (SQLException e) {
				Helper.displayErrorMessage("Error",e.getMessage());
			}
			
			return null;
		}
	 
	 public static void createActionForAsset(String action_type,Asset asset) {
		Action action = new Action(0, action_type, asset.toString() , Helper.ASSET, java.sql.Date.valueOf(LocalDate.now()));
		ObservableList<Action> recentActions = ((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().getrecentActionsObsList();
		DB_Actions.createAction(action,recentActions);
	 }
	
}
