package com.ostap_kozak.progress.util;

import android.os.Build;
import android.util.Log;

/**
 * Created by ostapkozak on 09/10/2016.
 */

public class LogUtils {
    public final static String LOGTAG = "ProgressTimer";
    public final static boolean DEBUG = "eng".equals(Build.TYPE) || "userdebug".equals(Build.TYPE);

    public static void v(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.VERBOSE)) {
            Log.v(LOGTAG, args == null || args.length == 0
                    ? message : String.format(message, args));
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.VERBOSE)) {
            Log.v(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void d(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.DEBUG)) {
            Log.d(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.DEBUG)) {
            Log.d(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void i(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.INFO)) {
            Log.i(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.INFO)) {
            Log.i(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void w(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.w(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void w(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.w(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ERROR)) {
            Log.e(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ERROR)) {
            Log.e(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void e(String message, Exception e) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ERROR)) {
            Log.e(LOGTAG, message, e);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ERROR)) {
            Log.e(LOGTAG + "/" + tag, message, e);
        }
    }

    public static void wtf(String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ASSERT)) {
            Log.wtf(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void wtf(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(LOGTAG, Log.ASSERT)) {
            Log.wtf(LOGTAG + "/" + tag, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

}
