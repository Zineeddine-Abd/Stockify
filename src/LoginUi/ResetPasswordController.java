package LoginUi;

import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Components.User;
import application.DB_Login;
import application.DB_Users;
import application.Helper;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ResetPasswordController implements Initializable{
	
	User associatedUser;
	
	Timer timer = new Timer();
	@FXML
	private Button confirmEmail;
	@FXML
	private TextField emailAssociatedField;
	@FXML
	private TextField codeField;
	@FXML
	private TextField newPassField;
	@FXML
	private TextField confirmPassField;
	
	@FXML
	private VBox rootVbox;
	@FXML
	private VBox emailVbox;
	@FXML
	private VBox codeVbox;
	@FXML
	private VBox newPassVbox;
	@FXML
	private Label incorrectInfoLabel;
	@FXML
	private Hyperlink resendEmailLink;
	
	private long lastClickTime = 0;
	private int randomNumber;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		rootVbox.getChildren().removeAll(emailVbox,codeVbox,newPassVbox);
		
		rootVbox.heightProperty().addListener((obs, oldVal, newVal) -> {
			Scene mainScene = rootVbox.getScene();
			Stage mainStage = (Stage) mainScene.getWindow();
			mainStage.setHeight(mainStage.getHeight() + newVal.doubleValue() - oldVal.doubleValue());
		});

		confirmEmail.setOnAction(e -> {
	            long currentTime = System.currentTimeMillis();
	            if (currentTime - lastClickTime >= 2000) {
	            	validateEmailInfo();
	                lastClickTime = currentTime;
	            }
	    });
		
		resendEmailLink.setOnAction(e -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime >= 30 * 1000) {
            	resendEmail();
            	Helper.displayInfoMessage("code sent to provided email.", "Code was resent successfully.");
                lastClickTime = currentTime;
            }else {
            	incorrectInfoLabel.setText("Resending Code On Cooldown!");
            	animatedInvalidInfolabel();
            }
		});
		
		rootVbox.getChildren().add(emailVbox);
	}
	
	public void validateEmailInfo() {
		String email = emailAssociatedField.getText();
		
		if(!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
			incorrectInfoLabel.setText("Invalid Email Format!");
			animatedInvalidInfolabel();
			return;
		}
		
		associatedUser = DB_Login.getAssociatedUser(email);
		
		if(associatedUser == null) {
			Helper.displayErrorMessage("Invalid Email!", "the email you provided is not associated with Any Stockify Account!");
			return;
		}
		rootVbox.getChildren().add(codeVbox);
		rootVbox.getChildren().remove(emailVbox);
		
		sendEmailTo(email);
	}
	
	private void sendEmailTo(String to) {
        String from = "stockifyteams@gmail.com";
        String password = "espj hpgj bgsg citg";
        
        String host = "smtp.gmail.com";
        int port = 587; //gmail port.

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Reset Password Completion");
            
            Random random = new Random();
            randomNumber = random.nextInt(900000) + 100000;
            
            String msg = "Dear, "+associatedUser.getFirst_name() + "\n\nHere's the code for Resetting your password: " 
            + randomNumber + 
            "\nif you wish to get a new code , you can request a new one from Stockify.\n\nif you did not request to reset your password, please ignore this email. + \n\nBest Regards,\nStockifyTeams.";
            
            message.setText(msg);
            Transport.send(message);
        } catch(MessagingException mex){
            Helper.displayErrorMessage("Invalid Email", "The email associated with your account is either invalid or does not exist!\nContact your administrator to change your email to a valid one.");
        }
	}
	
	public void validateCode() {
		try {
			int retrieveCode = Integer.parseInt(codeField.getText());
			if(codeField.getText().isBlank()) {
				throw new NumberFormatException();
			}
			
			if(retrieveCode == randomNumber) {
				rootVbox.getChildren().add(newPassVbox);
				
				rootVbox.getChildren().remove(codeVbox);
				rootVbox.getChildren().remove(incorrectInfoLabel);
				
			}else {
				incorrectInfoLabel.setText("Invalid Code!");
				animatedInvalidInfolabel();
				return;
			}
		}catch(NumberFormatException e) {
			incorrectInfoLabel.setText("Invalid Code!");
			animatedInvalidInfolabel();
			return;
		}
	}
	
	public void resendEmail() {
		String email = associatedUser.getEmail();
		sendEmailTo(email);
	}
	
	public void confirmPassword(ActionEvent event) {
		String pass = newPassField.getText();
		String confirmPass = confirmPassField.getText();
		
		if(!pass.equals(confirmPass)) {
			incorrectInfoLabel.setText("Passwords Do not match!");
			animatedInvalidInfolabel();
			return;
		}
		String newHashedPassword = LoginController.hashPassword(confirmPass);
		String newHashedPasswordSalt = LoginController.getHashedPasswordSalt();
		
		User updatedUser = new User(associatedUser.getUser_id(), associatedUser.getUsername(),newHashedPassword ,associatedUser.getEmail(), associatedUser.getFirst_name(), associatedUser.getLast_name(), associatedUser.getUser_role(),newHashedPasswordSalt);
		DB_Users.updateUser(null,associatedUser, updatedUser);
		disposeWindow(event);
	}
	
	
	public void animatedInvalidInfolabel() {
		FadeTransition fadetransition = new FadeTransition(Duration.seconds(2),incorrectInfoLabel);
		fadetransition.setFromValue(1);
		fadetransition.setToValue(0);
		fadetransition.play();
	}
	public void disposeWindow(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

}
