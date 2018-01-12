package game.shenle.com.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import game.shenle.com.db.repository.Resource
import game.shenle.com.db.repository.Status
import lib.shenle.com.base.SlBaseAdapter

/**
 * Created by shenle on 2018/1/10.
 */
class BombHelper {
    companion object {
        fun <T> setOnLoadMoreListener(data:LiveData<Resource<List<T>>>, owner : LifecycleOwner, baseAdapter: SlBaseAdapter<T>?){
            data.observe(owner, Observer {
                if (it?.status == Status.ERROR) {
                    baseAdapter?.loadMoreFail()
                } else if (it?.status == Status.SUCCESS) {
                    baseAdapter?.addData(baseAdapter?.data!!.size - 1, it?.data!!)
                    if (it?.data?.size!! < 10) {
                        baseAdapter?.loadMoreEnd()
                    } else {
                        baseAdapter?.loadMoreComplete()
                    }
                }
            })
        }
    }
}