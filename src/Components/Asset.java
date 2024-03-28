package Components;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asset {
    private final StringProperty asset_id,asset_category,asset_type,asset_model,asset_status,asset_location,asset_purchase_date,asset_warranty,asset_serial_number;


    public Asset(String assetId,String asset_category, String assetType, String model, String status, String location, String warranty,String serial_number) {
        this.asset_id = new SimpleStringProperty(assetId);
        this.asset_category = new SimpleStringProperty(asset_category);
        this.asset_type = new SimpleStringProperty(assetType);
        this.asset_model = new SimpleStringProperty(model);
        this.asset_status = new SimpleStringProperty(status);
        this.asset_location = new SimpleStringProperty(location);
        this.asset_purchase_date = new SimpleStringProperty(LocalDate.now().toString());
        this.asset_warranty = new SimpleStringProperty(warranty);
        this.asset_serial_number = new SimpleStringProperty(serial_number);
    }


	public StringProperty getAsset_id() {
		return asset_id;
	}


	public StringProperty getAsset_category() {
		return asset_category;
	}


	public StringProperty getAsset_type() {
		return asset_type;
	}


	public StringProperty getAsset_model() {
		return asset_model;
	}


	public StringProperty getAsset_status() {
		return asset_status;
	}


	public StringProperty getAsset_location() {
		return asset_location;
	}


	public StringProperty getAsset_purchase_date() {
		return asset_purchase_date;
	}


	public StringProperty getAsset_warranty() {
		return asset_warranty;
	}


	public StringProperty getAsset_serial_number() {
		return asset_serial_number;
	}

}
