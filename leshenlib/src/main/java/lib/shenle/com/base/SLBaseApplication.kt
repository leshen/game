package lib.shenle.com.base

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.aitangba.swipeback.ActivityLifecycleHelper

/**
 * Created by shenle on 2017/11/16.
 */
open class SLBaseApplication : MultiDexApplication() {
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
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build())
        application = this
        mainThread = Thread.currentThread()
        mainThreadId = android.os.Process.myTid()
        mainThreadHandler = Handler()
        mainLoopler = mainLooper
    }
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