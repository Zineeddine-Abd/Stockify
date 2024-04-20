package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Components.Asset;
import Components.Hardware;
import Components.Software;
import javafx.collections.ObservableList;

public class DB_Softwares extends DB_Utilities{
	
	public static void refreshSoftwares(ObservableList<Software> softwares,ObservableList<Asset> correspondingAssets) {
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
}
