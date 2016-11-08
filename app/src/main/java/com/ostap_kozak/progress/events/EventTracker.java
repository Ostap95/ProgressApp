package com.ostap_kozak.progress.events;

import android.support.annotation.StringRes;

/**
 * Created by ostapkozak on 09/10/2016.
 */

public interface EventTracker {

    /**
     * Send screen view tracking to Log system.
     *
     * @param screenName Screen name to be logged
     */
    void sendView(String screenName);


    /**
     * Send category, action and label describing an event to Log system.
     * @param category string resource id indicating Timer or StopWatch or 0 for no category
     * @param action string resource id indication how the entity was altered;
     *               e.g. create, delete, fire, etc or 0 for no action
     * @param label string resource id indicating where the action originated;
     *              e.g. (UI), Intent, Notification, etc. or 0 for no label
     */
    void sendEvent(@StringRes int category, @StringRes int action, @StringRes int label);


    /**
     *  Send category, action and label describing an event to Log system.
     * @param category  Timer or StopWatch
     * @param action    how the entity was altered; e.g. create, delete, fire, etc.
     * @param label where the action originated; e.g. UI, Intent, Notification, etc.
     */
    void sendEvent(String category, String action, String label);
}
