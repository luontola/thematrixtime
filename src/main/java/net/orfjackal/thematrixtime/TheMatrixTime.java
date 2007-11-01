package net.orfjackal.thematrixtime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TheMatrixTime {

    @SuppressWarnings({"InfiniteLoopStatement"})
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.println("The Matrix Time");
        System.out.println("Copyright (c) 2007 Esko Luontola, www.orfjackal.net");
        System.out.println();
        System.out.print("Desired time speedup ratio: ");

        double ratio = in.nextDouble();
//        Clock clock = new AdjustableClock(new ReadOnlySystemClock());
        Clock clock = new SystemClock();
        TimeCompressor compressor = new TimeCompressor(clock, ratio);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

        System.out.println("SYSTEM TIME      REAL TIME");
        while (true) {
            Date systemTime = new Date(compressor.systemTime());
            Date realWorldTime = new Date(compressor.realWorldTime());
            System.out.println(timeFormat.format(systemTime) + "     " + timeFormat.format(realWorldTime));
            for (int i = 0; i < 20; i++) {
                Thread.sleep(50);
                compressor.synchronizeClock();
            }
        }
    }
}
