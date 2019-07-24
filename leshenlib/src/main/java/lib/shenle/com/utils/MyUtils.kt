package lib.shenle.com.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.text.style.ImageSpan
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern


/**
 * 开发中经常用到的工具
 *
 * @author shenle
 */
class MyUtils {

    /**
     * 居中图片ImageSpan
     */
    class CenteredImageSpan : ImageSpan {

        constructor(context: Context, drawableRes: Int) : super(context, drawableRes) {}

        constructor(drawableRes: Drawable) : super(drawableRes) {}

        override fun draw(canvas: Canvas, text: CharSequence,
                          start: Int, end: Int, x: Float,
                          top: Int, y: Int, bottom: Int, paint: Paint) {
            // image to draw
            val b = drawable
            // font metrics of text to be replaced
            val fm = paint.fontMetricsInt
            val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2

            canvas.save()
            canvas.translate(x, transY.toFloat())
            b.draw(canvas)
            canvas.restore()
        }
    }

    companion object {

        /**
         * textView设置DrawableLeft
         *
         * @param i
         * @param tv
         */
        fun setLeftDrawable(i: Int, tv: TextView) {
            val drawable = UIUtils.context.resources.getDrawable(i)
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth,
                    drawable.minimumHeight)
            tv.setCompoundDrawables(drawable, null, null, null)
        }

        /**
         * textView设置DrawableLeft
         *
         * @param i
         * @param tv
         */
        fun setRightDrawable(i: Int, tv: TextView) {
            val drawable = UIUtils.context.resources.getDrawable(i)
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.minimumWidth,
                    drawable.minimumHeight)
            tv.setCompoundDrawables(null, null, drawable, null)
        }

        /**
         * textView设置DrawableLeft
         *
         * @param i
         * @param tv
         */
        fun setLeftDrawable(i: Int, tv: TextView, right: Int, bottom: Int) {
            if (i == -1) {
                tv.setCompoundDrawables(null, null, null, null)
            } else {
                val drawable = UIUtils.context.resources.getDrawable(i)
                // / 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, right,
                        bottom)
                tv.setCompoundDrawables(drawable, null, null, null)
            }
        }



        /**
         * 验证手机号是否合法
         *
         * @param context
         * @param etPhone
         * @return
         */
        fun isPhoneValid(context: Context, etPhone: EditText): Boolean {
            var isValid = true
            val phone = etPhone.text.toString().trim { it <= ' ' }
            val length = phone.length
            if (TextUtils.isEmpty(phone)) {
                isValid = false
            } else {
                val p = Pattern
                        .compile("^(1)\\d{10}$")
                val m = p.matcher(phone)
                if (length > 11 || !m.matches()) {
                    isValid = false
                }
            }
            if (!isValid) {
                Toast.makeText(context, "请输入11位有效手机号", Toast.LENGTH_SHORT).show()
            }
            return isValid
        }


        /**
         * 弹出日期选择器
         *
         * @param context
         * @param textView
         * @param callback
         */
        fun seleteData(context: Context, textView: TextView,
                       callback: AlertDialogInterface) {
            val calendar = Calendar.getInstance()
            // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
            DatePickerDialog(context,
                    // 绑定监听器
                    DatePickerDialog.OnDateSetListener { dp, year, month, dayOfMonth ->
                        textView.text = (year.toString() + "-" + (month + 1) + "-"
                                + dayOfMonth)
                        callback.onClickYes(textView.text.toString())
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))// 设置初始日期
                    .show()
        }

        /**
         * text为空,控件父类不显示
         *
         * @param name
         * @param view
         */
        fun setInfo(name: String, view: TextView) {
            val parent = view.parent as ViewGroup
            if (UIUtils.isEmpty(name)) {
                if (parent != null)
                    parent.visibility = View.GONE
            } else {
                view.text = name
                if (parent != null)
                    parent.visibility = View.VISIBLE
            }
        }

        /**
         * text为空,控件不显示
         *
         * @param name
         * @param view
         * @param content
         */
        @JvmOverloads
        fun setInfoSelf(name: String, view: TextView, content: String = name) {
            if (UIUtils.isEmpty(name)) {
                view.visibility = View.GONE
            } else {
                view.text = content
                view.visibility = View.VISIBLE
            }
        }

        /**
         * 从字符串里获取手机号
         *
         * @param sParam
         * @return
         */
        fun getTelnum(sParam: String): String {
            return getZZ(sParam, "(1|861)(3|4|5|7|8)\\d{9}$*")
        }

        /**
         * 从字符串里获取
         *
         * @param sParam
         * @return
         */
        fun getZZ(sParam: String, zz: String): String {
            if (sParam.length <= 0)
                return ""
            val pattern = Pattern.compile(zz)
            val matcher = pattern.matcher(sParam)
            val bf = StringBuffer()
            while (matcher.find()) {
                bf.append(matcher.group()).append(",")
            }
            val len = bf.length
            if (len > 0) {
                bf.deleteCharAt(len - 1)
            }
            return bf.toString()
        }

        fun saveObject2Locate(activity: Activity, key: String): JSONObject? {
            val mCache = ACache.get(activity)
            var cache: JSONObject? = null
            if (mCache != null) {
                cache = mCache.getAsJSONObject(key)
            }
            return cache
        }

        /**
         * 计算文本的长度是否超过最大行
         *
         * @param text
         * @return
         */
        fun isGTmaxLines(textView: TextView, marginAndPadding: Int, text: String, context: Context): Boolean {
            val MAX_LINES = 3
            var lines = 0
            // 获得字体的宽度，sp转px
            val txtWidth = UIUtils.sp2px(context, 14f)
            // 获得屏幕的宽度 30为margin+padding总和
            val viewWidth = UIUtils.screenWidth - UIUtils.dip2px(marginAndPadding.toFloat())
            // 获得单行最多显示字数
            val maxWords = viewWidth / txtWidth
            // 计算字符串长度，
            val stringLen = text.length
            if (text.contains("\n")) {
                val strs = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var str_lines: Int
                for (i in strs.indices) {
                    str_lines = strs[i].length / maxWords
                    if (strs[i].length % maxWords > 0) {
                        str_lines++
                    }
                    lines += str_lines
                }
            } else {
                lines = stringLen / maxWords// 字符串长度除以单行最多显示字数为行数
            }
            if (lines > MAX_LINES) {
                // 如果大于指定行数，则直接返回
                return true
            } else if (lines == MAX_LINES) {
                // 否则需要判断下是否等于最大行，但是有余数
                if (stringLen % maxWords > 0) {
                    return true
                }
            }
            return false
        }

        /**
         * 打开允许通知的设置页
         */
        fun goToNotificationSetting(context: Context) {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            } else {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", context.packageName, null)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        /**
         * 获取所有满足正则表达式的字符串
         * @param str 需要被获取的字符串
         * @param regex_front id前的字符串
         * @return 所有满足正则表达式的字符串
         */
        fun getZZId(str: String?, regex_front: String): String? {
            if (str == null || str.isEmpty()) {
                return null
            }
            val regex = "$regex_front(?:\\d+)"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            var id: String? = null
            if (matcher.find()) {
                id = matcher.group().replace(regex_front, "")
            }
            return id
        }
        /**
         * 获取所有满足正则表达式的字符串
         * @param str 需要被获取的字符串
         * @param regex_front id后的字符串
         * @return 所有满足正则表达式的字符串
         */
        fun getZZIdEnd(str: String?, regex_front: String): String? {
            if (str == null || str.isEmpty()) {
                return null
            }
            val regex = "(?:\\d+)$regex_front"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            var id: String? = null
            if (matcher.find()) {
                id = matcher.group().replace(regex_front, "")
            }
            return id
        }
    }
}
interface AlertDialogInterface {
    fun onClickYes(title: String)

}
