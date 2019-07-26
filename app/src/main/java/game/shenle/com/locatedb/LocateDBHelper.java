package game.shenle.com.locatedb;

import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;


/**
 * Created by shenle on 2016/11/1.
 */

public class LocateDBHelper {
    /**
     * 单个查询
     * @param c
     * @return
     */
    public static <T extends BaseModel> T querySingle(Class<T> c){
        return (T)SQLite.select().from(c).querySingle();
    }

    /**
     *
     * @param c
     * @param where(example:model_Table.gender.eq(1)  查询单个gender是1)
     * @return
     */
    public static BaseModel querySingle(Class<? extends BaseModel> c, SQLOperator where){
        BaseModel model = SQLite.select().from(c).where(where).querySingle();
        return model;
    }
    /**
     * 返回所有查询结果
     * @param c
     * @return
     */
    public static List<? extends BaseModel> queryList(Class<? extends BaseModel> c){
        List<? extends BaseModel> models = SQLite.select().from(c).queryList();
        return models;
    }

    /**
     *
     * @param c
     * @param where(example:model_Table.gender.eq(1)  查询所有gender是1)
     * @return
     */
    public static List<? extends BaseModel> queryList(Class<? extends BaseModel> c, SQLOperator where){
        List<? extends BaseModel> models = SQLite.select().from(c).where(where).queryList();
        return models;
    }
}
