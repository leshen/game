package game.shenle.com.viewmodel

import android.graphics.Typeface
import cn.bmob.v3.Bmob
import game.shenle.com.NewGameBeginActivity
import game.shenle.com.R.id.tv_content
import game.shenle.com.R.id.tv_init
import game.shenle.com.view.OnPrintOverListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import lib.shenle.com.utils.UIUtils
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by shenle on 2017/11/15.
 */
class MainViewModel : BaseViewModel {
    @Inject
    constructor():super()

    fun init() {
        initBob()
    }
    private fun initBob() {
        //第一：默认初始化
//        Bmob.initialize(this, "Your Application ID");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        Bmob.initialize(UIUtils.context, "832c88805561e96e49261e5674eefd71","bmob")

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        val config = BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }
}