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
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobDate
import com.google.gson.Gson
import game.shenle.com.utils.BaseParse
import java.util.*

class _User private constructor(

        @ColumnInfo(name = "jb_id")//剧本编号
        var jbId: String? = null,

        @ColumnInfo(name = "u_phone")
        var mobliePhoneNumber: String? = null,
        @ColumnInfo(name = "u_name")
        var username: String? = null,
        @ColumnInfo(name = "u_status")
        var status: Int? = 0)  : BmobObject() {
        companion object {
            fun getInstance(table :UserTable):_User{
                    return BaseParse.parse(Gson().toJson(table).toString(),_User::class.java)
            }
        }
        fun toTable():UserTable{
                return BaseParse.parse(Gson().toJson(this).toString(),UserTable::class.java)
        }
}
