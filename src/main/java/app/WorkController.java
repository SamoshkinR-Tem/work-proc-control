package app;

import app.tools.CheckScreenSaverIsActiveTask;
import app.tools.LockScreenTask;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;

public class WorkController {

    private static final String START = "Start";
    private static final Long WORK_INTERVAL = 5000000L; // 50 min

    private static boolean wasActive = false;

    private static WorkController wc;
    private Timer timer;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm");
    private static final Logger logger = Logger.getLogger(WorkController.class);

    public static void main(String[] args) {
        logger.info("main()");

        wc = new WorkController(START);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("      --- Hi! This is Your Work Controller! ---\n" +
                "        -----It is " + sdf.format(timestamp) + " now -----\n" +
                "   ==== We will activate ScreenSaver in 50 min ====\n" +
                "      === Have an awesome day and be happy! ===");
    }

    private WorkController(String s) {
        logger.info("WorkController() param: " + s);

        if (s.matches(START)) startLockScreenTimer(WORK_INTERVAL);
    }

    private void startLockScreenTimer(Long delay) {
        logger.info("startLockScreenTimer() delay: " + delay);

        if (timer != null) timer.cancel();

        // re-schedule timer here otherwise, IllegalStateException of
        // "TimerTask is scheduled already" will be thrown
        timer = new Timer();
        LockScreenTask lockScreenTask = new LockScreenTask();

        // delay 50min
        // if U wont to repeat in 5s add 5000 as the 3d param
        timer.schedule(lockScreenTask, delay);
    }

    public static void onScreenSaverStarted(boolean isStarted) {
        logger.info("onScreenSaverStarted() started: " + isStarted);
        wc.startCheckerTimer();
    }

    private void startCheckerTimer() {
        logger.info("startCheckerTimer()");
        if (timer != null) timer.cancel();
        timer = new Timer();
        CheckScreenSaverIsActiveTask checkScreenSaverIsActiveTask = new CheckScreenSaverIsActiveTask();
        timer.schedule(checkScreenSaverIsActiveTask, 15000, 10000);
    }

    public static void makeChoice(boolean isActive) {
        logger.info("makeChoice() wasActive: " + wasActive +
                " become: " + isActive);
        if ((!wasActive && !isActive) || (wasActive && !isActive)) wc.startLockScreenTimer(WORK_INTERVAL);
        wasActive = isActive;
    }
}
