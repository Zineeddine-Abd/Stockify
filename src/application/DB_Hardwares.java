package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Components.Asset;
import Components.Hardware;
import Components.Software;
import javafx.collections.ObservableList;

public class DB_Hardwares extends DB_Utilities{
	
	public static void refreshHardwares(ObservableList<Hardware> hardwares,ObservableList<Asset> correspondingAssets) {
		int i=0;
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
							i++;
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
