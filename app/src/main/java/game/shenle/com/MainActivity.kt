package game.shenle.com

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
    override fun getViewMolderClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        setContentView(R.layout.activity_main)
        tv_init.text = "欢迎来到笔客江湖"
        Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(this.bindToLifecycle()).subscribe {
            //开始
            NewGameBeginActivity.goHere()
        }
    }

    companion object {
        fun goHere() {

        }
    }
}
