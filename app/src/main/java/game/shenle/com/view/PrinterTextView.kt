package game.shenle.com.view

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.text.StaticLayout
import android.util.AttributeSet

import java.util.Timer
import java.util.TimerTask

import skin.support.widget.SkinCompatTextView

/**
 * 打字效果
 * Created by junweiliu on 16/12/29.
 */
class PrinterTextView : SkinCompatTextView {
    /**
     * 默认打字字符
     */
    private val DEFAULT_INTERVAL_CHAR = "_"
    /**
     * 默认打字间隔时间
     */
    private val DEFAULT_TIME_DELAY = 150
    /**
     * 计时器
     */
    private var mTimer: Timer? = null
    /**
     * 需要打字的文字
     */
    private var mPrintStr: String? = null
    /**
     * 间隔时间
     */
    private var intervalTime = DEFAULT_TIME_DELAY
    /**
     * 间隔字符
     */
    private var intervalChar = DEFAULT_INTERVAL_CHAR
    /**
     * 打字进度
     */
    private var printProgress = 0


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * 设置需要打字的文字,打字间隔,间隔符号
     *
     * @param str          打字文字
     * @param time         打字间隔(ms)
     * @param intervalChar 间隔符号("_")
     */
    @JvmOverloads
    fun setPrintText(str: String, time: Int = DEFAULT_TIME_DELAY, intervalChar: String = DEFAULT_INTERVAL_CHAR) {
        if (strIsEmpty(str) || 0 == time || strIsEmpty(intervalChar)) {
            return
        }
        this.mPrintStr = str
        this.intervalTime = time
        this.intervalChar = intervalChar
    }

    /**
     * 开始打字
     */
    fun startPrint(listener: OnPrintListener?) {
        this.listener = listener
        // 判空处理
        if (strIsEmpty(mPrintStr)) {
            if (!strIsEmpty(text.toString())) {
                this.mPrintStr = text.toString()
            } else {
                return
            }
        }
        // 重置相关信息
        text = ""
        stopPrint()
        listener?.start(mPrintStr!!)
        printProgress = 0
        mTimer = Timer()
        mTimer!!.schedule(PrinterTimeTask(), intervalTime.toLong(), intervalTime.toLong())
    }

    fun restartTimer() {
        if (mTimer != null) {
            mTimer!!.cancel();

        }
        mTimer = Timer()
        mTimer!!.schedule(PrinterTimeTask(), intervalTime.toLong(), intervalTime.toLong())
    }

    private var listener: OnPrintListener? = null

    /**
     * 停止打字
     */
    fun stopPrint() {
        if (null != mTimer) {
            mTimer!!.cancel()
            mTimer = null
        }
    }

    /**
     * 暂停打字
     */
    fun pausePrint() {
        if (null != mTimer) {
            mTimer!!.cancel()
        }
    }

    /**
     * 判断str是否为空
     *
     * @param str
     * @return
     */
    private fun strIsEmpty(str: String?): Boolean {
        return if (null != str && "" != str) {
            false
        } else {
            true
        }
    }

//    override fun onDraw(canvas: Canvas) {
//        val paint = paint
//        paint.color = textColors.defaultColor
//        val layout = StaticLayout(text, paint, canvas.width, Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, false)
//        layout.draw(canvas)
//    }

    /**
     * 打字计时器任务
     */
    internal inner class PrinterTimeTask : TimerTask() {

        override fun run() {
            // 需要刷新页面,必须在UI线程,使用post方法
            post {
                // 如果未显示完,继续显示
                if (printProgress < mPrintStr!!.length) {
                    printProgress++
                    // (printProgress & 1) == 1 等价于printProgress%2!=0
                    text = mPrintStr!!.substring(0, printProgress) + if (printProgress and 1 == 1) intervalChar else ""
                    listener?.printing(printProgress)
                } else {
                    // 如果完成打字,显示完整文字
                    text = mPrintStr
                    stopPrint()
                    listener?.over()
                }
            }
        }
    }

    companion object {
        /**
         * TAG
         */
        private val TAG = "PrinterTextView"
    }
}

interface OnPrintListener {
    fun over()
    fun start(mPrintStr: String)
    fun printing(process: Int)
}
