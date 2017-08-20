package app.tools;

import app.WorkController;

import java.sql.Timestamp;
import java.util.TimerTask;

public class CheckScreenSaverIsActiveTask extends TimerTask {

    @Override
    public void run() {
        WorkController.setScreenSaverIsActive(Checker.isScreenSaverActive());
        System.out.println(new Timestamp(System.currentTimeMillis()));
    }
}
