package game.shenle.com.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.viewmodel.GameControllerModel
import game.shenle.com.viewmodel.GameMainModel
import lib.shenle.com.base.SLBaseFragment

/**
 * 控制器
 * Created by shenle on 2017/12/1.
 */
class ControllerFragment : BaseFragment<GameControllerModel>() {
    override fun getTNameClass(): Class<GameControllerModel> {
        return GameControllerModel::class.java
    }

    companion object {
        fun getInstance(jbId :String?):ControllerFragment{
            val gameUserInfoFragment = Instance.controllerFragment
            jbId?.let {
                val bundle = Bundle()
                bundle.putString("jbId",jbId)
                gameUserInfoFragment.arguments = bundle
            }
            return gameUserInfoFragment
        }
    }
    private object Instance{
        val controllerFragment  = ControllerFragment()
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_game_controller,container,false)
        return view
    }
}