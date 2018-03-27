package game.shenle.com.viewmodel

import android.arch.lifecycle.LiveData
import android.support.design.widget.TextInputLayout
import com.example.android.observability.persistence.JbHttp
import com.example.android.observability.persistence.JbTable
import com.example.android.observability.persistence.UserTable
import game.shenle.com.db.repository.JbRepository
import game.shenle.com.db.repository.Resource
import javax.inject.Inject

/**
 * Created by shenle on 2017/11/15.
 */
class EditJbViewModel : BaseViewModel {
    private var jbRepo: JbRepository
    @Inject
    constructor(jbRepo: JbRepository):super(){
        this.jbRepo = jbRepo
    }
    fun submitJb(jbHttp: JbHttp):LiveData<Resource<JbTable>>{
        return jbRepo.submitJb(jbHttp)
    }
}


