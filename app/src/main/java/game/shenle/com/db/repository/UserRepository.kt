package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import com.example.android.observability.persistence.UserTable
import javax.inject.Singleton
import com.example.android.observability.persistence.UserDao
import game.shenle.com.dragger.AppExecutors
import javax.inject.Inject
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.BmobQuery
import com.example.android.observability.persistence.UserHttp


/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class UserRepository {
    private val userDao: UserDao
    private val executor: AppExecutors
    private var userTable: LiveData<Resource<UserTable>>?=null

    @Inject
    constructor(userDao: UserDao, executor: AppExecutors){
        this.userDao = userDao
        this.executor = executor
    }

    fun getUser(userId: String): LiveData<Resource<UserTable>>? {
        userTable = object : NetworkBoundResource<UserTable>(executor) {
            override fun fetchFromNetwork(dbSource: LiveData<UserTable>) {
                val query = BmobQuery<UserHttp>()
                query.getObject(userId, object : QueryListener<UserHttp>() {
                    override fun done(userHttp: UserHttp?, e: BmobException?) {
                        if (e == null) {
                            onFetchSuccess(userHttp?.toTable()!!, dbSource!!)
                        } else {
                            onFetchFailed(e, dbSource!!)
                        }
                    }
                })
            }

            override fun saveCallResult(item: UserTable) {
                userDao.insertUser(item)
            }

            override fun shouldFetch(data: UserTable?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<UserTable> {
                return userDao.getUserById(userId)
            }
        }.asLiveData()
        return userTable
    }
}