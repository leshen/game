package game.shenle.com.dragger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import game.shenle.com.fragment.ControllerFragment

/**
 * Created by shenle on 2017/12/1.
 */
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    internal abstract fun contributeControllerFragment(): ControllerFragment
}