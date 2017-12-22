package game.shenle.com

import android.arch.lifecycle.Observer
import com.example.android.observability.persistence.UserTable
import game.shenle.com.view.OnPrintOverListener
import game.shenle.com.viewmodel.NewGameBeginViewModel
import kotlinx.android.synthetic.main.activity_new_game_begin.*
import lib.shenle.com.utils.UIUtils
import java.util.*

/**
 * 游戏开始界面
 * Created by shenle on 2017/11/14.
 */
class NewGameBeginActivity : BaseActivity<NewGameBeginViewModel>() {
    override fun getTNameClass(): Class<NewGameBeginViewModel> {
        return NewGameBeginViewModel::class.java
    }
    companion object {
        fun goHere() {
            UIUtils.startActivity(NewGameBeginActivity::class.java)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        viewModel.init(UUID.randomUUID().toString())
        tv_content.setPrintText("  检查进度中,请稍等....")
        tv_content.startPrint(object :OnPrintOverListener{
            override fun over() {
                //检查进度
                checkProgress()
            }
        })
    }

    private fun checkProgress() {
        // 检查进度(新游戏和档案)
        viewModel.getUser()?.observe(this, Observer<UserTable> {
            val userName = it?.userName
            if (userName.isNullOrEmpty()) {
                //新游戏
                GameActivity.goHere()
            } else {
                // 读取档案
                GameActivity.goHere()
            }
        })

    }
}