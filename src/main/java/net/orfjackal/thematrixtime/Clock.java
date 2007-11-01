package net.orfjackal.thematrixtime;

/**
 * @author Esko Luontola
 * @since 1.11.2007
 */
public interface Clock {

    long time();

    void changeTimeTo(long msec);
}
