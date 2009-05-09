package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class TimeCompressor {

    private Clock clock;
    private double speedup;
    private long startup;

    public TimeCompressor(Clock clock, double speedup) {
        this.clock = clock;
        this.speedup = speedup;
        startup = clock.time();
    }

    public void synchronizeClock() {
        clock.changeTimeTo(targetTime());
    }

    public long systemTime() {
        return startup + systemTimeElapsed();
    }

    public long targetTime() {
        return startup + targetTimeElapsed();
    }

    private long systemTimeElapsed() {
        return clock.time() - startup;
    }

    private long targetTimeElapsed() {
        return (long) (systemTimeElapsed() * speedup);
    }
}
