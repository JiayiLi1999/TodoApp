package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTodo extends AppCompatActivity {
    private int todoID;
    private int process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        Intent intent = getIntent();
        todoID = intent.getIntExtra("todoID",-1);
        process = intent.getIntExtra("Process",0);
    }


    public void onClickSubmitButton(View view) {
        String title = ((TextView)findViewById(R.id.title)).getText().toString();
        String description = ((TextView)findViewById(R.id.text)).getText().toString();
        Spinner spinner = (Spinner)findViewById(R.id.choice);
        int status = spinner.getSelectedItemPosition();

        if(process==0){
            // add a new Todo Item
            addTodo(title,description,status);
        }else if(process==1 && todoID!=-1){
            // Edit a Todo Item
            updateTodo(todoID,title,description,status);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    private void addTodo(String title, String description, int status) {
        try {
            SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(this);
            SQLiteDatabase db = todolistHelper.getWritableDatabase();
            TodoDatabaseHelper.insertData(db,title,description,status);
            db.close();
        }catch (SQLException e){
            Toast toast = Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void updateTodo(int todoID, String title, String description, int status) {
        try {
            SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(this);
            SQLiteDatabase db = todolistHelper.getWritableDatabase();
            ContentValues todoItem = new ContentValues();
            todoItem.put("TITLE",title);
            todoItem.put("DESCRIPTION",description);
            todoItem.put("STATUS",status);
            db.update("TODO",todoItem,"_id=?",new String[]{String.valueOf(todoID)});
            db.close();
        }catch (SQLException e){
            Toast toast = Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}