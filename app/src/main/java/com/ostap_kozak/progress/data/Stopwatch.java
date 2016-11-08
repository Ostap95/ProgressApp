package com.ostap_kozak.progress.data;

/**
 * Created by ostapkozak on 31/10/2016.
 */


import android.net.Uri;
import android.os.SystemClock;

import com.ostap_kozak.progress.provider.ClockContract;

import static com.ostap_kozak.progress.data.Stopwatch.State.PAUSED;
import static com.ostap_kozak.progress.data.Stopwatch.State.RESET;
import static com.ostap_kozak.progress.data.Stopwatch.State.RUNNING;

/**
 * A read-only domain object representing a stopwatch.
 */
public class Stopwatch {

    public enum State { RESET, RUNNING, PAUSED}

    /**
     * The content:// style URI for the stopwatch
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + ClockContract.AUTHORITY + "/stopwatch");

    /** The single, immutable instance of a reset stopwatch. */
    private static final Stopwatch RESET_STOPWATCH = new Stopwatch(RESET, Long.MIN_VALUE, 0);

    /** Current state of this stopwatch. */
    private final State mState;

    /** Elapsed time in ms the stopwatch was last started; {@link Long#MIN_VALUE} if nor running. */
    private final long mLastStartTime;

    /** Elapsed time in ms this stopwatch has accumulated while running. */
    private final long mAccumulatedTime;

    Stopwatch(State state, long lastStartTime, long accumulatedTime) {
        mState = state;
        mLastStartTime = lastStartTime;
        mAccumulatedTime = accumulatedTime;
    }

    public Uri getContentUri() {
        return CONTENT_URI;
    }

    public State getState() { return mState; }
    public long getLastStartTime() { return mLastStartTime; }
    public boolean isReset() { return mState == RESET; }
    public boolean isPaused() { return mState == PAUSED; }
    public boolean isRunning() { return mState == RUNNING; }

    /**
     * @return the total amount of time accumulated up to this moment
     */
    public long getTotalTime() {
        if (mState != RUNNING) {
            return mAccumulatedTime;
        }

        /*
        in practice, "now" can be any value due to device reboots. When the real-time clock is reset, there
        is no more guarantee that "now" falls after the last start time. To ensure the stopwatch is
        monotonically increasing, normalize negative time segments to 0.
        */
        final long timeSinceStart = now() - mLastStartTime;
        return mAccumulatedTime + Math.max(0, timeSinceStart);
    }


    /**
     * @return the amount of time accumulated up to the last time the stopwatch was started
     */
    public long getAccumulatedTime() {
        return mAccumulatedTime;
    }


    /**
     * @return a copy of this stopwatch that is running
     */
    Stopwatch start() {
        if (mState == RUNNING) {
            return this;
        }

        return new Stopwatch(RUNNING, now(), getTotalTime());
    }


    /**
     * @return a copy of this stopwatch that is paused
     */
    Stopwatch pause() {
        if (mState != RUNNING) {
            return this;
        }

        return new Stopwatch(PAUSED, Long.MIN_VALUE, getTotalTime());
    }

    /**
     * @return a copy of this stopwatch that is reset
     */
    Stopwatch reset() {
        return RESET_STOPWATCH;
    }

    private static long now() {
        return SystemClock.elapsedRealtime();
    }
}
