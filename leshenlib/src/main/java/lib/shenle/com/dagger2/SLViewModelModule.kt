package lib.shenle.com.dagger2

import android.arch.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module

@Module
abstract class SLViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: SLViewModelFactory): ViewModelProvider.Factory
}
