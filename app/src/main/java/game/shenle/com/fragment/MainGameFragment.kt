package game.shenle.com.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.viewmodel.GameMainModel

/**
 * 主界面
 * Created by shenle on 2017/12/1.
 */
class MainGameFragment : BaseFragment<GameMainModel>() {
    override fun getTNameClass(): Class<GameMainModel> {
        return GameMainModel::class.java
    }
    companion object {
        fun getInstance(jbId :String?): MainGameFragment {
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
        val controllerFragment  = MainGameFragment()
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main_game,container,false)
        viewModel.init()
        return view
    }
}