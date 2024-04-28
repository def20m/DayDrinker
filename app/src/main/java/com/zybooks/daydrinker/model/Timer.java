package com.zybooks.daydrinker.model;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Timer {

    private long mTargetTime;
    private boolean mRunning;
    private long mDurationMillis;

    public Timer() {
        mRunning = false;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void start(long millisLeft) {
        mDurationMillis = millisLeft;
        mTargetTime = SystemClock.uptimeMillis() + mDurationMillis;
        mRunning = true;
    }

    public void start(int hours, int minutes, int seconds) {
        // Add 1 sec to duration so timer stays on current second longer
        mDurationMillis = (hours * 60L * 60L + minutes * 60L + seconds + 1) * 1000;
        mTargetTime = SystemClock.uptimeMillis() + mDurationMillis;

        mRunning = true;
    }

    public void stop() {
        mRunning = false;
    }

    public long getRemainingMilliseconds() {
        if (mRunning) {
            return Math.max(0, mTargetTime - SystemClock.uptimeMillis());
        }
        return 0;
    }

    public int getProgressPercent() {
        if (mDurationMillis != 1000) {
            return Math.min(100, 100 - (int) ((getRemainingMilliseconds() - 1000) * 100 /
                    (mDurationMillis - 1000)));
        }
        return 0;
    }
}