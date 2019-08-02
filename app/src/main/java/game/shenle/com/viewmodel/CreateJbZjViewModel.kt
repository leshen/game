package game.shenle.com.viewmodel

import android.arch.lifecycle.LiveData
import android.support.design.widget.TextInputLayout
import com.example.android.observability.persistence.*
import game.shenle.com.db.repository.JbContentRepository
import game.shenle.com.db.repository.JbRepository
import game.shenle.com.db.repository.Resource
import javax.inject.Inject

/**
 * Created by shenle on 2017/11/15.
 */
class CreateJbZjViewModel : BaseViewModel {
    private var jbContentRepository: JbContentRepository
    @Inject
    constructor(jbContentRepository: JbContentRepository):super(){
        this.jbContentRepository = jbContentRepository
    }
    fun submitJbZj(jbHttp: JbContentHttp):LiveData<Resource<JbContentTable>>{
        return jbContentRepository.saveHttpTable(jbHttp)
    }
    fun onInputZhiLing(zhiLing:String){

    }
}


