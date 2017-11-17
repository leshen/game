package game.shenle.com

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import game.shenle.com.viewmodel.BaseViewModel


/**
 * Created by shenle on 2017/11/10.
 */
abstract class BaseActivity<T: BaseViewModel> : RxAppCompatActivity(), LifecycleOwner {
    lateinit var viewModel: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(getViewMolderClass())
        initView()
    }

    abstract fun getViewMolderClass(): Class<T>
    abstract fun initView()
}