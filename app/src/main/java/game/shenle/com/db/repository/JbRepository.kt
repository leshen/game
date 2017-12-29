package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.example.android.observability.persistence.JbDao
import com.example.android.observability.persistence.JbHttp
import com.example.android.observability.persistence.JbTable
import game.shenle.com.dragger.AppExecutors
import lib.shenle.com.utils.TimeUtil
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class JbRepository {
    private val jbDao: JbDao
    private val executor: AppExecutors

    @Inject
    constructor(jbDao: JbDao, executor: AppExecutors) {
        this.jbDao = jbDao
        this.executor = executor
    }

    fun getJb(jbId: String): LiveData<Resource<JbTable>> {
        return object : NetworkBoundResource<JbTable>(executor) {
            override fun fetchFromNetwork(dbSource: LiveData<JbTable>) {
                val query = BmobQuery<JbHttp>()
                query.getObject(jbId, object : QueryListener<JbHttp>() {
                    override fun done(jbHttp: JbHttp?, e: BmobException?) {
                        if (e == null) {
                            onFetchSuccess(jbHttp?.toTable()!!, dbSource!!)
                        } else {
                            onFetchFailed(e, dbSource!!)
                        }
                    }
                })
            }

            override fun saveCallResult(item: JbTable) {
                jbDao.insertJb(item)
            }

            override fun shouldFetch(data: JbTable?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<JbTable> {
                return jbDao.getJbById(jbId)
            }
        }.asLiveData()
    }
    fun getJbList(page:Int): LiveData<Resource<List<JbTable>>> {
        return object : NetworkBoundResource<List<JbTable>>(executor) {
            override fun fetchFromNetwork(dbSource: LiveData<List<JbTable>>) {
                val query = BmobQuery<JbHttp>()
//              //查询playerName叫“比目”的数据
//              query.addWhereEqualTo("playerName", "比目")
//              返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(10)
                query.setSkip(10*(page-1))
//              执行查询方法
                query.findObjects(object : FindListener<JbHttp>() {
                    override fun done(jbTableList: List<JbHttp>?, e: BmobException?) {
                        if (e == null) {
                            val list = ArrayList<JbTable>()
                            for (i in jbTableList!!) {
                                list.add(i.toTable())
                            }
                            onFetchSuccess(list, dbSource!!)
                        } else {
                            onFetchFailed(e, dbSource!!)
                        }
                    }
                })
            }

            override fun saveCallResult(item: List<JbTable>) {
                for (i in item) {
                    jbDao.insertJb(i)
                }
            }

            override fun shouldFetch(data: List<JbTable>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<JbTable>> {
                return jbDao.getJbAll(10*(page-1)+1,10*page)
            }
        }.asLiveData()
    }
    fun submitJb(jbHttp: JbHttp) :LiveData<Resource<JbTable>>{
        return object : NetworkBoundResource<JbTable>(executor) {
            override fun fetchFromNetwork(dbSource: LiveData<JbTable>) {
                jbHttp.save(object : SaveListener<String>() {
                    override fun done(objectId: String?, e: BmobException?) {
                        if (e == null) {
                            jbHttp.objectId = objectId
                            val toTable = jbHttp.toTable()
                            toTable.updatedAt = TimeUtil.getChinaDateTime(System.currentTimeMillis())!!
                            onFetchSuccess(toTable, dbSource!!)
                        } else {
                            onFetchFailed(e, dbSource)
                        }
                    }
                })
            }

            override fun saveCallResult(item: JbTable) {
                jbDao.insertJb(item)
            }

            override fun shouldFetch(data: JbTable?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<JbTable> {
                return jbDao.getJbById(jbHttp.objectId?:"-1")
            }
        }.asLiveData()
    }
}