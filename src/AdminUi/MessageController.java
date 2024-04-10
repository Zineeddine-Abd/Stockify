package AdminUi;

import java.io.IOException;

import Components.Asset;
import Components.Message;
import application.Helper;
import application.Main;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MessageController{
	@FXML
	private ListView<Message> allMessagesList;
	
  	private Stage fillFormula;
  	private Scene createNewScene;
  	
  	private Asset currentAsset;
  	
  	public void setAsset(Asset asset) {
  		this.currentAsset = asset;
  	}
  	
  	private ObservableList<Message> messagesList;
  	private ObservableList<Message> filteredMessagesList;

	public void setItems() {
		messagesList = ((AdminController)Helper.currentAdminLoader.getController()).getMessagesList();
		filteredMessagesList = new FilteredList<Message>(messagesList, message -> message.getCor_asset_id() == currentAsset.getAsset_id());
		
		allMessagesList.setItems(filteredMessagesList);
		
		allMessagesList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<Message>() {
                    @Override
                    protected void updateItem(Message item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                            setOnMouseClicked(event->showReportDetails(event, this.getListView().getSelectionModel().getSelectedItem()));
                        }
                    }
                };
            }
        });
	}
	
	
	
	public void showReportDetails(MouseEvent event ,Message item) {
		Parent root;
		try {
			AdminController.currentReportDetailsPopupLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlReportDetails));
			root = AdminController.currentReportDetailsPopupLoader.load();//this is the problem.
			ReportDetailsPopupController controller = (ReportDetailsPopupController)AdminController.currentReportDetailsPopupLoader.getController();
			controller.setMessage(item);
			controller.setItems();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Report Details:");
			
			createNewScene = new Scene(root);
			createNewScene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
	
			fillFormula.setScene(createNewScene);
			fillFormula.getIcons().add(Main.itAssetLogo);
			
			//make it as a dialog box
			Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
			fillFormula.initModality(Modality.WINDOW_MODAL);
			fillFormula.initOwner(parentStage);
			
			fillFormula.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
