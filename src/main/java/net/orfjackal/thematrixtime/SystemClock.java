package net.orfjackal.thematrixtime;

import java.io.File;
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class SystemClock implements Clock {

    private static final int TIMEVAL_SIZE = 2;
    private static final int TV_SEC = 0;
    private static final int TV_USEC = 1;

    static {
        try {
            System.load(new File("libsystemclock.so").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long time() {
        long[] timeval = new long[TIMEVAL_SIZE];
        int retval = getTimeOfDay(timeval);
        if (retval != 0) {
            throw new RuntimeException("gettimeofday error code: " + retval);
        }
        return timevalToMilliseconds(timeval);
    }

    public void changeTimeTo(long msec) {
        long[] timeval = millisecondsToTimeval(msec);
        int retval = setTimeOfDay(timeval);
        if (retval != 0) {
            throw new RuntimeException("settimeofday error code: " + retval);
        }
    }

    private static native int getTimeOfDay(long[] timeval);

    private static native int setTimeOfDay(long[] timeval);

    private static long timevalToMilliseconds(long[] timeval) {
        long sec = timeval[TV_SEC];
        long usec = timeval[TV_USEC];
        return secondsToMilliseconds(sec) + nanosecondsToMilliseconds(usec);
    }

    private static long[] millisecondsToTimeval(long msec) {
        long sec = millisecondsToSeconds(msec);
        long usec = millisecondsToNanoseconds(msec);
        long[] timeval = new long[TIMEVAL_SIZE];
        timeval[TV_SEC] = sec;
        timeval[TV_USEC] = usec;
        return timeval;
    }

    private static long secondsToMilliseconds(long sec) {
        return (sec * 1000);
    }

    private static long nanosecondsToMilliseconds(long usec) {
        return (usec / 1000) % 1000;
    }

    private static long millisecondsToSeconds(long msec) {
        return msec / 1000;
    }

    private static long millisecondsToNanoseconds(long msec) {
        return (msec % 1000) * 1000;
    }

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
