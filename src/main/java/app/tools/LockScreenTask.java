package app.tools;

import app.WorkController;
import org.apache.log4j.Logger;

import java.io.IOException;

public class LockScreenTask extends java.util.TimerTask {

    private static final int EXPECTED_EXIT_CODE = 0;
    private static final long FIVE_SEC = 5000L;
    static final int SHUTDOWN = 1;
    static final int LOCK_SCREEN = 0;
    static final boolean LINUX_OR_MAC = true;
    static final boolean WINDOWS = false;
    static final String LOCK_SCREEN_LIN = "gnome-screensaver-command -l";
    static final String LOCK_SCREEN_WIN = "rundll32.exe user32.dll,LockWorkStation";
    private static final String SHUTDOWN_LIN = "shutdown -h now";
    private static final String SHUTDOWN_WIN = "shutdown.exe -s -t 0";
    private static final String MESSAGE = "Unsupported operating system.";

    private static final Logger logger = Logger.getLogger(LockScreenTask.class);

    @Override
    public void run() {
        try {
            if (doAction(LOCK_SCREEN)) {
                WorkController.onScreenSaverStarted(true);
            } else {
                System.out.println("Ups! Something went wrong! :(");
                WorkController.onScreenSaverStarted(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean doAction(int action) throws RuntimeException, IOException {
        switch (action) {
            case LOCK_SCREEN:
                logger.info("doAction() action: LOCK_SCREEN");
                break;
            case SHUTDOWN:
                logger.info("doAction() action: SHUTDOWN");
                break;
        }
        boolean os = checkOS();
        Runtime r = Runtime.getRuntime();
        String command = getCommand(os, action);

        try {
            Process p = r.exec(command);
            try {
                return p.waitFor() == EXPECTED_EXIT_CODE;
            } catch (InterruptedException e) {
                System.err.println("exit value = " + p.exitValue());
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean checkOS() {
        String osName = System.getProperty("os.name");
        logger.info("checkOS() os: " + osName);

        if (osName.startsWith("Linux") || osName.startsWith("Mac")) {
            return LINUX_OR_MAC;
        } else if (osName.startsWith("Win")) {
            return WINDOWS;
        } else {
            throw new RuntimeException(MESSAGE);
        }
    }

    String getCommand(boolean os, int action) {
        String command;
        switch (action) {
            case SHUTDOWN:
                if (os) command = SHUTDOWN_LIN;
                else command = SHUTDOWN_WIN;
                break;
            case LOCK_SCREEN:
                if (os) command = LOCK_SCREEN_LIN;
                else command = LOCK_SCREEN_WIN;
                break;
            default:
                command = MESSAGE;
        }
        logger.info("getCommand() command: " + command);
        return command;
    }
}
