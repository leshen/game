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

package com.example.android.observability.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.os.SystemClock

/**
 * @Dao 指明为 Dao 类
 * @Insert 插入操作
 * @Update 更新操作
 * @Delete 删除操作
 * @Query 查询操作 ，需要指定 Sql 语句
 */
@Dao
interface JbContentDao {

    /**
     * Get a user by id.

     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM JbContentTable LIMIT :s ,:e")
    fun getJbAll(s :Int,e :Int): LiveData<List<JbContentTable>>
    @Query("SELECT * FROM JbContentTable")
//    fun getUserById(id: String): Flowable<User>
    fun getJbAll(): LiveData<List<JbContentTable>>
    /**
     * Get a user by id.

     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM JbContentTable WHERE jbId = :id")
//    fun getUserById(id: String): Flowable<User>
    fun getJbById(id: String): LiveData<JbContentTable>
    @Query("SELECT * FROM JbContentTable WHERE jbId = :id AND zj_index BETWEEN :from AND :end")
//    fun getUserById(id: String): Flowable<User>
    fun getJbById(id: String,from:Long,end:Long): LiveData<List<JbContentTable>>

    /**
     * Insert a user in the database. If the user already exists, replace it.

     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJb(user: JbContentTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJb(user: List<JbContentTable>)
    @Update
    fun updateJbContent(user: JbContentTable)

    /**
     * Delete jb.
     */
    @Query("DELETE FROM JbContentTable WHERE id = :id")
    fun deleteJb(id: String)
    /**
     * Delete jb.
     */
    @Query("DELETE FROM JbContentTable")
    fun deleteAll()
}
