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

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.println("The Matrix Time");
        System.out.println("Copyright (c) 2007 Esko Luontola, www.orfjackal.net");
        System.out.println();
        System.out.print("Desired time speedup ratio: ");

        double ratio = in.nextDouble();
        TimeCompressor compressor = new TimeCompressor(new AdjustableClock(new ReadOnlySystemClock()), ratio);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        while (true) {
            Date systemTime = new Date(compressor.systemTime());
            Date realWorldTime = new Date(compressor.realWorldTime());
            System.out.println("SYSTEM TIME      REAL TIME");
            System.out.println(timeFormat.format(systemTime) + "         " + timeFormat.format(realWorldTime));
            for (int i = 0; i < 100; i++) {
                Thread.sleep(10);
                compressor.synchronizeClock();
            }
        }
    }
}
