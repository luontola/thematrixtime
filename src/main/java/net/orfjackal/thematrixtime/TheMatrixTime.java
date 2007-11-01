package net.orfjackal.thematrixtime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TheMatrixTime {

    private static final String APP_NAME = "The Matrix Time";
    private static final String APP_VERSION = "1.0 (2007-11-02)";
    private static final String COPYRIGHT = "Copyright (c) 2007 Esko Luontola, www.orfjackal.net";

    private static final int PRINT_TIME_INTERVAL = 2000;
    private static final int WAIT_BETWEEN_SYNC = 50;

    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void main(String[] args) throws InterruptedException {
        System.out.println(APP_NAME + " " + APP_VERSION);
        System.out.println(COPYRIGHT);
        System.out.println();
        System.out.print("Desired time speedup ratio: ");

        double ratio = askSpeedupRatio();
        Clock clock = availableClock();
        TimeCompressor compressor = new TimeCompressor(clock, ratio);

        System.out.println();
        System.out.println("SYSTEM TIME      REAL TIME");
        while (true) {
            printCurrentTime(compressor);
            synchronizeAndWait(compressor);
        }
    }

    private static void printCurrentTime(TimeCompressor compressor) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        Date systemTime = new Date(compressor.systemTime());
        Date realWorldTime = new Date(compressor.realWorldTime());
        System.out.println(timeFormat.format(systemTime) + "     " + timeFormat.format(realWorldTime));
    }

    private static void synchronizeAndWait(TimeCompressor compressor) throws InterruptedException {
        int syncSteps = PRINT_TIME_INTERVAL / WAIT_BETWEEN_SYNC;
        for (int i = 0; i < syncSteps; i++) {
            Thread.sleep(WAIT_BETWEEN_SYNC);
            compressor.synchronizeClock();
        }
    }

    private static double askSpeedupRatio() {
        Scanner in = new Scanner(System.in);
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
                System.out.print("Not a number, enter a decimal number: ");
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
}
