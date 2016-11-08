package com.ostap_kozak.progress.events;

import android.support.annotation.StringRes;

import com.ostap_kozak.progress.R;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ostapkozak on 09/10/2016.
 */

public class Events {
    private static final Collection<EventTracker> sEventTrackers = new ArrayList<>();

    public static void addEventTracker(EventTracker eventTracker) {
        sEventTrackers.add(eventTracker);
    }

    public static void removeEventTracker(EventTracker eventTracker) {
        sEventTrackers.remove(eventTracker);
    }


    /**
     * Tracks an stopwatch event.
     * @param action action resource id of event action
     * @param label resource id of event labeld
     */
    public static void sendStopwatchEvent(@StringRes int action, @StringRes int label) {
        sendEvent(R.string.category_stopwatch, action, label);
    }


    /**
     * Tracks an event. Events have a category, action, label and value. This method
     * can be used to track events such as button presses or other user interaction with
     * your application (value is not used in this app).
     * @param category resource id of event category
     * @param action resource id of event action
     * @param label resource id of event label
     */
    public static void sendEvent(@StringRes int category, @StringRes int action, @StringRes int label) {
        for (EventTracker eventTracker : sEventTrackers) {
            eventTracker.sendEvent(category, action, label);
        }
    }

    /**
     * Tracks an event. Events have a category, action, label and value. This method
     * can be used to track events such as button presses or other user interaction with
     * your application (value is not used in this app).
     * @param category resource id of event category
     * @param action resource id of event action
     * @param label resource id of event label
     */
    public static void sendEvent(String category, String action, String label) {
        if (category != null && action != null) {
            for (EventTracker eventTracker : sEventTrackers) {
                eventTracker.sendEvent(category, action, label);
            }
        }
    }

    /**
     * Tracks entering a view with a new app screen name.
     * @param screenName the new app screen name
     */
    public static void sendView(String screenName) {
        for (EventTracker eventTracker : sEventTrackers) {
            eventTracker.sendView(screenName);
        }
    }
}
