package com.example.finalapplication;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalDatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DB_NAME = "goals.db";  // 数据库文件名
    public static final String TB_NAME = "goals";
    public static final String ID = "id";
    public static final String DATE = "goal_date";

    public static final String TEXT = "goal_text";
    public static final String DONE = "goal_done";


    public GoalDatabaseHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }

    // 第一次创建数据库时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TB_NAME+" " +
                "("+ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+DATE+
                "TEXT,"+TEXT+"TEXT,"+DONE+"INTEGER)";
        db.execSQL(sql);
    }

    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        // 一般用于版本更新时改表结构
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
        onCreate(db);
    }
}

