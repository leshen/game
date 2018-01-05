package game.shenle.com.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.viewmodel.GameControllerModel
import game.shenle.com.viewmodel.GameUserInfoModel
import lib.shenle.com.base.SLBaseFragment

/**
 * 控制器
 * Created by shenle on 2017/12/1.
 */
class GameUserInfoFragment : BaseFragment<GameUserInfoModel>() {
    override fun getTNameClass(): Class<GameUserInfoModel> {
        return GameUserInfoModel::class.java
    }
    companion object {
        fun getInstance(jbId :String?):GameUserInfoFragment{
            val gameUserInfoFragment = Instance.gameUserInfoFragment
            jbId?.let {
                val bundle = Bundle()
                bundle.putString("jbId",jbId)
                gameUserInfoFragment.arguments = bundle
            }
            return gameUserInfoFragment
        }
    }
    private object Instance{
        val gameUserInfoFragment  = GameUserInfoFragment()
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_game_user_info,container,false)
        return view
    }
}