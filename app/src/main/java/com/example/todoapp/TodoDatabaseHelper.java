package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todoList";
    private static final int DB_VERSION = 2;
    public static int sequence = 0;

    public TodoDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db,0,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db,oldVersion,newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        updateDatabase(db,oldVersion,newVersion);
        if(newVersion==1) deleteTable(db);
    }



    public static void insertData(SQLiteDatabase db,String title,String description,int status){
        ContentValues todoItem = new ContentValues();
        todoItem.put("TITLE",title);
        todoItem.put("DESCRIPTION",description);
        todoItem.put("STATUS",status);
        db.insert("TODO",null,todoItem);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion<=1){
            db.execSQL("CREATE TABLE TODO (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TITLE TEXT," +
                    "DESCRIPTION TEXT," +
                    "STATUS INTEGER,"+
                    "TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP);");
            insertData(db,"246P","mobile development",0);
            insertData(db,"241P","data structure & algo",1);
            insertData(db,"250P","web development",2);
        }
    }

    private void deleteTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE TODO");
    }


}
