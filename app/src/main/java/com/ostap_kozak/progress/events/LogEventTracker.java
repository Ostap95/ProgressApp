package com.ostap_kozak.progress.events;

import android.content.Context;
import android.support.annotation.StringRes;

import com.ostap_kozak.progress.util.LogUtils;

/**
 * Created by ostapkozak on 09/10/2016.
 */

public class LogEventTracker implements EventTracker {

    private static final String TAG = "Events";

    private final Context mContext;

    public LogEventTracker(Context context) {
        mContext = context;
    }

    @Override
    public void sendView(String screenName) {
        LogUtils.d(TAG, "viewing screen %s", screenName);
    }

    @Override
    public void sendEvent(@StringRes int category, @StringRes int action, @StringRes int label) {
        sendEvent(safeGetString(category), safeGetString(action), safeGetString(label));
    }

    @Override
    public void sendEvent(String category, String action, String label) {
        if (label == null) {
            LogUtils.d(TAG, "[%s] [%s]", category, action);
        } else {
            LogUtils.d(TAG, "[%s] [%s] [%s]", category, action, label);
        }
    }

    /**
     *
     * @param resId
     * @return Resource string represented by a given resource id, null if resId is invalid (0).
     */
    private String safeGetString(@StringRes int resId) {
        return resId == 0 ? null : mContext.getString(resId);
    }
}
