package Components;

import java.sql.Date;

public class Asset {
	
	private int asset_id;
	private String asset_room;
    private String asset_category, asset_type, asset_model, asset_status;
    Date asset_purchase_date;
    private int asset_warranty, asset_serial_number;
    
    public Asset(int assetId,String asset_category, String assetType, String model, String status, String room,Date purchase_date, int warranty,int serial_number) {
        this.asset_id = assetId;
        this.asset_category = asset_category;
        this.asset_type = assetType;
        this.asset_model = model;
        this.asset_status = status;
        this.asset_room = room;
        this.asset_purchase_date = purchase_date;
        this.asset_warranty = warranty;
        this.asset_serial_number = serial_number;
    }

	public int getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(int asset_id) {
		this.asset_id = asset_id;
	}

	public String getAsset_category() {
		return asset_category;
	}

	public void setAsset_category(String asset_category) {
		this.asset_category = asset_category;
	}

	public String getAsset_type() {
		return asset_type;
	}

	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}

	public String getAsset_model() {
		return asset_model;
	}

	public void setAsset_model(String asset_model) {
		this.asset_model = asset_model;
	}

	public String getAsset_status() {
		return asset_status;
	}

	public void setAsset_status(String asset_status) {
		this.asset_status = asset_status;
	}

	public String getAsset_room() {
		return asset_room;
	}

	public void setAsset_room(String asset_room) {
		this.asset_room = asset_room;
	}

	public Date getAsset_purchase_date() {
		return asset_purchase_date;
	}

	public void setAsset_purchase_date(Date asset_purchase_date) {
		this.asset_purchase_date = asset_purchase_date;
	}

	public int getAsset_warranty() {
		return asset_warranty;
	}

	public void setAsset_warranty(int asset_warranty) {
		this.asset_warranty = asset_warranty;
	}

	public int getAsset_serial_number() {
		return asset_serial_number;
	}

	public void setAsset_serial_number(int asset_serial_number) {
		this.asset_serial_number = asset_serial_number;
	}
	
	public String toString() {
		return this.getAsset_id()+": "+this.getAsset_category()+": "+this.getAsset_type()+": "+this.getAsset_model()+": "+this.getAsset_status() + " ";
	}

}
