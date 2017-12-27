package game.shenle.com

import cn.bmob.v3.Bmob
import game.shenle.com.view.OnPrintOverListener
import game.shenle.com.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.graphics.Typeface



/**
 * 先介绍
 */
class MainActivity : BaseActivity<MainViewModel>() {
    override fun getTNameClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        setContentView(R.layout.activity_main)
        viewModel.init()
//        val typeface = Typeface.createFromAsset(assets, "fonnts/标准仿宋体简.ttf")
        val typeface = Typeface.createFromAsset(assets, "fonnts/标准楷体简.ttf")
//        val typeface = Typeface.createFromAsset(assets, "fonnts/标准隶书体简.TTF")
        tv_content.setTypeface(typeface)
        tv_content.setPrintText("江湖",1000,"　")
        tv_content.startPrint(object :OnPrintOverListener{
            override fun over() {
                tv_init.text = "笔客江湖"
                Observable.timer(2, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).compose(this@MainActivity.bindToLifecycle()).subscribe {
                    //开始
                    NewGameBeginActivity.goHere()
                    Observable.timer(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe{finish()}
                }
            }

        })
    }



    companion object {
        fun goHere() {

        }
    }
}
