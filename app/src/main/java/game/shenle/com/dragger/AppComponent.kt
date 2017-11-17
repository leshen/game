package game.shenle.com.dragger

import dagger.Component
import game.shenle.com.dragger.module.AppModule
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/16.
 */
@Singleton
@Component(modules = {
    AppModule.class
})
interface AppComponent {
}