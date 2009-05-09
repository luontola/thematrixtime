package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class DummyClock implements Clock {

    private long time = 0;

    public long time() {
        return time;
    }

    public void changeTimeTo(long msec) {
        time = msec;
    }

    public void increaseTimeBy(int msec) {
        time += msec;
    }
}
