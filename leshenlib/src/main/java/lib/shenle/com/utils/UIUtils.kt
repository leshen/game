package lib.shenle.com.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lib.shenle.com.base.SLBaseActivity
import lib.shenle.com.base.SLBaseApplication
import lib.shenle.com.base.SLBaseApplication.Companion.mainThreadId
import lib.shenle.com.view.BadgeView
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

object UIUtils {

    private var toast: Toast? = null

    val context: Context
        get() = SLBaseApplication.application
    /**
     * dip转换px
     */
    fun dip2px(dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    /**
     * pxz转换dip
     */
    fun px2dip(px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕的宽 单位px

     * @return int
     */
    val screenWidth: Int
        get() = context.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕的高 单位px

     * @return int
     */
    val screenHeight: Int
        get() = context.resources.displayMetrics.heightPixels

    /**
     * 获取主线程的handler
     */
    val handler: Handler
        get() = SLBaseApplication.mainThreadHandler

    /**
     * 延时在主线程执行runnable
     */
    fun postDelayed(runnable: Runnable, delayMillis: Long): Boolean {
        return handler.postDelayed(runnable, delayMillis)
    }

    /**
     * 在主线程执行runnable
     */
    fun post(runnable: Runnable): Boolean {
        return handler.post(runnable)
    }

    /**
     * 从主线程looper里面移除runnable
     */
    fun removeCallbacks(runnable: Runnable) {
        handler.removeCallbacks(runnable)
    }

    fun inflate(resId: Int): View {
        return LayoutInflater.from(context).inflate(resId, null)
    }

    /**
     * 获取资源
     */
    val resources: Resources
        get() = context.resources

    /**
     * 获取文字
     */
    fun getString(resId: Int): String {
        return resources.getString(resId)
    }

    /**
     * 获取文字数组
     */
    fun getStringArray(resId: Int): Array<String> {
        return resources.getStringArray(resId)
    }

    /**
     * 获取dimen
     */
    fun getDimens(resId: Int): Int {
        return resources.getDimensionPixelSize(resId)
    }

    /**
     * 获取drawable
     */
    fun getDrawable(resId: Int): Drawable {
        return resources.getDrawable(resId)
    }

    /**
     * 获取drawable
     */
    fun getDrawableId(resName: String): Int {
        val indentify = resources.getIdentifier(context.packageName + ":drawable/" +
                resName, null, null)
        return indentify
    }

    /**
     * 获取颜色
     */
    fun getColor(resId: Int): Int {
        return resources.getColor(resId)
    }

    /**
     * 获取颜色选择器
     */
    fun getColorStateList(resId: Int): ColorStateList {
        return resources.getColorStateList(resId)
    }

    // 判断当前的线程是不是在主线程
    val isRunInMainThread: Boolean
        get() = android.os.Process.myTid() == mainThreadId

    fun runInMainThread(runnable: Runnable) {
        if (isRunInMainThread) {
            runnable.run()
        } else {
            post(runnable)
        }
    }

    fun startActivity(cl: Class<*>, bundle: Bundle?, bundlename: String) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        val intent = Intent(activity, cl)
        if (bundle != null) {
            intent.putExtra(bundlename, bundle)
        }
        if (activity != null) {
            //			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun startActivity(intent: Intent) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        if (activity != null) {
            activity.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun startActivity(cl: Class<*>) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        val intent = Intent(activity, cl)
        if (activity != null) {
            //           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun startActivityForResult(cl: Class<*>, requestcode: Int) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        val intent = Intent(activity, cl)
        if (activity != null) {
            //            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivityForResult(intent, requestcode)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun startActivityForResult(cl: Class<*>, bundle: Bundle?, requestcode: Int) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        val intent = Intent(activity, cl)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (activity != null) {
            //            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivityForResult(intent, requestcode)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun startActivity(cl: Class<*>, bundle: Bundle?) {
        val activity = ActivityLifecycleHelper.getLatestActivity()
        val intent = Intent(activity, cl)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (activity != null) {
            //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    fun showToastSafe(resId: Int) {
        showToastSafe(getString(resId))
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    fun showToastSafe(str: String?) {
        if (UIUtils.isEmpty(str)) {
            return
        }
        if (isRunInMainThread) {
            showToast(str!!)
        } else {
            post(Runnable { showToast(str!!) })
        }
    }

    private fun showToast(str: String) {
        val frontActivity = ActivityLifecycleHelper.getLatestActivity()
        if (frontActivity != null) {
            if (toast == null) {
                toast = Toast.makeText(frontActivity, str, Toast.LENGTH_LONG)
            } else {
                toast!!.setText(str)
            }
            toast!!.show()
        }
    }

    /**
     * 格式电话号码

     * @param phone 格式化后 xxx-xxxx-xxxx 如果不是11位电话,则不做任何操作
     * *
     * @return
     */
    fun formatPhone(mobile: String): String {
        var phone = mobile
        phone = phone.trim()
        if (phone.length == 11) {
            val sBuilder = StringBuilder(phone)
            val line = "-"
            val newString = "${sBuilder.substring(0, 3) +
                    line+sBuilder.substring(3, 7) + line+
                    sBuilder.substring(7, sBuilder.length)}"
            phone = newString
        }
        return phone
    }

    /**
     * @param activity
     * *
     * @return 状态栏高度  > 0 success; <= 0 fail
     */
    fun getStatusHeight(activity: Activity): Int {
        val localRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        var statusHeight = localRect.top
        if (0 == statusHeight) {
            val localClass: Class<*>
            try {
                localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                        .toString())
                statusHeight = activity.resources.getDimensionPixelSize(i5)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }

    fun closeWindowKeyBoard() {
        val im = UIUtils.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (UIUtils.getActivity()?.currentFocus != null) {
            im.hideSoftInputFromWindow(UIUtils.getActivity()?.currentFocus!!
                    .applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun closeWindowKeyBoard(et: EditText) {
        val imm = UIUtils.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et.windowToken, 0)
    }

    fun openWindowKeyBoard() {
        val imm = UIUtils.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun openWindowKeyBoard(et: EditText) {
        val imm = UIUtils.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, 0)
    }

    fun showErr() {
        UIUtils.showToastSafe("网络访问失败")
    }

    fun getActivity(): Activity?{
        return ActivityLifecycleHelper.getLatestActivity()
    }


    fun isEmpty(s: String?): Boolean {
        return s == null || s.trim().isEmpty() || s == "null"
    }

    /**
     * 初始化红点

     * @param view
     * *
     * @return
     */
    fun initBadge(view: View, str: String?): BadgeView {
        var text = str
        val badge = BadgeView(getActivity(), view)
        badge.textSize = 9f
        if (text == null)
            text = ""
        badge.text = text
        badge.badgePosition = BadgeView.POSITION_TOP_RIGHT
        badge.badgeMargin = UIUtils.dip2px(5)
        return badge
    }

    //    public static void register(Object o) {
    //        EventBus.getDefault().register(o);
    //    }
    //
    //    public static void unregister(Object o) {
    //        EventBus.getDefault().unregister(o);
    //    }

    /**
     * 获取资源文件ID

     * @param resName
     * *
     * @param defType
     * *
     * @return
     */
    fun getResId(resName: String, defType: String): Int {
        return context.resources.getIdentifier(resName, defType, context.packageName)
    }

    /**
     * 回收view

     * @param view
     */
    fun unbindDrawables(view: View?) {
//        if (view?.tag != null && view.tag is BaseHolder) {
//            (view.tag as BaseHolder).onDestroy()
//        }
        if (view?.background != null) {
            view.background.callback = null
        }
        if (view is ViewGroup && view !is AdapterView<*>) {
            for (i in 0..view.childCount - 1) {
                unbindDrawables(view.getChildAt(i))
            }
            view.removeAllViews()
        }
    }

    fun setToolBar(toolbar: Toolbar?, title: String): Toolbar? {
        toolbar?.setTitle(title)
        return toolbar
    }

    fun finishDelay(i: Long,activity: SLBaseActivity) {
//        Observable.timer(i, TimeUnit.SECONDS).compose(activity.bindToLifecycle()).subscribe {
        Observable.timer(i, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .observeOn( AndroidSchedulers.mainThread() )  //指定 Subscriber 的回调发生在主线程
                .compose(activity.bindUntilEvent<Long>(ActivityEvent.DESTROY ))
//                .compose(activity.bindToLifecycle())
                .subscribe {
            activity.finish()
//            UIUtils.showToastSafe("已经关闭了"+activity.toString())
        }
    }

    fun <T> register(clazz: Class<T>, event: () -> Unit):Observable<T> {
        val observable = RxBus.get().register("sl", clazz)
        observable.subscribe { event }
        return observable
    }
    fun <T> unregister(observable:Observable<T>) {
        RxBus.get().unregister("sl",observable)
    }

    /**
     * //两秒钟之内只取一个点击事件，防抖操作
     */
    fun onClick(button: Button, event: (view :View) -> Unit) {
        RxView.clicks( button )
                .throttleFirst( 2 , TimeUnit.SECONDS )
                .compose((getActivityFromView(button)  as SLBaseActivity).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe{event}
    }
    /**
     * try get host activity from view.
     * views hosted on floating window like dialog and toast will sure return null.
     * @return host activity; or null if not available
     */
    fun  getActivityFromView(view:View):Activity? {
        var context = view.getContext()
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.getBaseContext()
        }
        return null
    }
    //通过反射获取状态栏高度，默认25dp
//    fun  getStatusBarHeight(context:Context):Int {
//        var statusBarHeight = dip2px(25)
//        try {
//            var clazz = Class.forName("com.android.internal.R\$dimen")
//            var o = clazz.newInstance()
//            var height = Integer.parseInt(clazz.getField("status_bar_height")
//                    .get(o).toString())
//            statusBarHeight = context.getResources().getDimensionPixelSize(height)
//        } catch (e:Exception) {
//            e.printStackTrace()
//        }
//        return statusBarHeight
//    }
    //通过反射获取状态栏高度，默认25dp
    fun  getStatusBarHeight(context:Context):Int {
        var statusBarHeight = -1
//获取status_bar_height资源的ID
        val resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}
