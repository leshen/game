package game.shenle.com.db

import android.arch.persistence.room.TypeConverter
import cn.bmob.v3.datatype.BmobDate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by yuan on 02/08/2017.
 * 转换器
 */
class Converters {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    /**
     * 时间戳转日期
     */
    @TypeConverter
    fun fromTimestamp(value: String?): Date {
        if (value == null) {
            return Date(System.currentTimeMillis())
        }
        synchronized(format) {
            return format.parse(value)
        }
    }

    /**
     * 日期转时间
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): String {
        if (date == null) {
            val nowDate = Date(System.currentTimeMillis())
            synchronized(format) {
                return format.format(nowDate)
            }
        }
        synchronized(format) {
            return format.format(date)
        }
    }

    /**
     * 时间戳转日期
     */
    @TypeConverter
    fun timestampToBmobDate(value: String?): BmobDate {
        if (value == null) {
            return BmobDate(Date(System.currentTimeMillis()))
        }
        synchronized(format) {
            return BmobDate(format.parse(value))
        }
    }

    /**
     * 日期转时间
     */
    @TypeConverter
    fun bmobDateToTimestamp(date: BmobDate?): String {
        if (date == null) {
            val nowDate = Date(System.currentTimeMillis())
            synchronized(format) {
                return format.format(nowDate)
            }
        }
        synchronized(format) {
            return date.date
        }
    }

    @TypeConverter
    fun listToString(list: List<String>?): String {
        if (list==null||list.size==0)
            return ""
        val result = StringBuilder()
        list.forEachIndexed{index, str ->
            when (index){
                0 or list.size->result.append(str)
                else->result.append(","+str)
            }
        }
        return result.toString()
    }

    @TypeConverter
    fun stringToList(arr: String): List<String>? {
        return arr.split(",")
    }
}