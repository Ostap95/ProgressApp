package com.ostap_kozak.progress.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.events.Events;
import com.ostap_kozak.progress.service.StopwatchService;

/**
 * Created by ostapkozak on 09/10/2016.
 */

public class HandleTimeApiCalls extends Activity {
    private Context mAppContext;

    private static final String ACTION_PREFIX = "com.ostap_kozak.progress.action.";

    // starts the current stopwatch
    public static final String ACTION_START_STOPWATCH = ACTION_PREFIX + "START_STOPWATCH";
    // pauses the current stopwatch that's currently running
    public static final String ACTION_PAUSE_STOPWATCH = ACTION_PREFIX + "PAUSE_STOPWATCH";
    // stops the current stopwatch
    public static final String ACTION_STOP_STOPWATCH =  ACTION_PREFIX + "STOP_STOPWATCH";
    // resumes the current stopwatch
    public static final String ACTION_RESET_STOPWATCH = ACTION_PREFIX + "RESET_STOPWATCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mAppContext = getApplicationContext();

            final Intent intent = getIntent();
            if (intent == null) return;

            final String action = intent.getAction();

            switch (action) {
                case ACTION_START_STOPWATCH:
                case ACTION_STOP_STOPWATCH:
                case ACTION_PAUSE_STOPWATCH:
                case ACTION_RESET_STOPWATCH:
                    handleStopWatchIntent(action);
                    break;
            }
        } finally {
            finish();
        }

    }


    private void handleStopWatchIntent(String action) {
        switch (action) {
            case ACTION_START_STOPWATCH:
                Events.sendStopwatchEvent(R.string.action_start, R.string.label_intent);
                break;
            case ACTION_STOP_STOPWATCH:
                Events.sendStopwatchEvent(R.string.action_stop, R.string.label_intent);
                break;
            case ACTION_RESET_STOPWATCH:
                Events.sendStopwatchEvent(R.string.action_resume, R.string.label_intent);
                break;
        }

        final Intent intent = new Intent(mAppContext, StopwatchService.class).setAction(action);
        startService(intent);
    }
}
