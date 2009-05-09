package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class DefaultClock implements Clock {

    public long time() {
        return System.currentTimeMillis();
    }

    public void changeTimeTo(long msec) {
        throw new UnsupportedOperationException();
    }
}
