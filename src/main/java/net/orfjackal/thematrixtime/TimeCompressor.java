package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TimeCompressor {

    private Clock clock;
    private double speedupRatio;
    private long startup;
    private long realWorldTimeOffset = 0;

    public TimeCompressor(Clock clock, double speedupRatio) {
        this.clock = clock;
        this.speedupRatio = speedupRatio;
        startup = clock.time();
    }

    public void synchronizeClock() {
        // TODO: try to optimize the update process so that it does not hang other apps
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
        return (long) (realWorldTimeElapsed() * speedupRatio);
    }
}
