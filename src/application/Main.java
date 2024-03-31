package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	
	public final static Image itAssetLogo = new Image(Main.class.getResourceAsStream("/Logo.jpg"));
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent loginroot = FXMLLoader.load(getClass().getResource("/LoginUi/loginScene.fxml"));
			Scene loginScene = new Scene(loginroot);	
			String css = this.getClass().getResource("/LoginUi/loginUI.css").toExternalForm();
			loginScene.getStylesheets().add(css);
			primaryStage.setScene(loginScene);
			primaryStage.setTitle("Stockify - Login");
			primaryStage.getIcons().add(itAssetLogo);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setOnCloseRequest(e ->{
				e.consume();
				primaryStage.close();
				if(DatabaseUtilities.getDataSource() != null) {
					DatabaseUtilities.getDataSource().close();
				}
			});
			
			//connectToStockifyDB();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
