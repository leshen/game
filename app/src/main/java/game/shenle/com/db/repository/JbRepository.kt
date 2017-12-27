package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import com.example.android.observability.persistence.JbDao
import com.example.android.observability.persistence.JbTable
import com.example.android.observability.persistence.UserTable
import javax.inject.Singleton
import com.example.android.observability.persistence.UserDao
import game.shenle.com.dragger.AppExecutors
import javax.inject.Inject



/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class JbRepository {
    private val jbDao: JbDao
    private val executor: AppExecutors

    @Inject
    constructor(jbDao: JbDao, executor: AppExecutors){
        this.jbDao = jbDao
        this.executor = executor
    }

    fun getJb(jbId: String): LiveData<JbTable> {
        return jbDao.getJbById(jbId)!!
    }
}