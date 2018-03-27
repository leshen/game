/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package game.shenle.com.db.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import cn.bmob.v3.exception.BmobException

import java.util.Objects

import game.shenle.com.dragger.AppExecutors
import lib.shenle.com.utils.LogUtils
import lib.shenle.com.utils.UIUtils

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType> @MainThread
internal constructor(val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.setValue(Resource.loading<ResultType>(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                result.addSource(dbSource) { newData -> setValue(Resource.loading(newData)) }
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData -> setValue(Resource.success(newData)) }
            }
        }
    }

    abstract fun saveTable()

    abstract fun fetchFromNetwork(dbSource: LiveData<ResultType>)

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.setValue(newValue)
        }
    }

    fun onFetchSuccess(resultType: ResultType, dbSource: LiveData<ResultType>) {
        result.removeSource(dbSource)
        appExecutors.diskIO().execute {
            saveCallResult(resultType)
            appExecutors.mainThread().execute {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDb()
                ) { newData -> setValue(Resource.success(newData)) }
            }
        }
    }

    fun onFetchFailed(exception: BmobException, dbSource: LiveData<ResultType>) {
        LogUtils.e(exception.toString())
        result.removeSource(dbSource)
        result.addSource(dbSource
        ) { newData -> setValue(Resource.error(exception.message, newData)) }
    }


    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

}
