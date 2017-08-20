package app.tools;

import org.junit.Test;

import java.io.*;

public class CheckerTest {

    @Test
    public void isScreenSaverActive() throws IOException, InterruptedException {
        new LockScreenTask().doAction(LockScreenTask.LOCK_SCREEN);
        Runtime.getRuntime().exec("gnome-screensaver-command -a");
        CheckerTest sst = new CheckerTest();
        synchronized (sst) {
            sst.wait(3000);
        }
        System.out.println(Checker.isScreenSaverActive());
    }
}