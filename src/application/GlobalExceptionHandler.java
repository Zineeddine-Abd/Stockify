package application;




import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
	
    @Override
    public void uncaughtException(Thread t, Throwable e) {
    	e.printStackTrace();
    	logger.log(Level.SEVERE,"An Error Occured and the Application needs to quit!");
    	Helper.displayErrorMessage("An Error Occured and the Application needs to quit!","Details: " + e.getStackTrace().toString() + "\nIn Thread: " + t.getName());
        Helper.closeResources();
        System.exit(1);
    }
}