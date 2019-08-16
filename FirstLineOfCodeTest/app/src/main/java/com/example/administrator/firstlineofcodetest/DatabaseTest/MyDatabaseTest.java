package com.example.administrator.firstlineofcodetest.DatabaseTest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.firstlineofcodetest.R;

public class MyDatabaseTest extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_database_test);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        //创建数据库
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        //添加数据
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                /**
                 * 第一个参数  表名
                 * 第二个参数  用于在未指定添加数据的情况下给某些可为空的列自动赋值为null，一般用不到这个功能，直接传入null。
                 *第三个参数  一个ContentValues对象，提供了一系列的put()方法重载，用于向ContentValues中添加数据，
                 *            只需要将表中的每个列名以及相应的待添加数据传入即可。
                 */
                db.insert("Book", null, values);//插入第一条数据
                values.clear();
                //开始组装第二条数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);
            }
        });
        //更新数据
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                /**
                 * 第一个参数 表名
                 * 第二个参数 ContentValues对象，把更新的数据在这里组装进去
                 * 第三个、四个参数 用于约束更新某一行或某几行中的数据，不指定的话默认就是更新所有行。*/
                db.update("Book", values, "name=?", new String[]{"The Da Vinci Code"});
            }
        });
        //删除数据
        Button deleteButton = (Button) findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //表名 二三参数 用于约束删除某一行或某几行的数据，不指定的话默认就是删除所有行。
                db.delete("Book", "pages>?", new String[]{"500"});
            }
        });
        //查询数据
        Button queryButton = (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                /**
                 * table 表名
                 * columns 指定去查询哪几列，不指定默认查询所有列。
                 * selection、selectionArgs 约束查询某一行或某几行，不指定默认查询所有行数据
                 * groupBy 指定需要去groupBy的列，不指定则表示不对查询结果进行groupBy操作。
                 * having 对groupBy之后的数据进行进一步的过滤，不指定则表示不进行过滤。
                 * orderBy 指定查询结果的排序方式、不指定则表示使用默认的排序方式。*/
                //查询Book表中所有的数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("Book", "book name is " + name + ",book author is " + author + ",book pages is " + pages + ",book price is" + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
