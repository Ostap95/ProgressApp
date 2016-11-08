package com.ostap_kozak.progress.data;

/**
 * Created by ostapkozak on 08/11/2016.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationManagerCompat;

import com.ostap_kozak.progress.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * All {@link Stopwatch} data is accessed via this model.
 */
public class StopwatchModel {

    private final Context mContext;

    /** The model from which notification data are fetched. */
    private final NotificationModel mNotificationModel;

    /** Used to create and destroy system notifications related to the stopwatch. */
    private final notificationManagerCompat mNotificationManager;

    /** Update stopwatch notification when locale changes. */
    @SuppressWarnings("FieldCanBeLocal")
    private final BroadcastReceiver mLocaleChangedReceiver = new LocaleChangedReceiver();

    /** The listeners to notify when the stopwatch change */
    private final List<StopwatchListener> mStopwatchListeners = new ArrayList<>();

    /** Delegate that builds platform-specific stopwatch notification. */
    private NotificationBuilder mNotificationBuilder;

    /** The current state of the stopwatch. */
    private Stopwatch mStopwatch;


    StopwatchModel(Context context, NotificationModel notificationModel) {
        mContext = context;
        mNotificationModel = notificationModel;
        mNotificationManager = NotificationManagerCompat.from(context);

        // Update stopwatch notification when locale changes.
        final IntentFilter localeBroadcastFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        mContext.registerReceiver(mLocaleChangedReceiver, localeBroadcastFilter);

    }

    /**
     * @param stopwatchListener to be notified when stopwatch changes.
     */
    void addStopwatchListener(StopwatchListener stopwatchListener) {
        mStopwatchListeners.add(stopwatchListener);
    }

    /**
     * @param stopwatchListener to no longer be notified when stopwatch changes.
     */
    void removeStopwatchListener(StopwatchListener stopwatchListener) {
        mStopwatchListeners.remove(stopwatchListener);
    }


    Stopwatch getStopwatch() {
        if (mStopwatch == null) {
            mStopwatch = StopwatchDAO.getStopwatch(mContext);
        }

        return mStopwatch;
    }

    /**
     * @param stopwatch the new state of the stopwatch
     * @return
     */
    Stopwatch setStopwatch(Stopwatch stopwatch) {
        final Stopwatch before = getStopwatch();
        if (before != stopwatch) {
            StopwatchDAO.setStopwatch(mContext, stopwatch);
            mStopwatch = stopwatch;

            // Refresh the stopwatch notification to reflect the latest stopwatch state.
            if (!mNotificationModel.isApplicationInForegound()) {
                updateNotification();
            }

            // Notify listeners of the stopwatch change.
            for (StopwatchListener stopwatchListener : mStopwatchListeners) {
                stopwatchListener.stopwatchUpdate(before, stopwatch);
            }
        }

        return stopwatch;
    }

    /**
     * Updates the notification to reflect the latest state of the stopwatch.
     */
    void updateNotification() {
        final Stopwatch stopwatch = getStopwatch();

        // Notification should be hidden if the stopwatch has no time or the app is open.
        if (stopwatch.isReset() || mNotificationModel.isApplicationInForegound()) {
            return;
        }

        // Otherwise build and post a notification reflecting the latest stopwatch state.
        final Notification notification = getNotificationBuilder().build(mContext, mNotificationModel, stopwatch);
        mNotificationManager.notify(mNotificationModel.getStopwatchNotificationId(), notification);
    }


    private NotificationBuilder getNotificationBuilder() {
        if (mNotificationBuilder == null) {
            if (Utils.isNOrLater()) {
                mNotificationBuilder = new StopwatchNotificationBuilderN();
            } else {
                mNotificationBuilder = new StopwatchNotificationBuilderPreN();
            }
        }
        return mNotificationBuilder;
    }

    /**
     * Update the stopwatch notification in response to a locale change.
     */
    private final class localeChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

    /**
     * An API for building platform-specific stopwatch notifications.
     */
    public interface NotificationBuilder {

        /**
         * @param context a context to use for fetching resources
         * @param nm from which notification data are fetched
         * @param stopwatch the stopwatch guaranteed to be running or paused
         * @return a notification reporting the state of the {@code stopwatch}
         */
        Notification build(Context context, NotificationModel nm, Stopwatch stopwatch);
    }
}

