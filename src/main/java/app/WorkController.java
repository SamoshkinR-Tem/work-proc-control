package app;

import app.tools.CheckScreenSaverIsActiveTask;
import app.tools.LockScreenTask;

import java.util.Timer;

public class WorkController {

    private static final String START = "Start";
    private static final Long WORK_INTERVAL = 5000000L;
    public static final String ARTEM = "artem";

    private static boolean wasActive = false;

    private static WorkController wc;
    private Timer timer;

    public static void main(String[] args) {
        wc = new WorkController(START);
        System.out.println("Hi! It is Your Work Controller!" +
                "\nHave an awesome day and be happy!");
    }

    private WorkController(String s) {
        if (s.matches(START)) startTimer(WORK_INTERVAL);
    }

    private void startTimer(Long delay) {
        if (timer != null) timer.cancel();

        // re-schedule timer here otherwise, IllegalStateException of
        // "TimerTask is scheduled already" will be thrown
        timer = new Timer();
        LockScreenTask lockScreenTask = new LockScreenTask();

        // delay 50min
        // if U wont to repeat in 5s add 5000 as the 3d param
        timer.schedule(lockScreenTask, delay);
    }

    public static void onScreenSaverStarted() {
        wc.startChecker();
    }

    private void startChecker() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        CheckScreenSaverIsActiveTask checkScreenSaverIsActiveTask = new CheckScreenSaverIsActiveTask();
        timer.schedule(checkScreenSaverIsActiveTask, 10000, 10000);
    }

    public static void setScreenSaverIsActive(boolean isActive) {
        if ((!wasActive && !isActive) || (wasActive && !isActive)) wc.startTimer(WORK_INTERVAL);
        else wasActive = true;
    }
}
