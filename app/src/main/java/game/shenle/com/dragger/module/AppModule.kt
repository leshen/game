package game.shenle.com.dragger.module

import android.app.Application
import android.arch.persistence.room.Room
import com.example.android.observability.persistence.AppDatabase
import com.example.android.observability.persistence.UserDao
import dagger.Module
import dagger.Provides
import game.shenle.com.AppExecutors
import game.shenle.com.db.repository.UserRepository
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/16.
 */
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, executor: AppExecutors): UserRepository {
        return UserRepository(userDao, executor)
    }

    @Provides
    @Singleton
    fun provideUserDao(db :AppDatabase): UserDao {
        return db.userDao()
    }
    @Provides
    @Singleton
    fun provideExecutor(): AppExecutors {
        return AppExecutors()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "bkjh.db").build()
    }
}