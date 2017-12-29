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
import com.google.gson.Gson
import game.shenle.com.utils.BaseParse

data class JbHttp constructor(
        @ColumnInfo(name = "jb_title")//剧本名称
        var jbTitle: String = "",

        @ColumnInfo(name = "jb_content")//剧本简介
        var jbContent: String = "",

        @ColumnInfo(name = "jb_auth")//作者
        var userName: String? = null,

        @ColumnInfo(name = "jb_auth_phone")//作者电话
        var jbAuthPhone: String? = null,

        @ColumnInfo(name = "jb_status")//剧本完成状态 0未完成 1完成
        var jbStatus: Int? = 0)  : BmobObject(){
        companion object {
                fun getInstance(table :JbTable):JbHttp{
                        return BaseParse.parse(Gson().toJson(table).toString(),JbHttp::class.java)
                }
        }
        fun toTable():JbTable{
                return BaseParse.parse(Gson().toJson(this).toString(),JbTable::class.java)
        }}