package net.orfjackal.thematrixtime;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TheMatrixTime {

    private static final String APP_NAME = "The Matrix Time";
    private static final String COPYRIGHT = "Copyright (c) 2007 Esko Luontola, www.orfjackal.net";

    private static final int PRINT_TIME_INTERVAL = 2000;

    private static final Scanner in = new Scanner(System.in);

    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void main(String[] args) throws InterruptedException {
        System.out.println(APP_NAME + " " + appVersion());
        System.out.println(COPYRIGHT);
        System.out.println();

        double ratio = askSpeedupRatio();
        int waitTime = askWaitBetweenSync();
        Clock clock = availableClock();
        TimeCompressor compressor = new TimeCompressor(clock, ratio);

        System.out.println();
        System.out.println("SYSTEM TIME      REAL TIME");
        while (true) {
            // TODO: run this in its own thread and allow input from user to stop the process, or to change the configuration
            printCurrentTime(compressor);
            synchronizeAndWait(compressor, waitTime);
        }
    }

    private static void printCurrentTime(TimeCompressor compressor) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        Date systemTime = new Date(compressor.systemTime());
        Date realWorldTime = new Date(compressor.realWorldTime());
        System.out.println(timeFormat.format(systemTime) + "     " + timeFormat.format(realWorldTime));
    }

    private static void synchronizeAndWait(TimeCompressor compressor, int waitBetweenSync) throws InterruptedException {
        int syncSteps = PRINT_TIME_INTERVAL / waitBetweenSync;
        for (int i = 0; i < syncSteps; i++) {
            Thread.sleep(waitBetweenSync);
            compressor.synchronizeClock();
        }
    }

    private static double askSpeedupRatio() {
        System.out.print("Desired time speedup ratio: ");
        while (true) {
            try {
                double ratio = in.nextDouble();
                if (ratio <= 0.0) {
                    System.out.print("Enter a decimal number larger than zero: ");
                    continue;
                }
                return ratio;

            } catch (InputMismatchException e) {
                in.next(); // discard invalid token
                System.out.print("Not a number, retry: ");
            }
        }
    }

    private static int askWaitBetweenSync() {
        System.out.print("Wait time between time sync in milliseconds: ");
        while (true) {
            try {
                int waitTime = in.nextInt();
                if (waitTime <= 0) {
                    System.out.print("Enter an integer larger than zero: ");
                    continue;
                }
                return waitTime;

            } catch (InputMismatchException e) {
                in.next(); // discard invalid token
                System.out.print("Not a number, retry: ");
            }
        }
    }

    private static Clock availableClock() {
        try {
            return new SystemClock();

        } catch (UnsatisfiedLinkError e) {
            System.err.println();
            System.err.println("ERROR: " + e.getMessage());
            System.err.println("       Falling back to a pseudo clock which does not change the system time");
            return new AdjustableClock(new ReadOnlySystemClock());
        }
    }

    private static String appVersion() {
        InputStream pom = TheMatrixTime.class
                .getResourceAsStream("/META-INF/maven/net.orfjackal.thematrixtime/thematrixtime/pom.properties");
        if (pom == null) {
            return "";
        }
        try {
            Properties p = new Properties();
            p.load(pom);
            return p.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
