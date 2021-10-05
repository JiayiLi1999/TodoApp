package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class TodoDetail extends AppCompatActivity {

    final static String  TODO_ID = "TODO_ID";
    private long todoID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        TodoDetailFragment frag = (TodoDetailFragment) getSupportFragmentManager().findFragmentById(R.id.todoDetail_id);
//        frag.setID(1);
        todoID = getIntent().getLongExtra(TODO_ID,0);
        frag.setID(todoID);
    }

    public void onClickDeleteButton(View view) {
        if(todoID!=-1&&Todo.todoList.size()>todoID) Todo.todoList.remove((int)todoID);
        Log.v("message", String.valueOf(todoID));
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onClickEditButton(View view) {
        if(todoID!=-1&&Todo.todoList.size()>todoID) Todo.todoList.get((int)todoID).setStatus(1);
        Intent intent = new Intent(this,AddTodo.class);
        intent.putExtra("todoID",(int)todoID);
        intent.putExtra("Process",1);
        startActivity(intent);
    }
}