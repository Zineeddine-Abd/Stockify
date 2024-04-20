package Components;

import java.sql.Date;

public class Hardware extends Asset {
	
	private String hardware_serial_number;
	
    public Hardware(int assetId,String assetCategory, String assetType, String model, String status, String room,Date purchase_date,int warranty,String serial_number) {
        super(assetId, assetCategory, assetType, model, status, room, purchase_date ,warranty);
        this.hardware_serial_number = serial_number;
    }

    public String getHardware_serial_number() {
        return hardware_serial_number;
    }

    public void setHardware_serial_number(String hardware_serial_number) {
        this.hardware_serial_number = hardware_serial_number;
    }
}