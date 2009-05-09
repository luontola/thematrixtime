package net.orfjackal.thematrixtime;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
@RunWith(JDaveRunner.class)
public class TimeCompressorSpec extends Specification<TimeCompressor> {

    public class TimeCompressorWithNoSpeedup {

        private TimeCompressor compressor;
        private DummyClock now;
        private long beginning;

        public TimeCompressor create() {
            now = new DummyClock();
            beginning = now.time();
            compressor = new TimeCompressor(now, 1.0);
            now.increaseTimeBy(100);
            return compressor;
        }

        public void shouldIncreaseTargetTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.targetTime(), should.equal(beginning + 100));
        }

        public void shouldIncreaseSystemTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.systemTime(), should.equal(beginning + 100));
        }
    }

    public class TimeCompressorWith100PercentSpeedup {

        private TimeCompressor compressor;
        private DummyClock now;
        private long beginning;

        public TimeCompressor create() {
            now = new DummyClock();
            beginning = now.time();
            compressor = new TimeCompressor(now, 2.0);
            now.increaseTimeBy(100);
            return compressor;
        }

        public void shouldIncreaseTargetTimeAtDoubleSpeed() throws InterruptedException {
            specify(compressor.targetTime(), should.equal(beginning + 200));
        }

        public void shouldIncreaseSystemTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.systemTime(), should.equal(beginning + 100));
        }
    }

    public class TimeCompressorWith50PercentSlowdown {

        private TimeCompressor compressor;
        private DummyClock now;
        private long beginning;

        public TimeCompressor create() {
            now = new DummyClock();
            beginning = now.time();
            compressor = new TimeCompressor(now, 0.5);
            now.increaseTimeBy(100);
            return compressor;
        }

        public void shouldIncreaseTargetTimeAtHalfSpeed() throws InterruptedException {
            specify(compressor.targetTime(), should.equal(beginning + 50));
        }

        public void shouldIncreaseSystemTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.systemTime(), should.equal(beginning + 100));
        }
    }

    public class WhenTheSystemClockIsSynchronized {

        private TimeCompressor compressor;
        private DummyClock now;
        private long beginning;

        public TimeCompressor create() {
            now = new DummyClock();
            beginning = now.time();
            compressor = new TimeCompressor(now, 0.5);
            now.increaseTimeBy(100);
            compressor.synchronizeClock();
            return compressor;
        }

        public void theClockShouldBeUpdatedToTheExpectedTime() {
            specify(now.time(), should.equal(beginning + 50));
        }

        public void theTimeShouldProgressNormallyAfterwards() {
            now.increaseTimeBy(100);
            specify(now.time(), should.equal(beginning + 50 + 100));
        }
    }
}
