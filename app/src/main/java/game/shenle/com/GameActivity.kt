package game.shenle.com

import game.shenle.com.view.OnPrintOverListener
import game.shenle.com.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.activity_new_game_begin.*
import lib.shenle.com.utils.UIUtils

/**
 * 游戏主界面
 * Created by shenle on 2017/11/14.
 */
class GameActivity : BaseActivity<GameViewModel>() {
    override fun getTNameClass(): Class<GameViewModel> {
        return GameViewModel::class.java
    }

    companion object {
        fun goHere() {
            UIUtils.startActivity(GameActivity::class.java)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        tv_content.setPrintText("很久很久以前...")
        tv_content.startPrint(object:OnPrintOverListener{
            override fun over() {

            }
        })
    }

}