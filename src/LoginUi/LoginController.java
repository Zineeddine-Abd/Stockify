package LoginUi;

import application.*;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javafx.event.EventHandler;
import AdminUi.AdminController;
import AdminUi.NewUserController;
import Components.Credentials;
import Components.Session;
import Components.User;
import ProfessorUi.ProfessorController;
import TechnicianUi.TechnicianController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;




public class LoginController implements Initializable{
	private static Session currentSession;
	
	public static void setSession(Session ses) {
		currentSession = ses;
	}
	
	private static User currentLoggedInUser;
	
	public static User getLoggedUser() {
		return currentLoggedInUser;
	}
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private static FXMLLoader currentResetPasswordLoader;
	public static final String fxmlResetPassword = "/LoginUi/resetPasswordScene.fxml";
	
	public static final String savedCredentialsFilePath = "creds.ser";
	
	private static final String ADMIN = "Administrator";
	private static final String TECHNICIAN = "Technician";
	private static final String PROFESSOR = "Professor";
	public static final String[] permissions = {ADMIN,TECHNICIAN,PROFESSOR};
	
	@FXML
	private Hyperlink signupLink;
	@FXML
	private Hyperlink forgotPasswordLink;
	@FXML
	private Button loginButton;
	@FXML
	private Label incorrectInfo;
	//password text fields : 
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField showPasswordField;
	@FXML
	private CheckBox showPassBox;
	@FXML
	private CheckBox rememberMe;
	
	private static String currentSalt;
	
	public static String getHashedPasswordSalt() {
		return currentSalt;
	}
	
	private boolean fileExists = false;
	public static boolean isSigningUp = false;
	//************************************Methods:****************************************************************
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		fileExists = fileExists(savedCredentialsFilePath) ? true : false;
		
		if(fileExists) {
			rememberMe.setSelected(true);
			try {
				assignUser(null);
			} catch (IOException e) {
				Helper.displayErrorMessage("Error", e.getMessage());
			}
		}
		
		// Add event listener to username field for Enter key press
        usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    // Trigger login action when Enter key is pressed
                    loginButton.fire();
                }
            }

        });
        
        // Add event listener to username field for Enter key press
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    // Trigger login action when Enter key is pressed
                    loginButton.fire();
                }
            }

        });

	}
	
	private void assignUser(ActionEvent event) throws IOException {
		
		try (Connection con = DB_Utilities.getDataSource().getConnection()) {
			String password = (showPassBox.isSelected() ? showPasswordField.getText() : passwordField.getText());
			String username =  (fileExists ? getDecryptedUsername(retrieveSecretKey()):usernameField.getText());
	        String sql = "SELECT * FROM users WHERE username=" +"'" + username +"';";
	        
	        try(PreparedStatement statement = con.prepareStatement(sql);
	        	ResultSet resultSet = statement.executeQuery();){
	        	if(resultSet.next()) {
	        		String hashdpassword = resultSet.getString("pass_word");
	        		String user_salt = resultSet.getString("user_salt");
	        		String currentEnteredHashedPassword = (fileExists ? getStoredHashedPass() : hashPassword(password, user_salt));
	        		
	        		if(!currentEnteredHashedPassword.equals(hashdpassword)) {
	        			incorrectInfo.setText("Invalid Password!");
	        			animatedIncorrectInfolabel();
	        			return;
	        		}
	        		
	        		int user_id = resultSet.getInt("user_id");
	        		//username exists.~~
					String email = resultSet.getString("email");
					String first_name = resultSet.getString("first_name");
					String last_name = resultSet.getString("last_name");
					String user_role = resultSet.getString("user_role");
	        		
	        		if(DB_Sessions.sessionExists(user_id)) {
	        			if(fileExists) {
	        				Helper.displayInfoMessage("Login attempt failed!", "Same account launched from another device!");
	        				if(DB_Utilities.getDataSource() != null) {
	        					DB_Utilities.getDataSource().close();
	        				}
	        			}else {
	        				incorrectInfo.setText("Same account launched from another device!");
	        				animatedIncorrectInfolabel();
	        			}
	        			
			        	return;
	        		}
	        		
	        		DB_Sessions.createSession(user_id);
	        		currentLoggedInUser = new User(user_id,username,hashdpassword,email,first_name,last_name,user_role,user_salt);
	        		//initial file creation to save creds.
	        		if(rememberMe.isSelected()) {
	        			createFileForUser(username,hashdpassword);
	        			fileExists = true;
	        		}
	        		
		        	switch(resultSet.getString("user_role")) {
		        		case ADMIN:
		        			directAdmin(event);
		        			break;
		        		case TECHNICIAN:
		        			directTechnician(event);
		        			break;
		        		case PROFESSOR:
		        			directProfessor(event);
		        			break;
		        		default:
		        			System.out.println("error!"); //idk what to put here - lokman.
		        	}
		        }else {
		        	incorrectInfo.setText("Invalid username or password!");
		        	animatedIncorrectInfolabel();
		        }
	        }
		} catch (SQLException e) {
			Helper.displayErrorMessage("Error",e.getMessage());
			
			if(DB_Sessions.sessionExists(currentLoggedInUser.getUser_id())) {
				Helper.closeResources();
			}
		}
	}
	
	
	public void showPassword(ActionEvent event) {
		String pass;
		if(showPassBox.isSelected()) {
			pass = passwordField.getText();
			showPasswordField.setText(pass);
			
			showPasswordField.setVisible(true);
			passwordField.setVisible(false);
		}else {
			pass = showPasswordField.getText();
			passwordField.setText(pass);
			
			showPasswordField.setVisible(false);
			passwordField.setVisible(true);
		}
	}
	
	public void rememberMe(ActionEvent event) {
		//in co memoration of this method, this code will stay here as it was the one of the first to be written in Stockify - lokman
	}
	
	public void loginClicked(ActionEvent event) {
		if(usernameField.getText().isBlank() || 
		isPassBlank())
		{
			incorrectInfo.setText("Invalid username or password!");
			animatedIncorrectInfolabel();
		}else {
			try {
				assignUser(event);
			} catch (Exception e) {
				try {				
					if(DB_Sessions.sessionExists(getLoggedUser().getUser_id())) {
						DB_Sessions.terminateCurrentSession(getLoggedUser().getUser_id());
					}
					Helper.displayErrorMessage("Error!", e.getMessage());
				}catch(NullPointerException e2) {
					Helper.displayErrorMessage("Error",e.getMessage());
				}
			}
		}	
	}
	
	
	private void animatedIncorrectInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),incorrectInfo);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	
	private boolean isPassBlank() {
		return (passwordField.getText().isBlank() && showPasswordField.getText().isBlank());
	}
	
	public void directAdmin(ActionEvent event) throws IOException {
		Helper.currentAdminLoader = new FXMLLoader(getClass().getResource(Helper.fxmlAdmin));
		root = Helper.currentAdminLoader.load();
		newStage(event, root);
		
		AdminController controller = (AdminController)(Helper.currentAdminLoader.getController());
		AnchorPane.setRightAnchor(controller.getLogOutButton(), controller.getAccountButton().getWidth()+100);
	}
	public void directTechnician(ActionEvent event) throws IOException {
		Helper.currentTechnicianLoader = new FXMLLoader(getClass().getResource(Helper.fxmlTechnician));
		root = Helper.currentTechnicianLoader.load();
		newStage(event, root);
		
		TechnicianController controller = (TechnicianController)(Helper.currentTechnicianLoader.getController());
		AnchorPane.setRightAnchor(controller.getLogOutButton(), controller.getAccountButton().getWidth()+100);
	}
	public void directProfessor(ActionEvent event) throws IOException {
		Helper.currentProfessorLoader = new FXMLLoader(getClass().getResource(Helper.fxmlProfessor));
		root = Helper.currentProfessorLoader.load();
		newStage(event, root);
		
		ProfessorController controller = (ProfessorController)(Helper.currentProfessorLoader.getController());
		AnchorPane.setRightAnchor(controller.getLogOutButton(), controller.getAccountButton().getWidth()+100);
	}
	
	private void newStage(ActionEvent event, Parent root) {
		try {			
			if(event != null) {			
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				stage.close();
			}
			scene = new Scene(root);
			
			stage = new Stage();
			
			stage.setScene(scene);
			stage.getIcons().add(Main.itAssetLogo);
			stage.setTitle("Stockify");
			stage.setMaximized(true);
			stage.setMinHeight(700);
			stage.setMinWidth(900);
			
			stage.setOnCloseRequest(e ->{
				DB_Sessions.terminateCurrentSession(currentLoggedInUser.getUser_id());
				e.consume();
				stage.close();
				if(DB_Utilities.getDataSource() != null) {
					DB_Utilities.getDataSource().close();
				}
			});
			stage.centerOnScreen();
			
			stage.show();
		}catch(Exception e) {
			Helper.displayErrorMessage("Error", e.getMessage());
			DB_Sessions.terminateCurrentSession(currentLoggedInUser.getUser_id());
			System.exit(1);
		}
	}
	
	 
	 Stage draggableStage;
	 double x, y;
	 
	 public void getInitialMousePosition(MouseEvent event) {
		draggableStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		x =  event.getScreenX() - draggableStage.getX();
		y =  event.getScreenY() - draggableStage.getY();
	 }
	 
	 public void setNewRootPosition(MouseEvent event) {
		draggableStage.setX(event.getScreenX() - x);
		draggableStage.setY(event.getScreenY() - y);
	 }
	 
	 public void closeLoginScreen(MouseEvent event) {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
		
		Helper.closeResources();
	 }
	 
	 //********************************HASHING METHODS:***************************************************
	 
	 //hash an existing password for comparing purposes.
	 
	 public static String hashPassword(String password,String salt) {
		 int iterations = 65536;
	     int keyLength = 256;
	     
	     byte[] dec = Base64.getDecoder().decode(salt);
	     
	     KeySpec spec = new PBEKeySpec(password.toCharArray(), dec, iterations, keyLength);
	     byte[] hash = null;
	     try {
	    	 SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	    	 hash = factory.generateSecret(spec).getEncoded();
	     } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
	    	 Helper.displayErrorMessage("Error","Invalid Key Spec Or Algorithm Not Found!");
	     }
	     String hashedPass = Base64.getEncoder().encodeToString(hash);
	     
	     return hashedPass;
	 }
	 
	 //hash a newly created password with a new salt.
	 public static String hashPassword(String password) {
		 SecureRandom sr = new SecureRandom();
		 byte[] salt = new byte[16];
		 sr.nextBytes(salt);
		 String encodedSalt = Base64.getEncoder().encodeToString(salt);
		 
		 currentSalt = encodedSalt;
		 
		 int iterations = 65536;
		 int keyLength = 256;
		 
		 KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
		 byte[] hash = null;
		 
		 try {
			 SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			 hash = factory.generateSecret(spec).getEncoded();
		 } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			 Helper.displayErrorMessage("Error","Invalid Key Spec Or Algorithm Not Found!");
		 }
		 String encoded = Base64.getEncoder().encodeToString(hash);
		 
		 return encoded;
	 }
	 
	 //********************************END HASHING METHODS:***************************************************
	 
	 //remember me functionalities
	 
	 private String getStoredHashedPass() {
		 
		 Object obj = retrieveObjectfrom(savedCredentialsFilePath);
		 if(obj instanceof Credentials) {
			 Credentials creds = (Credentials)obj;
			 return creds.getHashedPassword();
		 }else {
			 Helper.displayErrorMessage("Error","Unexpected Error has occured! "
			 		+ "cannot retrieve saved credentials due to invalid file format or data corruption");
		 }
		 return null;
	 }
	 
	 private String getDecryptedUsername(SecretKey key) {
		 Object obj = retrieveObjectfrom(savedCredentialsFilePath);
		 if(obj instanceof Credentials) {
			 try {
				 Credentials creds = (Credentials)obj;
				 String encrypted_username = creds.getUsername();
				 byte[] iv = creds.getIv();
				 
				 //init the AES algorithm.
				 AES aes = new AES();
				 aes.setSecretKey(key);
				 String decrypted_username = aes.decrypt(encrypted_username , iv);
				 //return the original username.
				 return decrypted_username;
			} catch (Exception e) {
				e.printStackTrace();
				Helper.displayErrorMessage("Error", e.getMessage());
			}
		 }else {
			 Helper.displayErrorMessage("Error","Unexpected Error has occured! "
			 		+ "cannot retrieve saved credentials due to invalid file format or data corruption");
		 }
		 return null;
	 }
	 
	 private SecretKey retrieveSecretKey() {
		 Object obj = retrieveObjectfrom(savedCredentialsFilePath);
		 if(obj instanceof Credentials) {
			 Credentials creds = (Credentials)obj;
			 return convertStringToKey(creds.getSk());
		 }else {
			 Helper.displayErrorMessage("Error","Unexpected Error has occured! "
			 		+ "cannot retrieve saved credentials due to invalid file format or data corruption");
		 }
		 return null;
	 }
	 
	 public static void createFileForUser(String username, String hashedPassword) {
		 AES aes = new AES();
		 try {
			aes.generateRandomKey();
			
			String encryptedUsername = aes.encrypt(username);
			byte[] iv = aes.getEncryptionCipherIV();
			SecretKey sk = aes.getSecretKey();
			//hashed password exists
			
			//enc_username + hashedPass + keyToDecryptUsername + initialization vector;
			
			Credentials creds = new Credentials(encryptedUsername, hashedPassword, convertKeyToString(sk) ,iv);
			
			saveObjectTo(savedCredentialsFilePath, creds);
		 } catch (Exception e) {
			 e.printStackTrace();
			 Helper.displayErrorMessage("Error", e.getMessage());
		 }
		 
	 }
	 
	 public static void saveObjectTo(String filename,Credentials obj) {//Serialization
			File filein = new File(filename);
			try {
				FileOutputStream fileOut = new FileOutputStream(filein);
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(obj);
				objOut.close();
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
				Helper.displayErrorMessage("Error", e.getMessage());
			}
		}
		
	 public Object retrieveObjectfrom(String filename) {//deSerialization
		 Object obj = new Object();
		 File file = new File(filename);
		 try {
			 FileInputStream fileIn = new FileInputStream(file);
			 ObjectInputStream objIn = new ObjectInputStream(fileIn);
			 obj = objIn.readObject();
			 objIn.close();
			 fileIn.close();
		 } catch (ClassNotFoundException | IOException e) {
			 e.printStackTrace();
			 Helper.displayErrorMessage("Error", e.getMessage());
		 }
		 return obj;
	 }
	 
	 public static String convertKeyToString(SecretKey secretKey) {
		 byte[] keyBytes = secretKey.getEncoded();
		 return Base64.getEncoder().encodeToString(keyBytes);
	 }
	 
	 // Convert String to SecretKey
	 public static SecretKey convertStringToKey(String keyString) {
		 byte[] decodedKey = Base64.getDecoder().decode(keyString);
		 return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	 }
	 
	 public static boolean fileExists(String filename) {
		 File file = new File(filename);
		 return file.exists();
	 }
	 
	 public static boolean deleteCredsFile() {
		 File file = new File(LoginController.savedCredentialsFilePath);
		 
		 if(file.exists()) {
			 return(file.delete());
		 }
		 return false;
	 }
	 
	 public void popupForgotPassword(ActionEvent event) {
		 VBox root;
			
			try {
				currentResetPasswordLoader = new FXMLLoader(getClass().getResource(fxmlResetPassword));
				root = currentResetPasswordLoader.load();
				
				stage = new Stage();
				stage.setResizable(false);
				
				stage.setTitle("Reset Password");
				
				//AssetController.setStage(fillFormula);
				scene = new Scene(root);
				scene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
		
				stage.setScene(scene);
				stage.getIcons().add(Main.itAssetLogo);
				
				//make it as a dialog box
				Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(parentStage);
				
				stage.show();	
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	    
	 public void popupSignUp(ActionEvent event) {
		 Parent root;
		 isSigningUp = true;
			try {
				AdminController.currentNewUserLoader = new FXMLLoader(getClass().getResource(AdminController.fxmlNewUser));
				root = AdminController.currentNewUserLoader.load();
				NewUserController controller = (NewUserController)AdminController.currentNewUserLoader.getController();
				controller.setOldUser(null);
				controller.setTitleLabelText("New User");
				
				
				stage = new Stage();
				stage.setResizable(false);
				
					
				scene = new Scene(root);
				scene.getStylesheets().add(this.getClass().getResource("/AdminUi/admin.css").toExternalForm());
				
				stage.setScene(scene);
				stage.getIcons().add(Main.itAssetLogo);
				
				//make it as a dialog box
				Stage parentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(parentStage);
				
				stage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	   
}
