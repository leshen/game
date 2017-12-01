package game.shenle.com.dragger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import game.shenle.com.MainActivity

/**
 * Created by shenle on 2017/12/1.
 */
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentBuildersModule::class))
    internal abstract fun contributeMainActivity(): MainActivity
}