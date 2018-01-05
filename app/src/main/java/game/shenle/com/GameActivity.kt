package game.shenle.com

import android.app.Activity
import android.os.Bundle
import game.shenle.com.fragment.ControllerFragment
import game.shenle.com.fragment.GameUserInfoFragment
import game.shenle.com.fragment.MainGameFragment
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
    }

    private var jbId: String?=null

    override fun initData(savedInstanceState: Bundle?) {
        savedInstanceState?:let {
            jbId = intent.getStringExtra("jbId")//剧本id
            val gameUserInfoFragment = GameUserInfoFragment.getInstance(jbId)
            supportFragmentManager.beginTransaction().replace(R.id.fl_info,gameUserInfoFragment,"GameUserInfoFragment").commitAllowingStateLoss()
            val controllerFragment = ControllerFragment.getInstance(jbId)
            supportFragmentManager.beginTransaction().replace(R.id.fl_controller,controllerFragment,"ControllerFragment").commitAllowingStateLoss()
            val mainGameFragment = MainGameFragment.getInstance(jbId)
            supportFragmentManager.beginTransaction().replace(R.id.fl_main,mainGameFragment,"MainGameFragment").commitAllowingStateLoss()
        }
    }
    override fun getSlideActivity(): Activity {
        return this
    }

    override fun supportSlideBack(): Boolean {
        return false
    }

    override fun canBeSlideBack(): Boolean {
        return true
    }
}