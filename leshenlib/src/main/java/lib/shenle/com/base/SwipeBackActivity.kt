package lib.shenle.com.base

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent

import lib.shenle.com.utils.SwipeBackHelper

/**
 * Created by fhf11991 on 2016/7/25.
 */

class SwipeBackActivity : AppCompatActivity(), SwipeBackHelper.SlideBackManager {

    private var mSwipeBackHelper: SwipeBackHelper? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = SwipeBackHelper(this)
        }
        return mSwipeBackHelper!!.processTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

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

