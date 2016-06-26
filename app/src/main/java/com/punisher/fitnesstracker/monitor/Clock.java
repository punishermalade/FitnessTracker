package com.punisher.fitnesstracker.monitor;

/**
 * simple clock that hold elapsed time and increment it when tick() is called
 */
public class Clock {

    private long mIntervalMs = 1000; // one second by default
    private long mElapsed = 0;

    public void tick() {
        mElapsed += mIntervalMs;
    }

    public void reset() {
        setElapsedTime(0);
    }

    public void setElapsedTime(long t) {
        mElapsed = t;
    }

    public long getElapsed() {
        return mElapsed;
    }

    public void setIntervalMs(long i) {
        mIntervalMs = i;
    }

    public long getIntervalMs() {
        return mIntervalMs;
    }
}