package com.ostap_kozak.progress.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ostap_kozak.progress.activity.HandleTimeApiCalls;
import com.ostap_kozak.progress.events.Events;
import com.ostap_kozak.progress.service.StopwatchService;
import com.ostap_kozak.progress.util.LogUtils;
import com.ostap_kozak.progress.util.Utils;
import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.views.TimerView;

/**
 * Created by ostapkozak on 19/10/2016.
 */

public class StopWatchFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "StopwatchFragment";
    private static final int STOPWATCH_REFRESH_INTERVAL_MILLIS = 25;
    // Lower the refresh rate in accessibility mode to give talkback time to catch up
    private static final int STOPWATCH_ACCESSIBILTY_REFRESH_INTERVAL_MILLIS = 500;

    int mState = StopWatch.STOPWATCH_RESET;

    // Stopwatch views that are accessed by the activity
    private TextView mTimeText;

    // Used for calculating the time from the start taking into account the pause times.
    long mStartTime = 0;
    long mAccumulatedTime = 0;


    public StopWatchFragment() {}

    public void toggleStopWatchState() {
        long time = Utils.getTimeNow();
        Context context = getActivity().getApplicationContext();
        Intent intent = new Intent(context, StopwatchService.class);
        intent.putExtra(StopWatch.MESSAGE_TIME, time);

        switch (mState) {
            case StopWatch.STOPWATCH_RUNNING:
                // do stop
                long curTime = Utils.getTimeNow();
                mAccumulatedTime += (curTime - mStartTime);
                doStop();
                Events.sendStopwatchEvent(R.string.action_stop, R.string.label_timertask);

                intent.setAction(HandleTimeApiCalls.ACTION_STOP_STOPWATCH);
                context.startService(intent);
                releseWakeLock();
                break;
            case StopWatch.STOPWATCH_RESET:
            case StopWatch.STOPWATCH_STOPPED:
                // do start
                doStart(time);
                Events.sendStopwatchEvent(R.string.action_start, R.string.label_timertask);

                intent.setAction(HandleTimeApiCalls.ACTION_START_STOPWATCH);
                context.startService(intent);
                acquireWakeLock();
                break;
            default:
                LogUtils.wtf("Illegal state " + mState + " while pressing the right stopwatch button");
                break;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup)inflater.inflate(R.layout.stopwatch_fragment, container, false);

        mTimeText = (TimerView)v.findViewById(R.id.stopwatch_time_text);

        // Initiate the stopwatch when the view is created
        toggleStopWatchState();
        return v;
    }

    @Override
    public void onResume() {
        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
        readFromSharedPref(prefs);
        mTime.readFromSharedPref(prefs, "sw");
        mTime.postInvalidate();

        setFabAppearance();
        setLeftRightButtonAppearance();
        mTimeText.setTime(mAccumulatedTime, true, true);
        if (mState == Stopwatches.STOPWATCH_RUNNING) {
            acquireWakeLock();
            startUpdateThread();
        } else if (mState == Stopwatches.STOPWATCH_STOPPED && mAccumulatedTime != 0) {
            mTimeText.blinkTimeStr(true);
        }
        showLaps();
        ((DeskClock)getActivity()).registerPageChangedListener(this);
        // View was hidden in onPause, make sure it is visible now.
        View v = getView();
        if (v != null) {
            v.setVisibility(View.VISIBLE);
        }
        super.onResume();
         */

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);
        readFromSharedPref(prefs);

        setLeftRightButtonAppearance();
        mTimeText.setText(mAccumulatedTime, true, true);

        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
