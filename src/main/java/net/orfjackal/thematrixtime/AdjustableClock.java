package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class AdjustableClock implements Clock {

    private long offset;
    private Clock clock;

    public AdjustableClock(Clock clock) {
        this.clock = clock;
    }

    public long time() {
        return clock.time() + offset;
    }

    public void changeTimeTo(long newTime) {
        long oldTime = time();
        long change = newTime - oldTime;
        offset += change;
    }
}
