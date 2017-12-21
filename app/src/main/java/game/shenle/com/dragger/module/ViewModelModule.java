package game.shenle.com.dragger.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import game.shenle.com.viewmodel.GameViewModel;
import game.shenle.com.viewmodel.MainViewModel;
import game.shenle.com.viewmodel.NewGameBeginViewModel;
import lib.shenle.com.dagger2.SLViewModelFactory;
import lib.shenle.com.dagger2.SLViewModelModule;
import lib.shenle.com.dagger2.ViewModelKey;

@Module(includes = SLViewModelModule.class)
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel.class)
    abstract ViewModel bindGameViewModel(GameViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewGameBeginViewModel.class)
    abstract ViewModel bindNewGameBeginViewModel(NewGameBeginViewModel repoViewModel);
}
