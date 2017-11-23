package game.shenle.com

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import game.shenle.com.viewmodel.BaseViewModel
import lib.shenle.com.base.SLBaseActivity


/**
 * Created by shenle on 2017/11/10.
 */
abstract class BaseActivity<T: BaseViewModel> : SLBaseActivity(), LifecycleOwner{
    lateinit var viewModel: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(this.localClassName::class.java as Class<T>)
        initView()
    }

    abstract fun initView()
}