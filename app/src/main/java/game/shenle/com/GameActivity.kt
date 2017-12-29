package game.shenle.com

import android.os.Bundle
import game.shenle.com.view.OnPrintOverListener
import game.shenle.com.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.activity_game.*
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
        fun goHere(id: String) {
            val bundle = Bundle()
            bundle.putString("jbId",id)
            UIUtils.startActivity(GameActivity::class.java,bundle)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_game)
        tv_content.setPrintText("很久很久以前...")
        tv_content.startPrint(object:OnPrintOverListener{
            override fun over() {

            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            val jbId = savedInstanceState.getLong("jbId")
            jbId
        }
    }
}