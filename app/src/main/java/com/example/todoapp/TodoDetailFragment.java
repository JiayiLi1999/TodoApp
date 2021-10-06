package com.example.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TodoDetailFragment extends Fragment {
    private long todoID;
    private LayoutInflater inflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            todoID = (int)savedInstanceState.getLong("INDEX"); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_todo_detail, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getView()==null) return;
        getTodoItem();
        setupDeleteButton();
        setupEditButton();
    }

    private void getTodoItem() {
        View view = getView();
        try {
            SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(inflater.getContext());
            SQLiteDatabase db = todolistHelper.getReadableDatabase();
            Cursor cursor = db.query("TODO",new String[]{"TITLE","DESCRIPTION","STATUS"},
                    "_id=?",new String[]{String.valueOf(todoID)},
                    null,null,null,null);
            if(cursor.moveToFirst()){
                String title = cursor.getString(0);
                String description = cursor.getString(1);
                int status = Integer.parseInt(cursor.getString(2));
                TextView titleView = view.findViewById(R.id.todoTitle);
                titleView.setText(title);
                TextView textView = view.findViewById(R.id.todoText);
                textView.setText(description);
                TextView statusView = view.findViewById(R.id.todoStatus);
                statusView.setText(MainActivity.statusList[status]);

            }
            db.close();
        }catch (SQLException e){
            Toast toast = Toast.makeText(inflater.getContext(),"Database Unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void setupEditButton() {
        View view = getView();
        Button edit_button = (Button) view.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    Intent intent2 = new Intent(view.getContext(),AddTodo.class);
                    intent2.putExtra("todoID",(int)todoID);
                    intent2.putExtra("Process",1);
                    startActivity(intent2);
            }
        });
    }
    private void setupDeleteButton() {
        View view = getView();
        Button delete_button = (Button) view.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(inflater.getContext());
                    SQLiteDatabase db = todolistHelper.getReadableDatabase();
                    db.delete("TODO","_id=?",new String[]{String.valueOf(todoID)});
                    db.close();
                    Intent intent = new Intent(view.getContext(),MainActivity.class);
                    startActivity(intent);
                }catch (SQLException e){
                    Toast toast = Toast.makeText(inflater.getContext(),"Database Unavailable",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("INDEX",todoID);
    }

    public void setID(long id){
        this.todoID = id;
    }
}