package net.orfjackal.thematrixtime;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
@RunWith(JDaveRunner.class)
public class AdjustableClockSpec extends Specification<Clock> {

    public class AClockInCurrentTime {

        private DummyClock now;
        private AdjustableClock clock;
        private long beginning;

        public Clock create() {
            now = new DummyClock();
            beginning = now.time();
            clock = new AdjustableClock(now);
            return clock;
        }

        public void shouldShowTheCurrentTime() {
            specify(clock.time(), should.equal(beginning));
        }

        public void shouldIncreaseTheTimeAsTimeProgress() throws InterruptedException {
            now.increaseTimeBy(100);
            specify(clock.time(), should.equal(beginning + 100));
        }
    }

    public class AClockWhoseTimeIsChanged {

        private DummyClock now;
        private AdjustableClock clock;
        private long beginning;

        public Clock create() {
            now = new DummyClock();
            beginning = now.time();
            clock = new AdjustableClock(now);
            clock.changeTimeTo(beginning + 1000);
            return clock;
        }

        public void shouldShowTheChangedTime() {
            specify(clock.time(), should.equal(beginning + 1000));
        }

        public void shouldIncreaseTheTimeAsTimeProgress() throws InterruptedException {
            now.increaseTimeBy(100);
            specify(clock.time(), should.equal(beginning + 1000 + 100));
        }
    }
}
