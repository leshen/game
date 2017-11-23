package slmodule.shenle.com.utils

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.TextView

/**
 * Created by shenle on 2017/8/14.
 */
class SmsHelper {
    companion object {
        var country_code = ""
        /**
         * 发送验证码

         * @param et_phone
         * *
         * @param_code
         */
        fun sendYZM(phone: String, tv_code: TextView) {
            country_code = "+${getCurrentCountry(tv_code.context)?.get(1)}"
            SMSSDK.getVerificationCode(country_code, phone)
            /**
             * 倒计时 30秒后可以重新发送验证码
             */
            val mc = MyCountTime((30 * 1000).toLong(), 1000, tv_code)
            mc.start()
        }
        fun onSubmit(context: Context, phone: String, code:String){
            if(country_code.isEmpty())
                country_code = "+${getCurrentCountry(context)?.get(1)}"
            SMSSDK.submitVerificationCode(country_code, phone, code)
        }

        fun getMCC(context : Context): String {
            val tm = context.getSystemService("phone") as TelephonyManager
            val networkOperator = tm.networkOperator
            return if (!TextUtils.isEmpty(networkOperator)) networkOperator else tm.simOperator
        }

        fun getCurrentCountry(context : Context): Array<String> ?{
            val mcc = this.getMCC(context)
            var country: Array<String>? = null
            if (!TextUtils.isEmpty(mcc)) {
                country = SMSSDK.getCountryByMCC(mcc)
            }
            if (country == null) {
                SMSLog.getInstance().d("no country found by MCC: " + mcc, *arrayOfNulls<Any>(0))
                country = SMSSDK.getCountry("42")
            }
            return country
        }
    }
    /**
     * 定时发送验证码类

     * @author shenle
     */
    class MyCountTime/* 定义一个倒计时的内部类 */
    (millisInFuture: Long, countDownInterval: Long,
     private val tv_code: TextView) : CountDownTimer(millisInFuture, countDownInterval) {
        private var currentTextColor: Int = 0

        override fun onFinish() {
            tv_code.text = "发验证码"
            tv_code.setTextColor(currentTextColor)
            tv_code.isClickable = true
            tv_code.isFocusable = true
        }

        override fun onTick(millisUntilFinished: Long) {
            tv_code.text = (millisUntilFinished / 1000).toString() + "s后重发"
            currentTextColor = tv_code.currentTextColor
            tv_code.setTextColor(Color.parseColor("#999999"))
            tv_code.isClickable = false
            tv_code.isFocusable = false
        }
    }
}