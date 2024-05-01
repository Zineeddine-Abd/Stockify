package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Components.Asset;
import Components.Software;
import javafx.collections.ObservableList;

public class DB_Softwares extends DB_Utilities{
	
	public static void refreshSoftwares(ObservableList<Asset> softwares,ObservableList<Asset> correspondingAssets) {
		try(Connection con = DB_Utilities.getDataSource().getConnection()){
			String getAllAssetsQuery = "SELECT * FROM softwares WHERE software_id=?";
			try(PreparedStatement psAssets = con.prepareStatement(getAllAssetsQuery)){
				for(Asset coresAsset : correspondingAssets) {					
					psAssets.setInt(1, coresAsset.getAsset_id());
					try(ResultSet rs = psAssets.executeQuery()){
						while(rs.next()) {
							String license_key = rs.getString("software_license_key");
							String version = rs.getString("software_version");
							Software software = new Software(coresAsset.getAsset_id(),coresAsset.getAsset_category(),coresAsset.getAsset_type(),coresAsset.getAsset_model(),coresAsset.getAsset_status(),coresAsset.getAsset_room(),coresAsset.getAsset_purchase_date(),coresAsset.getAsset_warranty(),license_key,version);
							softwares.add(software);
						}
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void addSoftware(Software software) {
		try(Connection con = dataSource.getConnection()){
			String insertAsset = "INSERT INTO softwares (software_id,software_license_key,software_version) VALUES (?,?,?)";
			try(PreparedStatement ps = con.prepareStatement(insertAsset,Statement.RETURN_GENERATED_KEYS)){
				ps.setInt(1, software.getAsset_id());
				ps.setString(2, software.getSoftware_license_key());
				ps.setString(3, software.getSoftware_version());
				
				ps.executeUpdate();
				
			}
		}catch(SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static void updateSoftware(Software oldSoft,Software newSoft) {
		try(Connection con = dataSource.getConnection()){
			String testUpdate = "UPDATE softwares SET software_license_key = ?, software_version = ? WHERE software_id=?";
			try(PreparedStatement ps = con.prepareStatement(testUpdate)){
				
				ps.setString(1,newSoft.getSoftware_license_key());
				ps.setString(2,newSoft.getSoftware_version());
				ps.setInt(3,oldSoft.getAsset_id());
				ps.executeUpdate();
				
				oldSoft.setSoftware_license_key(newSoft.getSoftware_license_key());
				oldSoft.setSoftware_version(newSoft.getSoftware_version());
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			Helper.displayErrorMessage("Error",e.getMessage());
		}
	}
	
	public static Software getSoftware(Asset asset) {
		Software soft = null;
		try (Connection con = DB_Utilities.getDataSource().getConnection())
		{
			String getAllUsersQuery = "SELECT * FROM softwares WHERE software_id=?";
			try(PreparedStatement ps = con.prepareStatement(getAllUsersQuery)){
				ps.setInt(1, asset.getAsset_id());
				try (ResultSet rs = ps.executeQuery();){
					if(rs.next()) {
						String license_key = rs.getString("software_license_key");
						String version = rs.getString("software_version");
						soft = new Software(asset,license_key,version);
						
					}
				}    
			}
		} catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
		}
		
		return soft;
	}
}
