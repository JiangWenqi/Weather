package cn.edu.bistu.cs.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Vinci on 2017-5-4.
 */

public class DBDao {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBDao(Activity activity) {
        dbHelper = new DBHelper(activity, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
    }

    /**
     * @param userName
     * @param userPass
     * @param @return  设定文件
     * @return long    返回类型
     * @Title: register
     * @Description: TODO(用户注册)
     */
    public long register(String userName, String userPass) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", userName);
        values.put("userPass", userPass);
        Log.d("User", userName + "," + userPass);
        long rows = db.insert("user", null, values);
        Log.d("SQLite", "注册用户成功！");
        dbHelper.close();
        db.close();
        return rows;
    }

    /**
     * @param userName
     * @param userPass
     * @param @return  设定文件
     * @return int    返回类型
     * @Title: login
     * @Description: TODO(用户登录)
     */
    public int login(String userName, String userPass) {
        Log.d("User", userName + "," + userPass);
        db = dbHelper.getReadableDatabase();
        String sql = "select * from user where userName ='" + userName + "' and userPass = '" + userPass + "'";
        Cursor c = db.rawQuery(sql, null);
        Log.d("SQLite", "执行查询语句");
        int rows = c.getCount();
        Log.d("SQLite", "查询结果:"+rows);
        dbHelper.close();
        db.close();
        return rows;
    }
}
