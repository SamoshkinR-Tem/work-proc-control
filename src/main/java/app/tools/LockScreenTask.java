package app.tools;

import app.WorkController;

import java.io.IOException;

public class LockScreenTask extends java.util.TimerTask {

    static final int LOCK_SCREEN = 0;
    static final int SHUTDOWN = 1;
    static final boolean LINUX_OR_MAC = true;
    static final boolean WINDOWS = false;
    static final String LOCK_SCREEN_LIN = "gnome-screensaver-command -l";
    static final String LOCK_SCREEN_WIN = "rundll32.exe user32.dll,LockWorkStation";
    private static final String SHUTDOWN_LIN = "shutdown -h now";
    private static final String SHUTDOWN_WIN = "shutdown.exe -s -t 0";
    private static final String MESSAGE = "Unsupported operating system.";

    @Override
    public void run() {
        try {
            if (doAction(LOCK_SCREEN)) {
                WorkController.onScreenSaverStarted();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean doAction(int action) throws RuntimeException, IOException {
        boolean os = checkOS();
        Runtime r = Runtime.getRuntime();
        String command = getCommand(os, action);

        try {
            r.exec(command);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkOS() {
        String operatingSystem = System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            return LINUX_OR_MAC;
        } else if ("Windows".equals(operatingSystem)) {
            return WINDOWS;
        } else {
            throw new RuntimeException(MESSAGE);
        }
    }

    public String getCommand(boolean os, int action) {
        String command;
        switch (action) {
            case SHUTDOWN:
                if (os) command = SHUTDOWN_LIN;
                else command = SHUTDOWN_WIN;
                return command;
            case LOCK_SCREEN:
                if (os) command = LOCK_SCREEN_LIN;
                else command = LOCK_SCREEN_WIN;
                return command;
            default:
                return MESSAGE;
        }
    }
}