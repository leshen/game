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
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.os.SystemClock
import game.shenle.com.db.table.BookInfoBean

/**
 * @Dao 指明为 Dao 类
 * @Insert 插入操作
 * @Update 更新操作
 * @Delete 删除操作
 * @Query 查询操作 ，需要指定 Sql 语句
 */
@Dao
interface BookInfoBeanDao {

    /**
     * Get a user by id.

     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM book_info LIMIT :s ,:e")
    fun getBookInfoBeanAll(s :Int,e :Int): LiveData<List<BookInfoBean>>
    @Query("SELECT * FROM book_info")
//    fun getUserById(id: String): Flowable<User>
    fun getBookInfoBeanAll(): LiveData<List<BookInfoBean>>
    /**
     * Get a user by id.

     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM book_info WHERE noteUrl = :md5")
//    fun getUserById(id: String): Flowable<User>
    fun getBookInfoByMd5(md5: String): LiveData<List<BookInfoBean>>

    /**
     * Insert a user in the database. If the user already exists, replace it.

     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookInfoBean(user: BookInfoBean)

    /**
     * Delete BookInfoBean.
     */
    @Query("DELETE FROM book_info WHERE id = :id")
    fun deleteBookInfoBean(id: String)
    /**
     * Delete BookInfoBean.
     */
    @Query("DELETE FROM book_info")
    fun deleteAll()
}
