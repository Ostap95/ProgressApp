package com.ostap_kozak.progress.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ostapkozak on 19/10/2016.
 */

public class TimerView extends TextView {
    private static final String TWO_DIGITS = "%02d";
    private static final String ONE_DIGIT = "%01d";
    private static final String NEG_TWO_DIGITS = "-%02d";
    private static final String NEG_ONE_DIGIT = "-%01d";
    private String mHours, mMinutes, mSeconds, mHundredths;

    public static final String PREF_CTV_PAUSED  = "_ctv_paused";
    public static final String PREF_CTV_INTERVAL  = "_ctv_interval";
    public static final String PREF_CTV_INTERVAL_START = "_ctv_interval_start";
    public static final String PREF_CTV_CURRENT_INTERVAL = "_ctv_current_interval";
    public static final String PREF_CTV_ACCUM_TIME = "_ctv_accum_time";
    public static final String PREF_CTV_TIMER_MODE = "_ctv_timer_mode";
    public static final String PREF_CTV_MARKER_TIME = "_ctv_marker_time";

    private long mIntervalTime = 0;
    private long mIntervalStartTime = -1;
    private long mMarkerTime = -1;
    private long mCurrentIntervalTime = 0;
    private long mAccumulatedTime = 0;
    private boolean mPaused = false;
    // Stopwatch mode is the default.
    private boolean mTimerMode = false;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTimerMode(boolean mode) {
        mTimerMode = mode;
    }

    // Since this view is used in multiple places, use the key to save different instances
    public void writeToSharedPref(SharedPreferences prefs, String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key + PREF_CTV_PAUSED, mPaused);
        editor.putLong(key + PREF_CTV_INTERVAL, mIntervalTime);
        editor.putLong(key + PREF_CTV_INTERVAL_START, mIntervalStartTime);
        editor.putLong(key + PREF_CTV_CURRENT_INTERVAL, mCurrentIntervalTime);
        editor.putLong(key + PREF_CTV_ACCUM_TIME, mAccumulatedTime);
        editor.putLong(key + PREF_CTV_MARKER_TIME, mMarkerTime);
        editor.putBoolean(key + PREF_CTV_TIMER_MODE, mTimerMode);
    }

    public void readFromSharedPref(SharedPreferences prefs, String key) {
        mPaused = prefs.getBoolean(key + PREF_CTV_PAUSED, false);
        mIntervalTime = prefs.getLong(key + PREF_CTV_INTERVAL, 0);
        mIntervalStartTime = prefs.getLong(key + PREF_CTV_INTERVAL_START, -1);
        mCurrentIntervalTime = prefs.getLong(key + PREF_CTV_CURRENT_INTERVAL, 0);
        mAccumulatedTime = prefs.getLong(key + PREF_CTV_ACCUM_TIME, 0);
        mMarkerTime = prefs.getLong(key + PREF_CTV_MARKER_TIME, -1);
        mTimerMode = prefs.getBoolean(key + PREF_CTV_TIMER_MODE, false);
    }


    public void clearSharedPrefs(SharedPreferences prefs, String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove (StopWatch.PREF_START_TIME);
        editor.remove (StopWatch.PREF_ACCUM_TIME);
        editor.remove (StopWatch.PREF_STATE);
        editor.remove (key + PREF_CTV_PAUSED);
        editor.remove (key + PREF_CTV_INTERVAL);
        editor.remove (key + PREF_CTV_INTERVAL_START);
        editor.remove (key + PREF_CTV_CURRENT_INTERVAL);
        editor.remove (key + PREF_CTV_ACCUM_TIME);
        editor.remove (key + PREF_CTV_MARKER_TIME);
        editor.remove (key + PREF_CTV_TIMER_MODE);
        editor.apply();
    }

    /**
     * Update the time to display. Separates that time into the hours, minutes, seconds and
     * hundredths. If update is true, the view is invalidated so that it will draw again.
     *
     * @param time new time to display - in milliseconds
     * @param showHundredths flag to show hundredths resolution
     * @param update to invalidate the view - otherwise the time is examined to see if it is within
     *               100 milliseconds of zero seconds and when so, invalidate the view.
     */
    // TODO:showHundredths S/B attribute or setter - i.e. unchanging over object life
    public void setTime(long time, boolean showHundredths, boolean update) {
        boolean neg = false, showNeg = false;
        String format;
        if (time < 0) {
            time = -time;
            neg = showNeg = true;
        }
        long hundreds, seconds, minutes, hours;
        seconds = time / 1000;
        hundreds = (time - seconds * 1000) / 10;
        minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        hours = minutes / 60;
        minutes = minutes - hours * 60;
        if (hours > 999) {
            hours = 0;
        }
        // The time  can be between 0 and -1 seconds, but the "truncated" equivalent time of hours
        // and minutes and seconds could be zero, so since we do not show fractions of seconds
        // when counting down, do not show the minus sign.
        // TODO:does it matter that we do not look at showHundredths?
        if (hours == 0 && minutes == 0 && seconds == 0) {
            showNeg = false;
        }

        // Normalize and check if it is 'time' to invalidate
        if (!showHundredths) {
            if (!neg && hundreds != 0) {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }
            }
            if (hundreds < 10 || hundreds > 90) {
                update = true;
            }
        }

        // Hours may be empty
        if (hours >= 10) {
            format = showNeg ? NEG_TWO_DIGITS : TWO_DIGITS;
            mHours = String.format(format, hours);
        } else if (hours > 0) {
            format = showNeg ? NEG_ONE_DIGIT : ONE_DIGIT;
            mHours = String.format(format, hours);
        } else {
            mHours = null;
        }

        // Minutes are never empty and when hours are non-empty, must be two digits
        if (minutes >= 10 || hours > 0) {
            format = (showNeg && hours == 0) ? NEG_TWO_DIGITS : TWO_DIGITS;
            mMinutes = String.format(format, minutes);
        } else {
            format = (showNeg && hours == 0) ? NEG_ONE_DIGIT : ONE_DIGIT;
            mMinutes = String.format(format, minutes);
        }

        // Seconds are always two digits
        mSeconds = String.format(TWO_DIGITS, seconds);

        // Hundredths are optional and then two digits
        if (showHundredths) {
            mHundredths = String.format(TWO_DIGITS, hundreds);
        } else {
            mHundredths = null;
        }

        if (update) {
            postInvalidateOnAnimation();
        }
    }
}
