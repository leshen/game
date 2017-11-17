package game.shenle.com.viewmodel

import game.shenle.com.db.repository.UserRepository
import javax.inject.Inject
import com.example.android.observability.persistence.User
import android.arch.lifecycle.LiveData

/**
 * Created by shenle on 2017/11/15.
 */
class NewGameBeginViewModel : BaseViewModel {
    private var userRepo: UserRepository
    private var user: LiveData<User>? = null
    @Inject
    constructor(userRepo: UserRepository){
        this.userRepo = userRepo
    }
    fun init(userId:String){
        if (this.user != null) {
            return
        }
        user = userRepo.getUser(userId)
    }

    fun getUser(): LiveData<User>? {
        return user
    }
}


