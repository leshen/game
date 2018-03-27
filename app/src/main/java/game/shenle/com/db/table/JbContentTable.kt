package com.example.android.observability.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import game.shenle.com.view.contentswitchview.BookContentView

/**
 * 剧本章节
 */
@Entity(foreignKeys = arrayOf(ForeignKey(entity = JbTable::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("jbId"))))
data class JbContentTable constructor(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo
        var zj_index: Int = 0,

        @ColumnInfo(name = "id")
        var objectId: String = "",

        @ColumnInfo//创建时间
        var createdAt: String? = null,

        @ColumnInfo//更新时间
        var updatedAt: String? = null,

        @ColumnInfo//剧本名称
        var jbTitle: String = "",

        @ColumnInfo//章节名称
        var zj_name: String = "",
        //剧本id
        var jbId: String = "",
        //剧本章节总数
        var totalZj: Int = 1,
        //当前章节位置   用页码
        var zj_self_index: Int = BookContentView.DURPAGEINDEXBEGIN,
        @ColumnInfo//背景
        var bg: String = "那个啥,你穿越到了原始社会,努力活下去吧,你现在的初始数据是:[*初始化数据*]",

        @ColumnInfo//背景
        var bg2: String = "",
//        @ColumnInfo
//        val list: List<String>?=null,













        @ColumnInfo//章节完成状态 0未完成 1完成
        var jbStatus: Int? = 0
)