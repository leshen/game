package game.shenle.com

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.example.android.observability.persistence.User
import game.shenle.com.viewmodel.NewGameBeginViewModel
import java.util.*

/**
 * 游戏开始界面
 * Created by shenle on 2017/11/14.
 */
class NewGameBeginActivity : BaseActivity<NewGameBeginViewModel>() {
//    override fun getViewMolderClass(): Class<NewGameBeginViewModel> {
//        return NewGameBeginViewModel::class.java
//    }

    companion object {
        fun goHere() {

        }
    }

    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        viewModel.init(UUID.randomUUID().toString())
        //检查进度
        checkProgress()
    }

    private fun checkProgress() {
        // 检查进度(新游戏和档案)
        viewModel.getUser()?.observe(this, Observer<User> {
            val userName = it?.userName
            if (userName.isNullOrEmpty()) {
                //新游戏

            } else {
                //TODO 读取档案
            }
        })

    }
}