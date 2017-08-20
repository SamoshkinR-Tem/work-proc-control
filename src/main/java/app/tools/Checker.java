package app.tools;

import app.WorkController;

import java.io.*;

public class Checker {

  /*
   * Pipes are a shell feature, so you have to open a shell first.
   *
   * You could use process.getInputStream() to read the output and parse it.
   *
   * For productive use i would prefer using the Inputstream.
   */

    private static final String COMMAND_IS_SS_ACTIVE =
            "gnome-screensaver-command -q |  grep -q 'is active'";
    private static final String COMMAND_IS_USER_LOGGED =
            "who";

    private static final int EXPECTED_EXIT_CODE = 0;
    private static final String ACTIVE = "включёна";

    public static boolean isScreenSaverActive() {
        boolean is_active = false;
        final Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(COMMAND_IS_SS_ACTIVE);
            String output[] = readTheLsOutput(p);
            for (String s : output) if (s.contains(ACTIVE)) is_active = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(is_active);
        return is_active;
    }

    boolean isUserLoggedIn(String userName) {
        boolean isLogged = false;
        final Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(COMMAND_IS_USER_LOGGED);
            String output[] = readTheLsOutput(p);
            for (String s : output) if (s.contains(WorkController.ARTEM)) isLogged = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return isLogged;

    }

    private static String[] readTheLsOutput(Process p) {
        InputStream in = p.getInputStream();
        BufferedInputStream buf = new BufferedInputStream(in);
        InputStreamReader inRead = new InputStreamReader(buf);
        BufferedReader bufferedreader = new BufferedReader(inRead);

        // Read the ls output
        String lines[] = new String[0];
        String tmp[] = new String[0];
        String line;
        int pos = 0;
        try {
            while ((line = bufferedreader.readLine()) != null) {
                if (lines.length != 0) System.arraycopy(lines, 0, tmp, 0, lines.length);
                lines = new String[pos + 1];
                System.arraycopy(tmp, 0, lines, 0, tmp.length);
                lines[pos] = line;
                pos++;
            }

            // Check for ls failure
            try {
                if (p.waitFor() != EXPECTED_EXIT_CODE) {
                    System.err.println("exit value = " + p.exitValue());
                }
            } catch (InterruptedException e) {
                System.err.println(e);
            } finally {
                // Close the InputStream
                bufferedreader.close();
                inRead.close();
                buf.close();
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}