package com.ostap_kozak.progress.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * This service exists solely to allow the stopwatch notification to alter the state of the stopwatch without
 * disturbing the notification shade. If an activity were used instead (even one that is not displayed) the notification
 * manager implicitly closes the notification shade which clashes with the use case of starting/pausing/lapping/resetting
 * the stopwatch without disturbing the notification shade.
 */

public class StopwatchService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case HandleDeskClockApiCalls.ACTION_START_STOPWATCH: {
                DataModel.getDataModel().startStopwatch();
                Events.sendStopwatchEvent(R.string.action_start, R.string.label_notification);
                break;
            }
            case HandleDeskClockApiCalls.ACTION_PAUSE_STOPWATCH: {
                DataModel.getDataModel().pauseStopwatch();
                Events.sendStopwatchEvent(R.string.action_pause, R.string.label_notification);
                break;
            }
            case HandleDeskClockApiCalls.ACTION_RESET_STOPWATCH: {
                DataModel.getDataModel().resetStopwatch();
                Events.sendStopwatchEvent(R.string.action_reset, R.string.label_notification);
                break;
            }
            case HandleDeskClockApiCalls.ACTION_LAP_STOPWATCH: {
                DataModel.getDataModel().addLap();
                Events.sendStopwatchEvent(R.string.action_lap, R.string.label_notification);
                break;
            }
        }

        return START_NOT_STICKY;
    }
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case HandleTi
        }
    }
}


