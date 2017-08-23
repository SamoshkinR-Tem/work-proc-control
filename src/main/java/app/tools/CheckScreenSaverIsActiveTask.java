package app.tools;

import app.WorkController;

import java.util.TimerTask;

public class CheckScreenSaverIsActiveTask extends TimerTask {

    @Override
    public void run() {
        WorkController.makeChoice(Checker.isScreenSaverActive());
    }
}
