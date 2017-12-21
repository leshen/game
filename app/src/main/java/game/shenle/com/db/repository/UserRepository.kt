package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import com.example.android.observability.persistence.UserTable
import javax.inject.Singleton
import com.example.android.observability.persistence.UserDao
import game.shenle.com.dragger.AppExecutors
import javax.inject.Inject



/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class UserRepository {
    private val userDao: UserDao
    private val executor: AppExecutors

    @Inject
    constructor(userDao: UserDao, executor: AppExecutors){
        this.userDao = userDao
        this.executor = executor
    }

    fun getUser(userId: String): LiveData<UserTable> {
        refreshUser(userId)
        // return a LiveData directly from the database.
        return userDao.getUserById(userId)!!
    }

    private fun refreshUser(userId: String) {
        executor.diskIO().execute{
            // running in a background thread
            // check if user was fetched recently
            val user = userDao.getUserById(userId)
            user?:let {
                // 如果用服务端,这里从服务器加载
                userDao.insertUser(UserTable())
            }
        }
    }
}