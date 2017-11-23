package game.shenle.com.dragger

import dagger.Component
import game.shenle.com.MyApplication
import game.shenle.com.dragger.module.AppModule
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: MyApplication)
}