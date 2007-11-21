package net.orfjackal.thematrixtime;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;
import org.jmock.Expectations;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
@RunWith(JDaveRunner.class)
public class TimeCompressorSpec extends Specification<TimeCompressor> {

    public class TimeCompressorRunningAtNormalSpeed {

        private TimeCompressor compressor;
        private long beginning;

        public TimeCompressor create() {
            DummyClock now = new DummyClock();
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

        public void shouldIncreaseRealWorldTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.realWorldTime(), should.equal(beginning + 100));
        }
    }

    public class TimeCompressorRunningAtDoubleSpeed {

        private TimeCompressor compressor;
        private long beginning;

        public TimeCompressor create() {
            DummyClock now = new DummyClock();
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

        public void shouldIncreaseRealWorldTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.realWorldTime(), should.equal(beginning + 100));
        }
    }

    public class TimeCompressorRunningAtHalfSpeed {

        private TimeCompressor compressor;
        private long beginning;

        public TimeCompressor create() {
            DummyClock now = new DummyClock();
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

        public void shouldIncreaseRealWorldTimeAtNormalSpeed() throws InterruptedException {
            specify(compressor.realWorldTime(), should.equal(beginning + 100));
        }
    }

    public class ClockSynchronization {

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

        public void shouldUpdateTheClock() {
            specify(now.time(), should.equal(beginning + 50));
        }

        public void shouldNotChangeTargetTime() {
            specify(compressor.targetTime(), should.equal(beginning + 50));
        }

        public void shouldChangeSystemTimeToTargetTime() {
            specify(compressor.systemTime(), should.equal(beginning + 50));
        }

        public void shouldNotChangeRealWorldTime() {
            specify(compressor.realWorldTime(), should.equal(beginning + 100));
        }

        public void afterwardsShouldIncreaseTargetTimeAtModifiedSpeed() throws InterruptedException {
            now.increaseTimeBy(100);
            specify(compressor.targetTime(), should.equal(beginning + 50 + 50));
        }

        public void afterwardsShouldIncreaseSystemTimeAtNormalSpeed() throws InterruptedException {
            now.increaseTimeBy(100);
            specify(compressor.systemTime(), should.equal(beginning + 50 + 100));
        }

        public void afterwardsShouldIncreaseRealWorldTimeAtNormalSpeed() throws InterruptedException {
            now.increaseTimeBy(100);
            specify(compressor.realWorldTime(), should.equal(beginning + 100 + 100));
        }
    }

    public class WhenNoTimeHasPassedSinceTheLastSynchronization {

        private TimeCompressor compressor;
        private Clock now;

        public TimeCompressor create() {
            now = mock(Clock.class);
            checking(new Expectations() {{
                one(now).time(); will(returnValue(0L));
                one(now).changeTimeTo(0L);
                allowing(now).time(); will(returnValue(1L));
            }});
            compressor = new TimeCompressor(now, 0.5);
            compressor.synchronizeClock();
            return compressor;
        }

        public void shouldNotSynchronizeOnTheSecondRun() {
            // expectations in create()
            compressor.synchronizeClock();
        }
    }
}
