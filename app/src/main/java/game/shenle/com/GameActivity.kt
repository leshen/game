package game.shenle.com

import android.arch.lifecycle.Observer
import com.example.android.observability.persistence.User
import game.shenle.com.viewmodel.NewGameBeginViewModel
import java.util.*

/**
 * 游戏主界面
 * Created by shenle on 2017/11/14.
 */
class GameActivity : BaseActivity<NewGameBeginViewModel>() {
    companion object {
        fun goHere() {

        }
    }

    override fun initView() {
        setContentView(R.layout.activity_new_game_begin)
        //检查进度
    }

}