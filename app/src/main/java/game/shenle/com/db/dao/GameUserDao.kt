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
interface GameUserDao {

    /**
     * Insert a user in the database. If the user already exists, replace it.

     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameUser(user: GameUserDataTable)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateGameUser(user: GameUserDataTable)

    /**
     * Delete all users.
     */
    @Query("DELETE FROM GameUserDataTable")
    fun deleteAllUsers()
    @Query("SELECT * FROM GameUserDataTable WHERE userId = :userid AND jbId = :jbId")
    fun getGameUserById(userid:String,jbId: String): LiveData<GameUserDataTable>
}
