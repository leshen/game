package game.shenle.com

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import game.shenle.com.viewmodel.BaseViewModel
import lib.shenle.com.base.SLBaseActivity
import lib.shenle.com.base.SLBaseFragment
import lib.shenle.com.dagger2.SLViewModelFactory
import javax.inject.Inject


/**
 * Created by shenle on 2017/11/10.
 */
abstract class BaseFragment<T: BaseViewModel> : SLBaseFragment(), LifecycleOwner{
    lateinit var viewModel: T
    @Inject
    @JvmField var viewModelFactory: SLViewModelFactory?=null

    abstract fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(activity!!,viewModelFactory!!).get(getTNameClass())
        return initView(inflater,container,savedInstanceState)
    }

    abstract fun getTNameClass(): Class<T>
}