package game.shenle.com

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory!!).get(getTNameClass())
    }
    abstract fun getTNameClass(): Class<T>
}