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
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.os.SystemClock
import cn.bmob.v3.BmobObject
import game.shenle.com.db.Converters
import java.util.*

@Entity(foreignKeys = arrayOf(ForeignKey(entity = JbTable::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("jbId"))))
data class GameUserDataTable constructor(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var objectId: String = "",

        var createdAt: String? = null,

        var updatedAt: String? = null,

        //剧本id
        var jbId: String = "",
        var userId: String = "",

        var gameUserName: String? = null,
        var sex: Int? = null,//0男 ,1 女,2太监,3兽
        var jed: Long? = null,//饥饿度(0-100)
        var xt: Long? = null,//血条(0-100)
        var jdzt: Long? = null,//健康状态(0~100,疾病(...),中毒)
        var xqz: Long? = null,//心情值(-100~0~100)
        var wlz: Long? = null,//武力值(0~)
        var zsz: Long? = null,//智商(0~)
        var zyz: Long? = null//幸运值(0~100*n)


//        "人物属性" +
//"饥饿度(0-100)" +
//"健康状态(0~100,疾病(...),中毒)" +
//"心情值(-100~0~100)影响情绪,作出的决定" +
//"武力值(0~)" +
//"智商(0~)" +
//"幸运值(0~100)"
     ) {
    override fun toString():String{
        return "\n姓名:${gameUserName?:"未知"}\n" +
                "性别:${when(sex){
                    0 -> "男"
                    1 -> "女"
                    2 -> "太监"
                    3 -> "兽"
                    else ->"未知"}}\n" +
                "饥饿度:${jed?.let { jed.toString() }?:"未知"}\n" +
                "血条:${xt?.let { xt.toString() }?:"未知"}\n" +
                "健康状态:${jdzt?.let { jdzt.toString() }?:"未知"}\n" +
                "心情值:${xqz?.let { xqz.toString() }?:"未知"}\n" +
                "武力值:${wlz?.let { wlz.toString() }?:"未知"}\n" +
                "智商:${zsz?.let { zsz.toString() }?:"未知"}\n" +
                "幸运值:${zyz?.let { zyz.toString() }?:"未知"}\n"
    }
}