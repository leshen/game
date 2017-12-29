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
class CreateJbViewModel : BaseViewModel {
    private var jbRepo: JbRepository
    @Inject
    constructor(jbRepo: JbRepository):super(){
        this.jbRepo = jbRepo
    }
    fun submitJb(jbHttp: JbHttp):LiveData<Resource<JbTable>>{
        return jbRepo.submitJb(jbHttp)
    }

    fun checkOk(tl_title: TextInputLayout, tl_jj: TextInputLayout):Boolean {
        val title = tl_title.editText?.getText().toString()
        val jj = tl_jj.editText?.getText().toString()
        if(title.isNullOrEmpty()){
            tl_title.error = "标题不能为空"
            return false
        }
        if(jj.isNullOrEmpty()){
            tl_jj.error = "简介不能为空"
            return false
        }
        return true
    }
}


