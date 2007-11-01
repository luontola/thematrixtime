package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TimeCompressor {

    private Clock clock;
    private double speedup;
    private long startup;
    private long realWorldTimeOffset = 0;

    public TimeCompressor(Clock clock, double speedup) {
        this.clock = clock;
        this.speedup = speedup;
        startup = clock.time();
    }

    public void synchronizeClock() {
        long before = clock.time();
        long after = targetTime();
        clock.changeTimeTo(after);
        realWorldTimeOffset += (before - after);
    }

    public long realWorldTime() {
        return systemTime() + realWorldTimeOffset;
    }

    public long systemTime() {
        return startup + systemTimeElapsed();
    }

    public long targetTime() {
        return startup + targetTimeElapsed();
    }

    private long realWorldTimeElapsed() {
        return realWorldTime() - startup;
    }

    private long systemTimeElapsed() {
        return clock.time() - startup;
    }

    private long targetTimeElapsed() {
        return (long) (realWorldTimeElapsed() * speedup);
    }
}
