package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TodoListFragment extends ListFragment {

    private Listener listener;
    private SQLiteDatabase db;
    private Cursor todolistCursor;

    private String order = "TIMESTAMP DESC";

    interface Listener{
        void itemClicked(long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setupList(inflater);
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("receive order change!","a");
        if(getArguments()!=null) order = getArguments().getString("order");
    }

    public void setupList(LayoutInflater inflater){
        try {
            SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(inflater.getContext());
            db = todolistHelper.getReadableDatabase();
            todolistCursor = db.query("TODO",new String[]{"_id","TITLE","DESCRIPTION"},
                    null,null,null,null,order);
            CursorAdapter todolistAdapter = new SimpleCursorAdapter(inflater.getContext(),
                    android.R.layout.simple_list_item_1,todolistCursor,
                    new String[]{"TITLE"}, new int[]{android.R.id.text1},0);
            setListAdapter(todolistAdapter);
        }catch (SQLException e){
            Toast toast = Toast.makeText(inflater.getContext(),"Database Unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        int todoID = -1;
        Log.v("message", String.valueOf(position+1));
        try {
//            String countQuery = "SELECT * FROM TODO ORDER BY TIMESTAMP LIMIT 10";
//            Cursor cursor = db.rawQuery(countQuery, new String[]{"_id"});
            Cursor cursor = db.query("TODO",new String[]{"_id"},null,null,null,null,order, String.valueOf(position+1));
            if(cursor.moveToLast()){
                todoID = cursor.getInt(0);
            }
        }catch (SQLException e){
            Log.e("SQLException",e.getMessage());
            return;
        }
        if(todoID==-1) return;
        if(listener!=null) listener.itemClicked(todoID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        todolistCursor.close();
        db.close();
    }
}