package Components;

import java.sql.Date;

public class Software extends Asset {

    private String software_license_key;
    private String software_version;
	
	public Software(int assetId,String assetCategory, String assetType, String model, String status, String room,Date purchase_date,int warranty,String license_key,String version) {
        super(assetId, assetCategory, assetType, model, status, room, purchase_date, warranty);
        this.software_license_key = license_key;
        this.software_version = version;
    }
	
	public Software(Asset asset , String licenseKey,String version) {
		super(asset.getAsset_id(),asset.getAsset_category(),asset.getAsset_type(),asset.getAsset_model(),asset.getAsset_status(),asset.getAsset_room(),asset.getAsset_purchase_date(),asset.getAsset_warranty());
		this.software_license_key = licenseKey;
		this.software_version = version;
	}

	public String getSoftware_version() {
		return software_version;
	}

	public void setSoftware_version(String software_version) {
		this.software_version = software_version;
	}

	public String getSoftware_license_key() {
		return software_license_key;
	}

	public void setSoftware_license_key(String software_license_key) {
		this.software_license_key = software_license_key;
	}
    
    
}