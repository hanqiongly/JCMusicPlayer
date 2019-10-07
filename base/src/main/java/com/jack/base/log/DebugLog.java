package com.jack.base.log;

import android.util.Log;

public class DebugLog {
    private static final String DEFAULT_TAG = "JCMusic_Player";

    public static void d(String content) {
        d(DEFAULT_TAG, content);
    }

    public static void d(String tag, String cotent) {
        Log.d(tag, cotent);
    }
}
