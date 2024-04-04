package Components;

import java.sql.Date;

public class HardwareAsset extends Asset {

    public HardwareAsset(int assetId,String assetCategory, String assetType, String model, String status, String room,Date purchase_date,int warranty,int serial_number) {
        super(assetId, assetCategory, assetType, model, status, room, purchase_date ,warranty, serial_number);
    }
}