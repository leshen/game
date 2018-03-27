package game.shenle.com.viewmodel

import android.arch.lifecycle.LiveData
import com.example.android.observability.persistence.JbHttp
import com.example.android.observability.persistence.JbTable
import game.shenle.com.db.repository.JbRepository
import game.shenle.com.db.repository.Resource
import game.shenle.com.db.repository.UserRepository
import javax.inject.Inject

/**
 * Created by shenle on 2017/11/15.
 */
class MyJbListModel : BaseViewModel {
    private var jbRepo: JbRepository
    private var jbList: LiveData<Resource<List<JbTable>>>? = null
    private var userRepo: UserRepository
    @Inject
    constructor(jbRepo: JbRepository,userRepo: UserRepository):super(){
        this.jbRepo = jbRepo
        this.userRepo = userRepo
    }
    private var page=1
    fun init() {
        if (this.jbList != null) {
            return
        }
        jbList = jbRepo.getJbList(page,"w9rxDDDi",-1)//TODO 以后加注册登录
    }
    fun getJBList(): LiveData<Resource<List<JbTable>>>? {
        return jbList
    }
    fun loadNextJBList(): LiveData<Resource<List<JbTable>>> {
        this.page++
        return jbRepo.getJbList(page)
    }
}


