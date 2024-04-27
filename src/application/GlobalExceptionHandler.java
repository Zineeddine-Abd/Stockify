package application;




import java.util.logging.Level;
import java.util.logging.Logger;

import LoginUi.LoginController;
import application.DB_Sessions;
import application.Helper;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
	
    @Override
    public void uncaughtException(Thread t, Throwable e) {
    	e.printStackTrace();
    	logger.log(Level.SEVERE,"An Error Occured and the Application needs to quit!");
    	Helper.displayErrorMessage("An Error Occured and the Application needs to quit!\n Details: ", e.getStackTrace().toString() + "\nIn Thread: " + t.getName());
        DB_Sessions.terminateCurrentSession(LoginController.getLoggedUser().getUser_id());
        System.exit(1);
    }
}