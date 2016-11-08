package com.ostap_kozak.progress.data;

/**
 * Created by ostapkozak on 08/11/2016.
 */

/**
 * Data that must be coordinated across all notifications is accessed via this model.
 */
final class NotificationModel {

    /*
    private boolean mApplicationInForeground;

    /**
     * @param inForeground {@code true} to indicate the application is open in the foreground

    void setApplicationInForeground(boolean inForeground) {
        mApplicationInForeground = inForeground;
    }

    /**
     * @return {@code true} while the application is open in the foreground

    boolean isApplicationInForeground() {
        return mApplicationInForeground;
    }
     */

    private boolean mApplicationInForeground;

    /**
     * @param inForeground
     */
    void setApplicationInForeground(boolean inForeground) {
        mApplicationInForeground = inForeground;
    }


}
