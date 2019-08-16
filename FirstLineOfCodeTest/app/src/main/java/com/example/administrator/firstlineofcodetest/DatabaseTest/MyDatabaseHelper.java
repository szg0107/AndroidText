package com.example.administrator.firstlineofcodetest.DatabaseTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 创建book表
     * blob 二进制类型
     * id (主键) integer 整型 primary key 设置主键 autoincrement 自增长
     * author 作者 text 文本类型
     * price 价格 real 浮点型
     * pages 页数
     * name 书名
     */
    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text)";
    /**
     * 创建Category（图书分类）表
     * id 主键
     * category_name 分类名
     * category_code 分类代码
     */
    public static final String CREATE_CATEGORY = "create table Category("
            +"id integer primary key autoincrement,"
            +"category_name text,"
            +"category_code integer)";
    //上下文
    private Context mContext;

    /**
     * MyDatabaseHelper构造函数四个参数
     * context 上下文
     * name 数据库名
     * factory 在查询数据的时候返回一个自定义的Cursor,一般都是传入nulL.
     * version 当前数据库版本号，可用于对数据库进行升级操作。
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果发现数据库中已经存在Book表或Category表，就将这两张表删除。
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        //调用onCreate方法重新创建
        onCreate(db);
    }
}
