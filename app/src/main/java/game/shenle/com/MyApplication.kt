package game.shenle.com

import game.shenle.com.dragger.AppComponent
import game.shenle.com.dragger.DaggerAppComponent
import lib.shenle.com.base.SLBaseApplication

/**
 * Created by shenle on 2017/11/16.
 */
class MyApplication : SLBaseApplication() {
    override fun initDraggerApp() {
        component.inject(this)
    }

    val component: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }
}