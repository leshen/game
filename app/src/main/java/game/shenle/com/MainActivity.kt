package game.shenle.com

import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobConfig
import game.shenle.com.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * 先介绍
 */
class MainActivity : BaseActivity<MainViewModel>() {
    override fun getTNameClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        setContentView(R.layout.activity_main)
        tv_init.text = "笔客江湖"
        Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(this.bindToLifecycle()).subscribe {
            //开始
            NewGameBeginActivity.goHere()
            Observable.timer(1, TimeUnit.SECONDS).subscribe{finish()}
        }
        initBob()
    }

    private fun initBob() {
        //第一：默认初始化
//        Bmob.initialize(this, "Your Application ID");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        Bmob.initialize(this, "832c88805561e96e49261e5674eefd71","bmob")

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

    companion object {
        fun goHere() {

        }
    }
}
