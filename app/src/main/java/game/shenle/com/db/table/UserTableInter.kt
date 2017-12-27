package game.shenle.com.db.table

import java.util.*

/**
 * Created by shenle on 2017/12/27.
 */
interface UserTableInter {
    var id: Long
    var createDate: Date?
    var createDateJb: Int?
    var userid: String
    var userName: String?
    var userPhone: String?
    var updateDate: Date?
    var status: Int?
}
