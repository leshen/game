package game.shenle.com.dragger.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import game.shenle.com.NewUserCreateActivity;
import game.shenle.com.viewmodel.CreateJbViewModel;
import game.shenle.com.viewmodel.GameViewModel;
import game.shenle.com.viewmodel.MainViewModel;
import game.shenle.com.viewmodel.NewGameBeginViewModel;
import game.shenle.com.viewmodel.NewUserCreateViewModel;
import lib.shenle.com.dagger2.SLViewModelFactory;
import lib.shenle.com.dagger2.SLViewModelModule;
import lib.shenle.com.dagger2.ViewModelKey;

@Module(includes = SLViewModelModule.class)
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel.class)
    abstract ViewModel bindGameViewModel(GameViewModel gameViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewGameBeginViewModel.class)
    abstract ViewModel bindNewGameBeginViewModel(NewGameBeginViewModel newGameBeginViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(NewUserCreateViewModel.class)
    abstract ViewModel bindNewUserCreateViewModel(NewUserCreateViewModel newUserCreateViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(CreateJbViewModel.class)
    abstract ViewModel bindCreateJbViewModel(CreateJbViewModel createJbViewModel);
}
