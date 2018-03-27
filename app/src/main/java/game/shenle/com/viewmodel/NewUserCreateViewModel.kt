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
class NewUserCreateViewModel : BaseViewModel {
    private var jbRepo: JbRepository
    private var jbList: LiveData<Resource<List<JbTable>>>? = null

    @Inject
    constructor(jbRepo: JbRepository):super(){
        this.jbRepo = jbRepo
    }
    private var page=1
    fun init() {
        if (this.jbList != null) {
            return
        }
        jbList = jbRepo.getJbList(page)
    }
    fun getJBList(): LiveData<Resource<List<JbTable>>>? {
        return jbList
    }
    fun loadNextJBList(): LiveData<Resource<List<JbTable>>> {
        this.page++
        return jbRepo.getJbList(page)
    }
}


