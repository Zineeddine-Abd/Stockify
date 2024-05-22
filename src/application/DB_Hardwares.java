package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Asset;
import Components.Hardware;
import javafx.collections.ObservableList;

public class DB_Hardwares extends DB_Utilities{
	
	public static int last_id = 0;
	
	public static void refreshHardwares(ObservableList<Asset> hardwares,ObservableList<Asset> correspondingAssets) {
		try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllAssetsQuery = "SELECT * FROM hardwares WHERE hardware_id=?";
			try(PreparedStatement psAssets = con.prepareStatement(getAllAssetsQuery)){
				for(Asset coresAsset : correspondingAssets) {					
					psAssets.setInt(1, coresAsset.getAsset_id());
					try(ResultSet rs = psAssets.executeQuery()){
						while(rs.next()) {
							String serial_number = rs.getString("hardware_serial_number");
							Hardware hardware = new Hardware(coresAsset.getAsset_id(),coresAsset.getAsset_category(),coresAsset.getAsset_type(),coresAsset.getAsset_model(),coresAsset.getAsset_status(),coresAsset.getAsset_room(),coresAsset.getAsset_purchase_date(),coresAsset.getAsset_warranty(),serial_number);
							hardwares.add(hardware);
						}
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void addHardware(Hardware hardware) {
		try(Connection con = dataSource.getConnection()){
			String insertAsset = "INSERT INTO hardwares (hardware_id,hardware_serial_number) VALUES (?,?)";
			try(PreparedStatement ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS)){
				ps.setInt(1, hardware.getAsset_id());
				ps.setString(2, hardware.getHardware_serial_number());
				ps.executeUpdate();
				
			}
		}catch(SQLException e) {
			DB_Assets.flag = true;
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void updateHardware(Hardware oldHard,Hardware newHard) {
		try(Connection con = dataSource.getConnection()){
			String updateAsset = "UPDATE hardwares SET "
					+ "hardware_serial_number = ? "
					+ "WHERE hardware_id = ?";
			try(PreparedStatement ps = con.prepareStatement(updateAsset)){
				
				ps.setString(1,newHard.getHardware_serial_number());
				ps.setInt(2,oldHard.getAsset_id());
				
				ps.executeUpdate();
				
				oldHard.setHardware_serial_number(newHard.getHardware_serial_number());
				
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static Hardware getHardware(Asset asset) {
		Hardware hard = null;
		try (Connection con = DB_Utilities.getDataSource().getConnection())
		{
			String getAllUsersQuery = "SELECT * FROM hardwares WHERE hardware_id=?";
			try(PreparedStatement ps = con.prepareStatement(getAllUsersQuery)){
				ps.setInt(1, asset.getAsset_id());
				try (ResultSet rs = ps.executeQuery();){
					if(rs.next()) {
						String serial_num = rs.getString("hardware_serial_number");
						hard = new Hardware(asset,serial_num);
					}
				}
			}
		} catch (SQLException | NullPointerException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		
		return hard;
	}
	
}
