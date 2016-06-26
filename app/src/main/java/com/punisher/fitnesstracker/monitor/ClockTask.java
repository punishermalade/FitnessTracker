package com.punisher.fitnesstracker.monitor;

import android.widget.TextView;

/**
 * A task that runs at a fixed interval to increment the Clock tick counter
 */
public class ClockTask implements Runnable {

    private TextView mClockView = null;
    private ClockManager mGameClock = null;

    public ClockTask(ClockManager c) {
        mGameClock = c;
    }

    public void setTextView(TextView v) {
        mClockView = v;
    }

    @Override
    public void run() {
        handleTick();
    }

    public TextView getClockView() {
        return mClockView;
    }

    public ClockManager getGameClockManager() {
        return mGameClock;
    }

    public void handleTick() {
        mGameClock.handleTick(this);
    }

}
