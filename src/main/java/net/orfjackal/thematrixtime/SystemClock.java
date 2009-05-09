package net.orfjackal.thematrixtime;

import java.io.File;
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class SystemClock implements Clock {

    static {
        try {
            System.load(new File("libsystemclock.so").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long time() {
        long[] secUsec = new long[2];
        int retval = getTimeOfDay(secUsec);
        if (retval != 0) {
            throw new RuntimeException("gettimeofday error code: " + retval);
        }
        long sec = secUsec[0];
        long usec = secUsec[1];
        return (sec * 1000) + (usec / 1000) % 1000;
    }

    public void changeTimeTo(long msec) {
        long sec = msec / 1000;
        long usec = (msec % 1000) * 1000;
        long[] secUsec = new long[]{sec, usec};
        int retval = setTimeOfDay(secUsec);
        if (retval != 0) {
            throw new RuntimeException("settimeofday error code: " + retval);
        }
    }

    private static native int getTimeOfDay(long[] secUsec);

    private static native int setTimeOfDay(long[] secUsec);

    public static void main(String[] args) {
        Clock clock = new SystemClock();
        for (int i = 0; i < 10; i++) {
            long time = clock.time();
            System.out.println("clock.getTime() = " + time);
            System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
            System.out.println("Reduced time by 500 ms");
            clock.changeTimeTo(time - 500);
            System.out.println("---------");
        }
        System.out.println("clock.getTime() = " + clock.time());
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
    }
}
