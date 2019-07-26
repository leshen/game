package game.shenle.com.locatedb;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 本地数据库
 */
@Database(name = AppLocateDatabase.NAME, version = AppLocateDatabase.VERSION)
public class AppLocateDatabase {
    //数据库名称
    public static final String NAME = "BkjhDatabase";
    //数据库版本号
    public static final int VERSION = 1;
}
