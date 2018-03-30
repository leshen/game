package game.shenle.com.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.BaseFragment
import game.shenle.com.R
import game.shenle.com.viewmodel.GameMainModel
import kotlinx.android.synthetic.main.fragment_game_controller.view.*

/**
 * 控制器
 * Created by shenle on 2017/12/1.
 */
class ControllerFragment : BaseFragment<GameMainModel>() {
    override fun getTNameClass(): Class<GameMainModel> {
        return GameMainModel::class.java
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
    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_game_controller,container,false)
        viewModel.getZlStr()?.observe(activity as LifecycleOwner, Observer {
                when (it) {
                    "[*指令_方向*]" -> {
                        view?.button1?.text = "东方"
                        view?.button1?.setOnClickListener{viewModel.doZl("东方")}
                        view?.button2?.text = "西方"
                        view?.button1?.setOnClickListener{viewModel.doZl("西方")}
                        view?.button3?.text = "南方"
                        view?.button1?.setOnClickListener{viewModel.doZl("南方")}
                        view?.button4?.text = "北方"
                        view?.button1?.setOnClickListener{viewModel.doZl("北方")}
                    }
                }
        })
        return view
    }
}