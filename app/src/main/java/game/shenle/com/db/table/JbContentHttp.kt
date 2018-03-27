package com.example.android.observability.persistence

import cn.bmob.v3.BmobObject
import com.google.gson.Gson
import game.shenle.com.utils.BaseParse

/**
 * 剧本章节
 */
data class JbContentHttp constructor(
        var zj_index: Int = 1,

        //剧本名称
        var jbTitle: String = "繁衍",

        //章节名称
        var zj_name: String = "第一章",

        //剧本章节总数
        var totalZj: Int = 1,
        //背景
        var bg: String = "那个啥,你穿越到了原始社会,努力活下去吧,你现在的初始数据是:[*初始化数据*]",

        //背景
        var bg2: String = "",

        //剧本id
        var jbId: String = "",
        //章节完成状态 0未完成 1完成
        var jbStatus: Int? = 0
) : BmobObject() {
    companion object {
        fun getInstance(table: JbContentTable): JbContentHttp {
            return BaseParse.parse(Gson().toJson(table).toString(), JbContentHttp::class.java)
        }
    }

    fun toTable(): JbContentTable {
        return BaseParse.parse(Gson().toJson(this).toString(), JbContentTable::class.java)
    }
}