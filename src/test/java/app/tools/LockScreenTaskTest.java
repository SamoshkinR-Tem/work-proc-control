package app.tools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LockScreenTaskTest {

    private boolean os;

    @Before
    public void before() {
        String operatingSystem = System.getProperty("os.name");
        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) os = true;
        else if ("Windows".equals(operatingSystem)) os = false;
    }

    @Test
    public void doAction() throws Exception {
        new LockScreenTask().doAction(LockScreenTask.LOCK_SCREEN);
    }

    @Test
    public void getCommand() throws Exception {
        if (os) assertEquals(LockScreenTask.LOCK_SCREEN_LIN,
                new LockScreenTask().getCommand(os, LockScreenTask.LOCK_SCREEN));
        else assertEquals(LockScreenTask.LOCK_SCREEN_WIN,
                new LockScreenTask().getCommand(os, LockScreenTask.LOCK_SCREEN));
    }

    @Test
    public void checkOS() throws Exception {
        if (os) assertEquals(LockScreenTask.LINUX_OR_MAC, new LockScreenTask().checkOS());
        else assertEquals(LockScreenTask.WINDOWS, new LockScreenTask().checkOS());
    }

}