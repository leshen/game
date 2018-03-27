package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import com.example.android.observability.persistence.*
import game.shenle.com.dragger.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by shenle on 2017/11/15.
 */
@Singleton // 告诉 Dagger 这个类只应该构造一次
class GameUserRepository {
    private val gameUserDao: GameUserDao
    private val executor: AppExecutors

    @Inject
    constructor(gameUserDao: GameUserDao, executor: AppExecutors){
        this.gameUserDao = gameUserDao
        this.executor = executor
    }

    fun getGameUserContent(userId:String,jbId: String): LiveData<Resource<GameUserDataTable>> {
        return object : NetworkBoundResource<GameUserDataTable>(executor) {
            override fun saveTable() {
                gameUserDao.insertGameUser(GameUserDataTable())
            }

            override fun fetchFromNetwork(dbSource: LiveData<GameUserDataTable>) {
                val query = BmobQuery<GameUserDataHttp>()
                val query1 = BmobQuery<GameUserDataHttp>()
                query1.addWhereEqualTo("userId",userId)
                val query2 = BmobQuery<GameUserDataHttp>()
                query2.addWhereEqualTo("jbId",jbId)
                val arrayList = ArrayList<BmobQuery<GameUserDataHttp>>()
                arrayList.add(query1)
                arrayList.add(query2)
                query.and(arrayList)
                query.findObjects(object : FindListener<GameUserDataHttp>() {
                    override fun done(list: List<GameUserDataHttp>?, e: BmobException?) {
                        if (e == null) {
                            if (list!=null)
                                for (item in list){
                                    onFetchSuccess(item.toTable(), dbSource!!)
                                    break
                                }
                        } else {
                            onFetchFailed(e, dbSource!!)
                        }
                    }
                })
            }

            override fun saveCallResult(item: GameUserDataTable) {
                gameUserDao.insertGameUser(item)
            }

            override fun shouldFetch(data: GameUserDataTable?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<GameUserDataTable> {
                return gameUserDao.getGameUserById(userId,jbId)
            }
        }.asLiveData()
    }

    fun saveHttpTable(gameUserDataHttp: GameUserDataHttp) {
        gameUserDataHttp.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
            }
        })
    }
    fun saveTable(gameUserDataTable: GameUserDataTable) {
        executor.diskIO().execute {
            gameUserDao.insertGameUser(gameUserDataTable)
        }
    }
}