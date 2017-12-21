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

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.SystemClock
import game.shenle.com.db.Converters
import java.util.*

@Entity(tableName = "users")
data class UserTable constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Long = 0L,
        @ColumnInfo(name = "u_create_date")
        var createDate: Date? = null,
        @ColumnInfo(name = "u_id")
        var userid: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "u_name")
        var userName: String? = null,
        @ColumnInfo(name = "u_phone")
        var userPhone: String? = null,
        @ColumnInfo(name = "u_update_date")
        var updateDate: Date? = null,
        @ColumnInfo(name = "u_status")
        var status: Int? = 0) {
    // 必须有公共构造方法
    constructor() : this(0, Date(System.currentTimeMillis()))
}