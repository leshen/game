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

@Entity(tableName = "users")
data class UserTable constructor(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "u_id")
        var objectId: String = "",

        @ColumnInfo(name = "u_create_date")
        var createdAt: String? = null,
        @ColumnInfo(name = "u_update_date")//更新时间
        var updatedAt: 	String? = null,

        @ColumnInfo(name = "jb_id")//剧本编号
        var jbId: String? = null,

        @ColumnInfo(name = "u_name")
        var username: String? = null,

        @ColumnInfo(name = "u_phone")
        var mobliePhoneNumber: String? = null,

        @ColumnInfo(name = "u_status")
        var status: Int? = 0) {
}