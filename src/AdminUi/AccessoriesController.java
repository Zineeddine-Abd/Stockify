package AdminUi;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import Components.Asset;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class AccessoriesController implements Initializable{
	
	@FXML
	private Button newAssetButton;
	
  	@FXML
    public TextField searchTextField;
  	
  	public TextField getSearchTextField() {
  		return searchTextField;
  	}
    @FXML
    public ChoiceBox<String> searchCriteriaComboBox;
    
    public ChoiceBox<String> getSearchCriteriaComboBox(){
    	return searchCriteriaComboBox;
    }
    
    private final String[] criteria = {"Category", "Type", "Model", "Status", "Location"};
    
    //observable lists***************
    private ObservableList<Asset> allAssetsObs;
    FilteredList<Asset> filteredAssets;
    SortedList<Asset> sortedAssets;
    //********************************
    
    public ObservableList<Asset> getAllAssetsObs(){
    	return allAssetsObs;
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
	}
}
