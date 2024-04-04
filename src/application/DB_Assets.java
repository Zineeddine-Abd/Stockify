package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Asset;
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
						int asset_serial_number = rs.getInt("asset_serial_number");
						
						Asset asset = new Asset(asset_id,asset_category,asset_type,asset_model,asset_status,asset_room,asset_purchase_date,asset_warranty,asset_serial_number);
						obsList.add(asset);
					}
				}
			}
		}catch (SQLException e) {
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
	
}
