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

import cn.bmob.v3.BmobObject
import com.google.gson.Gson
import game.shenle.com.utils.BaseParse

data class GameUserDataHttp constructor(
        //剧本id
        var jbId: String = "",
        var userId: String = "w9rxDDDi",

        var gameUserName: String? = "乐",
        var sex: Int? = 0,//0男 ,1 女,2太监,3兽
        var jed: Long? = 100,//饥饿度(0-100)
        var xt: Long? = 100,//血条(0-100)
        var jdzt: Long? = 100,//健康状态(0~100,疾病(...),中毒)
        var xqz: Long? = 0,//心情值(-100~0~100)
        var wlz: Long? = 1,//武力值(0~)
        var zsz: Long? = 80,//智商(0~)
        var zyz: Long? = 0//幸运值(-100*n~100*n)

) : BmobObject() {
    companion object {
        fun getInstance(table: GameUserDataHttp): GameUserDataHttp {
            return BaseParse.parse(Gson().toJson(table).toString(), GameUserDataHttp::class.java)
        }
    }

    fun toTable(): GameUserDataTable {
        return BaseParse.parse(Gson().toJson(this).toString(), GameUserDataTable::class.java)
    }
}