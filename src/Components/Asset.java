package Components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Asset {
    private final StringProperty assetId;
    private final StringProperty assetType;
    private final StringProperty model;
    private final StringProperty status;
    private final StringProperty location;

    public Asset(String assetId, String assetType, String model, String status, String location) {
        this.assetId = new SimpleStringProperty(assetId);
        this.assetType = new SimpleStringProperty(assetType);
        this.model = new SimpleStringProperty(model);
        this.status = new SimpleStringProperty(status);
        this.location = new SimpleStringProperty(location);
    }

    public String getAssetId() {
        return assetId.get();
    }

    public StringProperty assetIdProperty() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId.set(assetId);
    }

    public String getAssetType() {
        return assetType.get();
    }

    public StringProperty assetTypeProperty() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType.set(assetType);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }
}
