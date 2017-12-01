package lib.shenle.com.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.LinkedList;
import java.util.List;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import lib.shenle.com.dagger2.Injectable;

/**
 * Created by fhf11991 on 2016/7/18.
 */

public class ActivityLifecycleHelper implements Application.ActivityLifecycleCallbacks {

    private static ActivityLifecycleHelper singleton;
    private static final Object lockObj = new Object();
    private static List<Activity> activities;

    private ActivityLifecycleHelper() {
        activities = new LinkedList<>();
    }

    public static ActivityLifecycleHelper build() {
        synchronized (lockObj) {
            if (singleton == null) {
                singleton = new ActivityLifecycleHelper();
            }
            return singleton;
        }
    }
    private void handleActivity(Activity activity) {
        if (activity instanceof HasSupportFragmentInjector) {
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentCreated(FragmentManager fm, Fragment f,
                                                              Bundle savedInstanceState) {
                                    if (f instanceof Injectable) {
                                        AndroidSupportInjection.inject(f);
                                    }
                                }
                            }, true);
        }
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        handleActivity(activity);
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }

        if (activities.size() == 0) {
            activities = null;
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new LinkedList<>();
        }
        activities.add(activity);
    }

    /**
     * 获取集合中当前Activity
     *
     * @return
     */
    public static Activity getLatestActivity() {
        ActivityLifecycleHelper adapter = build();
        int count = adapter.activities.size();
        if (count == 0) {
            return null;
        }
        return adapter.activities.get(count - 1);
    }

    /**
     * 获取集合中上一个Activity
     *
     * @return
     */
    public static Activity getPreviousActivity() {
        ActivityLifecycleHelper adapter = build();
        int count = adapter.activities.size();
        if (count < 2) {
            return null;
        }
        return adapter.activities.get(count - 2);
    }
}
