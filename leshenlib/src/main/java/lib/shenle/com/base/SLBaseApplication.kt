package lib.shenle.com.base

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import lib.shenle.com.utils.ActivityLifecycleHelper
import javax.inject.Inject
import kotlin.text.Typography.dagger

/**
 * Created by shenle on 2017/11/16.
 */
abstract class SLBaseApplication : MultiDexApplication(), HasActivityInjector {
    @Inject
    @JvmField var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null
    private var isFirstLaunch: Boolean = true
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (isFirstLaunch) {
            // 首次启动
            Thread(Runnable {
                isFirstLaunch = false
                MultiDex.install(base)
            }).start()
        } else {
            // 非首次启动
            MultiDex.install(base)
        }
    }
    companion object {
        lateinit var application: SLBaseApplication
        /** 获取主线程的handler  */
        lateinit var mainThreadHandler: Handler
        /** 获取主线程ID  */
        var mainThreadId = -1
        /** 获取boolean主线程  */
        lateinit var mainThread: Thread
        /** 获取主线程的looper  */
        var mainLoopler: Looper? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        mainThread = Thread.currentThread()
        mainThreadId = android.os.Process.myTid()
        mainThreadHandler = Handler()
        mainLoopler = mainLooper
        initDraggerApp()
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build())
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }
    /**
     * 注入app dragger
     */
    abstract fun initDraggerApp()

    override fun onTerminate() {
        onDestory()
        System.exit(0)
        super.onTerminate()
    }
    /**
     * 销毁的其他
     */
    private fun onDestory() {
        Process.killProcess(Process.myPid())
    }
}