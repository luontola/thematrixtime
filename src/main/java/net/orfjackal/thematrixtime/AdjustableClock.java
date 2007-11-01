package net.orfjackal.thematrixtime;

import org.jetbrains.annotations.NotNull;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public class AdjustableClock implements Clock {

    private long offset;
    private Clock clock;

    public AdjustableClock(@NotNull Clock clock) {
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
