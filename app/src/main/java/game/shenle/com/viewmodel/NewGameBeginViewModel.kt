package game.shenle.com.viewmodel

import game.shenle.com.db.repository.UserRepository
import javax.inject.Inject
import com.example.android.observability.persistence.UserTable
import android.arch.lifecycle.LiveData
import game.shenle.com.db.repository.Resource

/**
 * Created by shenle on 2017/11/15.
 */

class NewGameBeginViewModel : BaseViewModel {
    private var userRepo: UserRepository
    private var user: LiveData<Resource<UserTable>>? = null
    @Inject
    constructor(userRepo: UserRepository):super(){
        this.userRepo = userRepo
    }
    fun init(userId:String){
        if (this.user != null) {
            return
        }
        user = userRepo.getUser(userId)
    }

    fun getUser(): LiveData<Resource<UserTable>>? {
        return user
    }
}


