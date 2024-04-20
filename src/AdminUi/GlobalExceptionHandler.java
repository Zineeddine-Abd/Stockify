package AdminUi;

import application.Helper;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Helper.displayErrorMessage("Exception thrown!", e.getMessage());
    }

}