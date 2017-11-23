package lib.shenle.com.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import lib.shenle.com.utils.SwipeBackHelper


/**
 * Created by shenle on 2017/7/31.
 */
abstract class SLBaseActivity : RxAppCompatActivity(), SwipeBackHelper.SlideBackManager{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun onBack(view: View) {
        onBackPressed()
    }

    // // 获取点击事件
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isHideInput(view, ev)) {
                HideSoftInput(view.windowToken)
            }
        }
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = SwipeBackHelper(this)
        }
        return mSwipeBackHelper!!.processTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    // 判定是否需要隐藏
    private fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText && v.hasFocusable()) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            if (ev.y > top - 100) {
                // && ev.getY() < bottom+150) {
                return false
            } else {
                return true
            }
        }
        return false
    }

    // 隐藏软键盘
    private fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
    /**
     * Reload activity
     */
    fun reload() {
        startActivity(Intent(this, this.localClassName::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
    }

    fun getCurrentRootView(): View {
        return findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private val TAG = "SwipeBackActivity"

    private var mSwipeBackHelper: SwipeBackHelper? = null

    override fun getSlideActivity(): Activity {
        return this
    }

    override fun supportSlideBack(): Boolean {
        return true
    }

    override fun canBeSlideBack(): Boolean {
        return true
    }

    override fun finish() {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper!!.finishSwipeImmediately()
            mSwipeBackHelper = null
        }
        super.finish()
    }

}