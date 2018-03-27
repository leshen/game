package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.SaveListener
import com.example.android.observability.persistence.JbContentDao
import com.example.android.observability.persistence.JbContentHttp
import com.example.android.observability.persistence.JbContentTable
import game.shenle.com.dragger.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class JbContentRepository {
    private val jbContentDao: JbContentDao
    private val executor: AppExecutors

    @Inject
    constructor(jbContentDao: JbContentDao, executor: AppExecutors) {
        this.jbContentDao = jbContentDao
        this.executor = executor
    }

    fun getJbContent(jbId: String,from :Long,end:Long): LiveData<Resource<List<JbContentTable>>> {
        return object : NetworkBoundResource<List<JbContentTable>>(executor) {
            override fun saveTable() {
                jbContentDao.insertJb(JbContentTable())
            }

            override fun fetchFromNetwork(dbSource: LiveData<List<JbContentTable>>) {
                val query = BmobQuery<JbContentHttp>()
                val query1 = BmobQuery<JbContentHttp>()
                query1.addWhereLessThanOrEqualTo("zj_index",end)
                val query2 = BmobQuery<JbContentHttp>()
                query2.addWhereGreaterThanOrEqualTo("zj_index",from)
                val query3 = BmobQuery<JbContentHttp>()
                query3.addWhereEqualTo("jbId",jbId)
                val arrayList = ArrayList<BmobQuery<JbContentHttp>>()
                arrayList.add(query1)
                arrayList.add(query2)
                arrayList.add(query3)
                query.and(arrayList)
                query.findObjects(object : FindListener<JbContentHttp>() {

                    override fun done(list: List<JbContentHttp>?, e: BmobException?) {
                        if (e == null) {
                            var list_table = ArrayList<JbContentTable>()
                            if (list!=null)
                            for (item in list){
                                list_table.add(item.toTable())
                            }
                            onFetchSuccess(list_table, dbSource!!)
                        } else {
                            onFetchFailed(e, dbSource!!)
                        }
                    }
                })
            }

            override fun saveCallResult(item: List<JbContentTable>) {
                jbContentDao.insertJb(item)
            }

            override fun shouldFetch(data: List<JbContentTable>?): Boolean {
                return data == null||data.size==0
            }

            override fun loadFromDb(): LiveData<List<JbContentTable>> {
                return jbContentDao.getJbById(jbId,from-1,end+1)
            }
        }.asLiveData()
    }
    fun upDataTable(jbContentTable:JbContentTable?){
        executor.diskIO().execute {
            if (jbContentTable!=null) {
                jbContentDao.updateJbContent(jbContentTable)
            }}
    }
    fun saveHttpTable(jbContentHttp:JbContentHttp) {
        jbContentHttp.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
            }
        })
    }
}