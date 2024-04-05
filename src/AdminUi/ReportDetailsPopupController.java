package AdminUi;

import Components.Asset;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ReportDetailsPopupController {
	
	private Asset reportedAsset;
	
	public void setAsset(Asset asset) {
		this.reportedAsset = asset;
	}
	
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
}
