package game.shenle.com

import game.shenle.com.viewmodel.GameViewModel
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

    }

}