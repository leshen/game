package game.shenle.com

import kotlinx.android.synthetic.main.activity_main.*

/**
 * 先介绍
 */
class MainActivity : BaseActivity() {
    override fun initView() {
        setContentView(R.layout.activity_main)
        tv_init.text = "欢迎来到笔客江湖"
        //开始
        NewGameBeginActivity.goHere()
    }

    companion object {
        fun goHere(){

        }
    }
}
