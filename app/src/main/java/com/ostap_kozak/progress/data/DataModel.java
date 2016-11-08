package com.ostap_kozak.progress.data;

/**
 * Created by ostapkozak on 04/11/2016.
 */


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * All application-wide data is accessible through this singleton.
 */
public class DataModel {

    /** The single instance of this data model that exists for the life of the application. */
    private static final DataModel sDataModel = new DataModel();

    private Handler mHandler;

    private Context mContext;

    /** The model from which settings are fetched. */
    private SettingsModel mSettingsModel;

    /** The model from which stopwatch data are fetched. */
    private StopwatchModel mStopwatchModel;

    /** The model from which notification data are fetched. */
    private NotificationModel mNotificationModel;


    public static DataModel getDataModel() {
        return sDataModel;
    }

    private DataModel() {}

    public void setContext(Context context) {
        if (mContext != null) {
            throw new IllegalStateException("context has already been set");
        }
        mContext = context.getApplicationContext();
        mSettingsModel = new SettingsModel(mContext);
        mStopwatchModel = new StopwatchModel(mContext, mNotificationModel);
    }


    /**
     * Convenience for {@code run(runnable, 0)}, i.e. waits indefinitely
     * @param runnable
     */
    public void run(Runnable runnable) {
        try {
            run(runnable, 0 /* waitMillis */);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Posts a runnable to the main thread and blocks until the runnable executes. Used to access the data model
     * from the main thread.
     * @param runnable
     * @param waitMillis
     * @throws InterruptedException
     */
    public void run(Runnable runnable, long waitMillis) throws InterruptedException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }

        final ExecutedRunnable er = new ExecutedRunnable(runnable);
        getHandler().post(er);

        // Wait for the data to arrive, it is has not
        synchronized (er) {
            if (!er.isExecuted()) {
                er.wait(waitMillis);
            }
        }
    }


    /**
     * @return a handler associated with the main thread
     */
    private synchronized  Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    /**
     *              APPLICATION
     */

    /**
     *
     * @param inForeground {@code true} to indicate the application is open in the foreground
     */
    public void setApplicationForeground(boolean inForeground) {
        enforceMainLooper();
        if (mNotificationModel.isApplicationInForeground() != inForeground) {
            mNotificationModel.setApplicationInForeground(inForeground);

            // Refresh all notifications in response to a change in app open state.
            mStopwatchModel.updateNotificaiton();
        }
    }

    /**
     *
     * @return {@code true} when the application is open in the foreground; {@code false} otherwise
     */
    public boolean isApplicationInForeground() {
        enforceMainLopper();
        return mNotificationModel.isApplicationInForeground();
    }

    /**
     * Called when the notification may be stale or absent from the notification manager and must be rebuilt.
     * e.g. afet upgrading the application
     */
    public void updateAllNotifications() {
        enforceMainLooper();
        mStopwatchModel.updateNotification();
    }


    /**
     * STOPWATCH
     */


    /**
     *
     * @param stopwatchListener to be notified when stopwatch changes
     */
    public void addStopwatchListener(StopwatchListener stopwatchListener) {
        enforceMainLooper();
        mStopwatchModel.addStopwatchListener(stopwatchListener);
    }


    public void removeStopwatchListener(StopwatchListener stopwatchListener) {
        enforceMainLooper();
        mStopwatchModel.removeStopwatchListener(stopwatchListener);
    }

    /**
     *
     * @return the current state of the stopwatch
     */
    public Stopwatch getStopwatch() {
        enforcemainLooper();
        return mStopwatchModel.getStopwatch();
    }

    /**
     *
     * @return the stopwatch after being started
     */
    public Stopwatch startStopwatch() {
        enforceMainLooper();
        return mStopwatchModel.setStopwatch(getStopwatch().start());
    }



    /**
     *
     * @return the stopwatch after being paused
     */
    public Stopwatch pauseStopwatch() {
        enforceMainLooper();
        return mStopwatchModel.setStopwatch(getStopwatch().pause());
    }

    /**
     *
     * @return the stopwatch after being reset
     */
    public Stopwatch resetStopwatch() {
        enforceMainLooper();
        return mStopwatchModel.setStopwatch(getStopwatch().reset());
    }



    /*
    /**
     * Used to execute a delegate runnable and track its completion.

    private static class ExecutedRunnable implements Runnable {

        private final Runnable mDelegate;
        private boolean mExecuted;

        private ExecutedRunnable(Runnable delegate) {
            this.mDelegate = delegate;
        }

        @Override
        public void run() {
            mDelegate.run();

            synchronized (this) {
                mExecuted = true;
                notifyAll();
            }
        }

        private boolean isExecuted() {
            return mExecuted;
        }
    }
     */

    /**
     * Used to execute a delegate runnable and track its completion.
     */
    private static class ExecutedRunnable implements  Runnable {

        private final Runnable mDelegate;
        private boolean mExecuted;

        private ExecutedRunnable(Runnable delegate) {
            this.mDelegate = delegate;
        }

        @Override
        public void run() {
            mDelegate.run();
            synchronized (this) {
                mExecuted = true;
                notifyAll();
            }
        }
    }
}


