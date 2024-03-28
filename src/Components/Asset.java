package Components;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asset {
	
	private int asset_id;
    private String asset_category, asset_type, asset_model, asset_status, asset_location, asset_purchase_date, asset_warranty, asset_serial_number;

    public Asset(int assetId,String asset_category, String assetType, String model, String status, String location, String warranty,String serial_number) {
        this.asset_id = assetId;
        this.asset_category = asset_category;
        this.asset_type = assetType;
        this.asset_model = model;
        this.asset_status = status;
        this.asset_location = location;
        this.asset_purchase_date = LocalDate.now().toString();
        this.asset_warranty = warranty;
        this.asset_serial_number = serial_number;
    }


	public int getAsset_id() {
		return asset_id;
	}


	public String getAsset_category() {
		return asset_category;
	}


	public String getAsset_type() {
		return asset_type;
	}


	public String getAsset_model() {
		return asset_model;
	}


	public String getAsset_status() {
		return asset_status;
	}


	public String getAsset_location() {
		return asset_location;
	}


	public String getAsset_purchase_date() {
		return asset_purchase_date;
	}


	public String getAsset_warranty() {
		return asset_warranty;
	}


	public String getAsset_serial_number() {
		return asset_serial_number;
	}

}
