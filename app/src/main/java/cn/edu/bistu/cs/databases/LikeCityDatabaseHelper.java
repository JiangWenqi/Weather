package cn.edu.bistu.cs.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vinci on 2017-5-11.
 */

public class LikeCityDatabaseHelper extends SQLiteOpenHelper {
    // 数据库版本号，最低为1
    public static final int DB_VERSION = 1;
    // 数据库名称
    public static final String DB_NAME = "user";
    // 注册表名
    public final static String TABLE_NAME = "likecity";
    // 建表语句
    private static final String CREATE_LIKE_TABLE = "create table if not exists likecity" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, cityCode Text)";

    public LikeCityDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行建表语句
        db.execSQL(CREATE_LIKE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
