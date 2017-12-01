package game.shenle.com.dragger

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import game.shenle.com.MyApplication
import game.shenle.com.dragger.module.AppModule
import game.shenle.com.dragger.module.MainActivityModule
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/16.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class,
        AndroidInjectionModule::class,
        MainActivityModule::class))
interface AppComponent {
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun application(application: MyApplication): Builder
//        fun build(): AppComponent
//    }
    fun inject(application: MyApplication)
}