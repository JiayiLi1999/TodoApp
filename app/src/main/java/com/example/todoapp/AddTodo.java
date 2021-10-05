package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

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
        Todo newTodo = new Todo(title,description,status);
        if(process==0){
            // add a new Todo Item
            Todo.todoList.add(newTodo);
        }else if(process==1&&todoID!=-1&&Todo.todoList.size()>todoID){
            //edit a Todo Item
//            Todo curr = Todo.todoList.get((int)todoID);
            Todo.todoList.set(todoID,newTodo);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}