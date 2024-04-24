package AdminUi;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import Components.Asset;
import Components.Hardware;
import Components.Software;
import application.DB_Assets;
import application.DB_Hardwares;
import application.DB_Softwares;
import application.Helper;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AssetsTableController implements Initializable{
    //stage and scene:********************
  	private Stage fillFormula;
  	private Scene createNewScene;
  	//*************************************************
  	//switching buttons :
  	public static final int ASSET_BUTTON = 0;
  	public static final int HARDWARE_BUTTON = 1;
  	public static final int SOFTWARE_BUTTON = 2;
  	
  	
  	@FXML
  	private Button assetsButton;
  	@FXML
  	private Button hardwaresButton;
  	@FXML
  	private Button softwaresButton;
  	
  	private String DEFAULT_STYLE;
  	
  	Button[] buttonsAbove;
  	
  	private Button currentView;//used to determine which button is now clicked.
  	
	//assets Table Columns starts here:********************
	@FXML
    private TableView<Asset> assetsTable;
	
	public TableView<Asset> getAssetsTable(){
		return assetsTable;
	}
    @FXML
    private TableColumn<Asset, Integer> assetIdColumn;
    
    @FXML
    private TableColumn<Asset, String> assetCategoryColumn;
    
    @FXML
    private TableColumn<Asset, String> assetTypeColumn;

    @FXML
    private TableColumn<Asset, String> modelColumn;
    
    @FXML
    private TableColumn<Asset, String> statusColumn;

    @FXML
    private TableColumn<Asset, String> roomColumn;
    
    @FXML
    private TableColumn<Asset, Date> assetPurchaseDateColumn;

    @FXML
    private TableColumn<Asset, Integer> assetWarrantyColumn;
    
    @FXML
    private TableColumn<Asset, String> editColumn;
	//ends here ************************************************
    
    //hardware table columns:
	@FXML
    private TableColumn<Asset, String> hardwareSerialNumberColumn;
	
	//Software Table Columns here:
	@FXML
	private TableColumn<Asset , String> softwareLicenseKey;
	@FXML
	private TableColumn<Asset , String> softwareVersion;
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
    
    private final String[] assetCriteria = {"Id","Category", "Type", "Model", "Status", "Location"};
    private final String[] hardwareCriteria = {"Id" ,"Type", "Model", "Status", "Location" , "Serial Number"};
    private final String[] softwareCriteria = {"Id", "Type", "Model", "Status", "Location" ,"License Key","Version"};
    //observable lists***************
    private ObservableList<Asset> allAssetsObs;
    FilteredList<Asset> filteredAssets;
    FilteredList<Asset> filteredListTempo;
    SortedList<Asset> sortedAssets;
    //********************************
    
    public ObservableList<Asset> getAllAssetsObs(){
    	return allAssetsObs;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allAssetsObs = FXCollections.observableArrayList();
		
		DB_Assets.refresh(allAssetsObs);
		
		
		HideAllNonHardCols();
		HideAllNonSoftCols();
		
		assetsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        assetIdColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_id"));
        assetCategoryColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_category"));
        assetTypeColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_type"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_model"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_status"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<Asset, String>("asset_room"));
        assetPurchaseDateColumn.setCellValueFactory(new PropertyValueFactory<Asset, Date>("asset_purchase_date"));
        assetWarrantyColumn.setCellValueFactory(new PropertyValueFactory<Asset, Integer>("asset_warranty"));
        
        hardwareSerialNumberColumn.setCellValueFactory(
                cellData -> {
                    Asset asset = cellData.getValue();
                    if (asset instanceof Hardware) {
                        return new ReadOnlyStringWrapper(((Hardware)asset).getHardware_serial_number()).getReadOnlyProperty();
                    } else {
                        return new ReadOnlyStringWrapper("").getReadOnlyProperty(); // Handle non-Hardware objects
                    }
                }
        );
        
        softwareLicenseKey.setCellValueFactory( cellData -> {
            Asset asset = cellData.getValue();
            if (asset instanceof Software) {
                return new ReadOnlyStringWrapper(((Software)asset).getSoftware_license_key()).getReadOnlyProperty();
            } else {
                return new ReadOnlyStringWrapper("").getReadOnlyProperty(); // Handle non-Hardware objects
            }
        });
        
        softwareVersion.setCellValueFactory( cellData -> {
            Asset asset = cellData.getValue();
            if (asset instanceof Software) {
                return new ReadOnlyStringWrapper(((Software)asset).getSoftware_version()).getReadOnlyProperty();
            } else {
                return new ReadOnlyStringWrapper("").getReadOnlyProperty(); // Handle non-Hardware objects
            }
        });
        
        
        assetsTable.setRowFactory(tv -> new TableRow<Asset>() {
            @Override
            protected void updateItem(Asset item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    setTextFill(Color.BLACK); // Default text color
                } else {
                    if (isSelected()) {
                        setTextFill(Color.WHITE); // Change text color when selected
                        setStyle("-fx-background-color: #0096c9;"); // Change background color when selected
                    } else {
                        setTextFill(Color.BLACK); // Default text color
                        if (item.getAsset_status().equals("Broken")) {
                            setStyle("-fx-background-color: #FB9494;");
                        } else if (item.getAsset_status().equals("Under Maintenance")) {
                            setStyle("-fx-background-color: #FFB266;");
                        } else if (item.getAsset_status().equals("Ready To Use")) {
                            setStyle("-fx-background-color: #B2FF66;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            }
            
            //try and add the top HBox in All Assets ( the actual one) to the stackpane to include it because it depends on the category type (hard soft acc)

            @Override
            public void updateSelected(boolean selected) {
            	super.updateSelected(selected);
                if (!selected) {
                    // Reset background color to its original state
                    if (getItem() != null) {
                        Asset item = getItem(); // Access the item
                        if (item.getAsset_status().equals(NewAssetController.BROKEN)) {
                            setStyle("-fx-background-color: #FB9494;");
                        } else if (item.getAsset_status().equals(NewAssetController.UNDER_MAINTENANCE)) {
                            setStyle("-fx-background-color: #FFB266;");
                        } else if (item.getAsset_status().equals(NewAssetController.READY_TO_USE)) {
                            setStyle("-fx-background-color: #B2FF66;");
                        } else {
                            setStyle("");
                        }
                    }
                } else {
                    // Change text color and background color when selected
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: #0096c9;");
                }
            }
        });
        
        editColumn.setReorderable(false);
		editColumn.setCellFactory((col)->{
			TableCell<Asset, String> cell = new TableCell<Asset, String>(){
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if(empty) {
						setGraphic(null);
					}else {
						FontAwesomeIconView edit = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE_ALT);
						
						FontAwesomeIconView report = new FontAwesomeIconView(FontAwesomeIcon.NEWSPAPER_ALT);
						
						FontAwesomeIconView reportMessage = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE);
						
						edit.setGlyphSize(18);
						edit.setCursor(Cursor.HAND);
						
						report.setGlyphSize(18);
						report.setCursor(Cursor.HAND);
						
						reportMessage.setGlyphSize(18);
						reportMessage.setCursor(Cursor.HAND);
						
						
						edit.hoverProperty().addListener((obs, oldVal, newVal) -> {
								if(newVal) {
									edit.setFill(Color.GREEN);
								}else {
									edit.setFill(Color.BLACK);
								}
						});
						
						report.hoverProperty().addListener((obs, oldVal, newVal) -> {
							if(newVal) {
								report.setFill(Color.BLUE);
							}else {
								report.setFill(Color.BLACK);
							}
						});
						
						reportMessage.hoverProperty().addListener((obs,oldVal,newVal)->{
							if(newVal) {
								reportMessage.setFill(Color.RED);
							}else {
								reportMessage.setFill(Color.BLACK);
							}
						});
						
						//((AdminController)Helper.currentAdminLoader.getController()).updateNumberOfItems();
						
						edit.setOnMouseClicked(event -> popupUpdateAsset(event, this.getTableRow().getItem()));
						
						report.setOnMouseClicked(event-> reportAsset(event , this.getTableRow().getItem()));
						
						reportMessage.setOnMouseClicked(event-> showMessagesList(event,this.getTableRow().getItem()));
						
						HBox box = new HBox(edit, report , reportMessage);
						HBox.setMargin(edit, new Insets(2, 2, 0, 3));
						HBox.setMargin(report, new Insets(2, 2, 0, 3));
						HBox.setMargin(reportMessage, new Insets(2, 2, 0, 3));
						
						setGraphic(box);
						
					}
				}
			};
			return cell;
		});
		
		filteredListTempo = new FilteredList<Asset>(allAssetsObs);
		
		
		
		filteredAssets = new FilteredList<Asset>(filteredListTempo);
		
        filterTableView();
        
        sortedAssets = new SortedList<Asset>(filteredAssets);
        sortedAssets.comparatorProperty().bind(assetsTable.comparatorProperty());
        
        assetsTable.setItems(sortedAssets);
        
        // Initialize search criteria ComboBox for asset
        searchCriteriaComboBox.getItems().addAll(assetCriteria);
        searchCriteriaComboBox.setValue(assetCriteria[0]);
        setFilterPredicate(null);
        //buttons above init for styling purposes :D 
        DEFAULT_STYLE = assetsButton.getStyle();
        buttonsAbove = new Button[] {assetsButton,hardwaresButton,softwaresButton};
        
        setButtonStyle(ASSET_BUTTON);
	}
	
	public void initAssets() {
//		FilteredList<Asset> filteredHards = new FilteredList<Asset>(allAssetsObs, asset -> asset.getAsset_category().equals("Hardware"));
//		FilteredList<Asset> filteredSofts = new FilteredList<Asset>(allAssetsObs, asset -> asset.getAsset_category().equals("Software"));
		InvalidationListener listener = observable -> {
//			((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().numHardware.setText(String.valueOf(filteredHards.size()));
//			((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().numSoftware.setText(String.valueOf(filteredSofts.size()));
        	((AdminController)Helper.currentAdminLoader.getController()).getDashboardViewController().setItems(); 
        };
        
        allAssetsObs.addListener(listener);
	}
	
	
	
	public void popupNewAsset(ActionEvent event){
		VBox root;
			
		try {
			AdminController.currentNewAssetLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewAsset));
			
			root = AdminController.currentNewAssetLoader.load();
			NewAssetController controller = (NewAssetController)AdminController.currentNewAssetLoader.getController();
			controller.setOldAsset(null);
			controller.setTitleLabelText("New Asset");
			if(currentView == hardwaresButton) {
				controller.setHardwareFields();
			}else if(currentView == softwaresButton) {
				controller.setSoftwareFields();
			}
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Create New Asset:");
			
			//AssetController.setStage(fillFormula);
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
	
	public void popupUpdateAsset(MouseEvent event, Asset asset){
		Parent root;
		try {
			AdminController.currentNewAssetLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewAsset));
			
			root = AdminController.currentNewAssetLoader.load();
			NewAssetController controller = (NewAssetController)AdminController.currentNewAssetLoader.getController();
			if(asset instanceof Hardware) {
				Hardware hard = (Hardware) asset;
				controller.setOldAsset(hard);
			}else if(asset instanceof Software) {
				Software soft = (Software) asset;
				controller.setOldAsset(soft);
			}else {
				controller.setOldAsset(asset);
			}
			
			controller.setTitleLabelText("Update Asset");
			controller.setInfos();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			fillFormula.setTitle("Update Asset:");
			
			//AssetController.setStage(fillFormula);
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
	
	public void reportAsset(MouseEvent event , Asset item) {
		Parent root;
		
		try {
			AdminController.currentReportPopupLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlReport));
			root = AdminController.currentReportPopupLoader.load();
			ReportPopupController controller = (ReportPopupController)AdminController.currentReportPopupLoader.getController();
			controller.setOldAsset(item);
			controller.checkWarrantyValidation();
			if(controller.getOldAsset().getAsset_category().equals(Helper.SOFTWARE)) {
				controller.getStatusChoiceBox().getItems().addAll(ReportPopupController.reportSoftwareStatuses);
			}else {		
				controller.getStatusChoiceBox().getItems().addAll(ReportPopupController.reportStatuses);
			}
			
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("Report an Asset:");
			
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
	
	
	public void showMessagesList(MouseEvent event ,Asset asset) {
		Parent root;
		
		try {
			AdminController.currentMessagesLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlMessages));
			root = AdminController.currentMessagesLoader.load();
			MessageController controller = (MessageController)AdminController.currentMessagesLoader.getController();
			controller.setAsset(asset);
			controller.setItems();
			
			fillFormula = new Stage();
			fillFormula.setResizable(false);
			
			fillFormula.setTitle("All Reports");
			
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
	
	
	public void addAsset(Asset newAsset) {
       DB_Assets.addAsset(allAssetsObs, newAsset);
    }
	
    public void deleteSelectedAssets() {
    	ObservableList<Asset> selectedAssets = assetsTable.getSelectionModel().getSelectedItems();
    	
    	if(Helper.displayConfirmMessge("Are you sure you want to delete item(s)?","This action cannot be undone.")) {    		
    		//you would wonder how this worked? well i just switched order between loop and removeAll method - lokman 	
    		DB_Assets.removeAsset(allAssetsObs, selectedAssets);
    	}
    }
    
    public void updateAsset(Asset oldAsset, Asset newAsset) {
		DB_Assets.updateAsset(oldAsset, newAsset);
		assetsTable.refresh();
	}
    
    public void addHardware(Hardware hardware) {
    	DB_Hardwares.addHardware(hardware);
    }
    
    public void updateHardware(Asset oldAsset,Hardware newHardware){
    	if(oldAsset instanceof Hardware) {
    		Hardware oldHard = (Hardware) oldAsset;
    		DB_Hardwares.updateHardware(oldHard,newHardware);
    	}
    }
    
    public void addSoftware(Software software) {
    	DB_Softwares.addSoftware(software);
    }
    
    public void updateSoftware(Asset oldAsset,Software newSoftware) {
    	if(oldAsset instanceof Software) {
    		Software oldSoft = (Software) oldAsset;
    		
    		DB_Softwares.updateSoftware(oldSoft,newSoftware);
    	}
    }
    
    //Filtering methods*********************************************
    public void setFilterPredicateTempo(String txt) {
    	filteredListTempo.setPredicate(asset->{
    		if(txt.equals("Hardware")) {
    			return asset.getAsset_category().equals(Helper.HARDWARE);
    		}else if(txt.equals("Software")) {
    			return asset.getAsset_category().equals(Helper.SOFTWARE);
    		}
    		return true;
    	});
    }
    
    
	public void filterTableView() {
		searchTextField.textProperty().addListener((obs, oldTxt, newTxt)->{
			setFilterPredicate(newTxt);
		});
		searchCriteriaComboBox.valueProperty().addListener((obs, oldVal, newVal)->{
			setFilterPredicate(searchTextField.getText());
		});
	}
	
	private void setFilterPredicate(String txt) {
		filteredAssets.setPredicate((asset)-> {
			if(txt == null || txt.isBlank()) {
				return true;
			}else {
				if(searchCriteriaComboBox.getValue() == null) {
					return false;
				}
				switch (searchCriteriaComboBox.getValue()) {
				case "Category":
					if(asset.getAsset_category().toLowerCase().contains(txt.toLowerCase())) {	
						return true;
					}
		            break;
		        case "Type":
		        	if(asset.getAsset_type().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Model":
		        	if(asset.getAsset_model().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Status":
		        	if(asset.getAsset_status().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Location":
		        	if(asset.getAsset_room().toLowerCase().contains(txt.toLowerCase())) {
						return true;
					}
		            break;
		        case "Serial Number":
		        	if(asset instanceof Hardware) {
		        		if(((Hardware)asset).getHardware_serial_number().toLowerCase().contains(txt.toLowerCase())) {
							return true;
						}
		        	}
		            break;
		        case "License Key":
		        	if(asset instanceof Software) {
		        		if(((Software)asset).getSoftware_license_key().toLowerCase().contains(txt.toLowerCase())) {
							return true;
						}
		        	}
		            break;
		        case "Version":
		        	if(asset instanceof Software) {
		        		if(((Software)asset).getSoftware_version().toLowerCase().contains(txt.toLowerCase())) {
							return true;
						}
		        	}
		            break;
		        case "Id":
		        	if((asset.getAsset_id() + "").toLowerCase().contains(txt.toLowerCase())) {
		        		return true;
		        	}
		        default:
		        	return false;
				}
				return false;
			}
		});
	}
	
	public void refreshAssetsList() {
		allAssetsObs.clear();
		DB_Assets.refresh(allAssetsObs);
    }
	
	//switching methods here:
	
	public void setAssetColumns(ActionEvent event) {
		searchCriteriaComboBox.getItems().clear();
		searchCriteriaComboBox.getItems().addAll(assetCriteria);
		searchCriteriaComboBox.setValue(assetCriteria[0]);
		setButtonStyle(ASSET_BUTTON);
		
		setFilterPredicateTempo("");
		HideAllNonHardCols();
		HideAllNonSoftCols();
	}
	
	
	public void setHardwareColumns(ActionEvent event) {
		searchCriteriaComboBox.getItems().clear();
        searchCriteriaComboBox.getItems().addAll(hardwareCriteria);
        searchCriteriaComboBox.setValue(hardwareCriteria[0]);
        searchTextField.setText("");
        
		setButtonStyle(HARDWARE_BUTTON);
		hardwareSerialNumberColumn.setVisible(true);
		setFilterPredicateTempo(Helper.HARDWARE);
		
		HideAllNonHardCols();
	}
	
	
	public void setSoftwareColumns(ActionEvent event) {
		searchCriteriaComboBox.getItems().clear();
		searchCriteriaComboBox.getItems().addAll(softwareCriteria);
        searchCriteriaComboBox.setValue(softwareCriteria[0]);
		
		setButtonStyle(SOFTWARE_BUTTON);
		setFilterPredicateTempo(Helper.SOFTWARE);
		
		softwareLicenseKey.setVisible(true);
		softwareVersion.setVisible(true);
		
		HideAllNonSoftCols();
	}
	
	public void setButtonStyle(int button) {
		for(int i=0;i<buttonsAbove.length;i++) {
			if(i != button) {
				buttonsAbove[i].setStyle(DEFAULT_STYLE);
			}else {
				currentView = buttonsAbove[i];
				buttonsAbove[i].setStyle(DEFAULT_STYLE + "-fx-background-color:#8DA0B5;");
			}
		}
	}
	
	public void HideAllNonHardCols() {
		softwareLicenseKey.setVisible(false);
		softwareVersion.setVisible(false);
	}
	public void HideAllNonSoftCols() {
		hardwareSerialNumberColumn.setVisible(false);
	}
	
}
