package game.shenle.com.locatedb.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import game.shenle.com.locatedb.AppLocateDatabase;


/**
 * 历史记录
 */
@Table(database = AppLocateDatabase.class)
public class Mp3History extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id=0L;
    @Column
    public String url;//地址
    @Column
    public long progress;//进度
    @Column
    public int gz_tiao_begin_time;//跳过开头时长分钟
    @Column
    public int gz_auto;//是否自动下一级=集 0 否 1是
    @Column
    public int gz_type;//规则type 0 否 1 规则1 2 规则2...
    @Column
    public String title;
}
