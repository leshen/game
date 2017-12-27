package game.shenle.com

import game.shenle.com.view.OnPrintOverListener
import game.shenle.com.viewmodel.NewUserCreateViewModel
import kotlinx.android.synthetic.main.activity_user_create.*
import lib.shenle.com.utils.UIUtils

/**
 * 新建游戏角色界面
 * 人物设定(暂时正派和反派)
 * Created by shenle on 2017/11/14.
 */
class NewUserCreateActivity : BaseActivity<NewUserCreateViewModel>() {
    override fun getTNameClass(): Class<NewUserCreateViewModel> {
        return NewUserCreateViewModel::class.java
    }

    companion object {
        fun goHere() {
            UIUtils.startActivity(NewUserCreateActivity::class.java)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_user_create)
        viewModel.getJBinfo()
    }

}