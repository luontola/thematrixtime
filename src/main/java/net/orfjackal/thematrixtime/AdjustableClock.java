package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class AdjustableClock implements Clock {

    private long offset;
    private Clock readOnlyClock;

    public AdjustableClock(Clock readOnlyClock) {
        this.readOnlyClock = readOnlyClock;
    }

    public long time() {
        return readOnlyClock.time() + offset;
    }

    public void changeTimeTo(long newTime) {
        long oldTime = time();
        long change = newTime - oldTime;
        offset += change;
    }
}
