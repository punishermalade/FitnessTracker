package com.punisher.fitnesstracker.monitor;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class manages a Clock by providing start and stop functions. The ticks are handled
 * by a background thread. An optional TextView can be attached to display the current elapsed
 * time.
 */
public class ClockManager {

    /**
     * Represents the handler to modify the TextView on the UI thread
     */
    private Handler mHandler = null;

    /**
     * Represents the time format (00:00)
     */
    private String mTimeFormat = "%02d:%02d";

    /**
     * Represents the Clock. It contains the current time and is configurable
     */
    private Clock mClock = null;

    /**
     * Represents the runnable used by the Executor. It also contains the reference to the
     * TextView (optional)
     */
    private ClockTask mTask = null;

    /**
     * Represents the Executor that is executed to generate ticks
     */
    private ScheduledExecutorService mExecutor = null;

    /**
     * default constructor, initialize the Clock and ClockTask
     */
    public ClockManager() {

        // init the default game clock
        mClock = new Clock();
        mClock.setElapsedTime(0);
        mClock.setIntervalMs(1000);

        // init the runnable to enable ticks
        mTask = new ClockTask(this);

    }

    /**
     * Call the tick method from the Clock. If a TextView is attached, it will also generate
     * a message to be handled by the Handler
     * @param t
     */
    public void handleTick(ClockTask t) {
        // increment here
        getGameClock().tick();

        if (t.getClockView() != null) {

            // build and send the message for the handler
            Message completeMessage = mHandler.obtainMessage(0, t);
            completeMessage.sendToTarget();
        }
    }

    /**
     * Attach the TextView to the ClockTask. It will also initialize the Handler.
     * @param v
     */
    public void attachUI(TextView v) {
        mTask.setTextView(v);

        // init the handler
        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message m) {

                ClockTask task = (ClockTask)m.obj;
                long elapsed = task.getGameClockManager().getGameClock().getElapsed() / 1000;
                long min = elapsed / 60;
                long sec = elapsed % 60;

                task.getClockView().setText(String.format(mTimeFormat, min, sec));

            }
        };


    }

    /**
     * Start the clock by creating the Executor and starting it. It uses the parameters defined
     * in the Clock object.
     */
    public void startClock() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        mExecutor.scheduleAtFixedRate(mTask, mClock.getIntervalMs(), mClock.getIntervalMs(), TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the clock by shutting down the Executor.
     */
    public void stopClock() {
        if (mExecutor != null) {
            mExecutor.shutdown();
        }
    }

    /**
     * Return the Clock
     * @return the Clock
     */
    public Clock getGameClock() {
        return mClock;
    }

    /**
     * Set the Clock. Can be used to define customized parameter for this clock. Interval by
     * default is 1000ms (one second).
     * @param c
     */
    public void setGameClock(Clock c) {
        mClock = c;
    }
}
