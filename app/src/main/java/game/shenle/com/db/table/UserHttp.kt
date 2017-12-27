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
import android.arch.persistence.room.PrimaryKey
import cn.bmob.v3.BmobObject
import game.shenle.com.db.table.UserTableInter
import game.shenle.com.utils.BaseParse
import org.json.JSONObject
import java.util.*

class UserHttp private constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        override var id: Long = 0L,
        @ColumnInfo(name = "u_create_date")
        override var createDate: Date? = null,
        @ColumnInfo(name = "u_create_jb")//剧本编号
        override var createDateJb: Int? = null,
        @ColumnInfo(name = "u_id")
        override var userid: String = UUID.randomUUID().toString(),
        @ColumnInfo(name = "u_name")
        override var userName: String? = null,
        @ColumnInfo(name = "u_phone")
        override var userPhone: String? = null,
        @ColumnInfo(name = "u_update_date")
        override var updateDate: Date? = null,
        @ColumnInfo(name = "u_status")
        override var status: Int? = 0) : BmobObject(),UserTableInter {
        companion object {
            fun getInstance(table :UserTable):UserHttp{
                    return BaseParse.parse(JSONObject().put("",table).toString(),UserHttp::class.java)
            }
        }
        fun toTable():UserTable{
                return BaseParse.parse(JSONObject().put("",this).toString(),UserTable::class.java)
        }
}
