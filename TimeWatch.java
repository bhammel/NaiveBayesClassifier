/*
 * File: TimeWatch.java
 * Author: Brandon Hammel
 * Class: CS 165A, Winter 2016
 * Assignment: Machine Problem 1
 */

import java.util.concurrent.TimeUnit;

public class TimeWatch {
    private long starts;

    private TimeWatch() {
        reset();
    }

    public static TimeWatch start() {
        return new TimeWatch();
    }

    public TimeWatch reset() {
        starts = System.nanoTime();
        return this;
    }

    public long time() {
        long ends = System.nanoTime();
        return ends - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.NANOSECONDS);
    }

    public String toMinuteSeconds() {
        return String.format("%d min, %d sec", time(TimeUnit.MINUTES), time(TimeUnit.SECONDS) - time(TimeUnit.MINUTES));
    }
}
