package cn.edu.bistu.cs.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vinci on 2017-5-4.
 */

/**
 *
 * @ClassName: DBHelper
 * @Description: TODO(数据库辅助类)
 * @author alonez
 * @date 2015-7-10 下午4:53:53
 * @version V1.0
 */
public class DBHelper extends SQLiteOpenHelper{

    // 数据库版本号，最低为1
    public static final int DB_VERSION = 1;
    // 数据库名称
    public static final String DB_NAME = "login";
    // 表名
    public final static String TABLE_NAME = "user";

    // 建表语句
    private static final String CREATE_TABLE =  "create table if not exists user" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, userPass Text)";

    /**
     * <p>Title: DataBaseHelper</p>
     * <p>Description: 数据库辅助类的构造方法</p>
     * @param context  上下文对象
     * @param name   数据库名称
     * @param factory  查询数据库的游标工厂
     * @param version   数据库版本，最低为1
     */
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /* (非 Javadoc)
     * <p>Title: onCreate</p>
     * <p>Description: 该函数是在第一次创建数据库的时候执行，实际上是第一次
     * 得到SQLiteDatabase对象的时候才会被调用</p>
     * @param db
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行建表语句
        db.execSQL(CREATE_TABLE);
    }

    /* (非 Javadoc)
     * <p>Title: onUpgrade</p>
     * <p>Description: </p>
     * @param db
     * @param oldVersion
     * @param newVersion
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("onUpgrade", "update database：oldVersion = "+oldVersion+", new Version ="+newVersion);
    }

}
