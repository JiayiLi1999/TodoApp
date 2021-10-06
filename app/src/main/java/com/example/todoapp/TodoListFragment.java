package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.zip.Inflater;

public class TodoListFragment extends ListFragment {

    private Listener listener;
    private View mView;
    private LayoutInflater mInflater;
    private SQLiteDatabase db;
    private Cursor todolistCursor;
    private CursorAdapter todolistAdapter;
    private int orderType = 0;

    private String order = "TIMESTAMP DESC";

    interface Listener{
        void itemClicked(long id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        mView = view;
        mInflater = inflater;
        setupList();
        addButtonListener();
        addSpinnerListener();
        addEditTextListener();
        return mView;
    }

    private void addEditTextListener() {
        EditText editTextView = mView.findViewById(R.id.searching);
        editTextView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String searchingText = editTextView.getText().toString(); 
                    updateByNewKeywords(searchingText);
                    return true;
                }
                return false;
            }
        });
    }

    private void updateByNewKeywords(String searchingText) {
        Cursor newCursor;
        if(searchingText.length()==0){
            newCursor =  db.query("TODO",new String[]{"_id","TITLE","DESCRIPTION"},
                    null,null,null,null,order);
        }else{
            newCursor =  db.query("TODO",new String[]{"_id","TITLE","DESCRIPTION"},
                    "TITLE=?",new String[]{searchingText},null,null,order);
        }
        todolistAdapter.changeCursor(newCursor);
        todolistCursor = newCursor;
    }

    private void addSpinnerListener() {
        Spinner orderView = mView.findViewById(R.id.order_spinner);
        orderView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=orderType){
                    orderType = position;
                    updateByNewOrder(orderType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    private void updateByNewOrder(int orderType) {
        if(orderType==0){
            order ="TIMESTAMP DESC";
        }else{
            order = "TITLE";
        }
        Cursor newCursor =  db.query("TODO",new String[]{"_id","TITLE","DESCRIPTION"},
                null,null,null,null,order);
        todolistAdapter.changeCursor(newCursor);
        todolistCursor = newCursor;
    }

    private void addButtonListener() {
        Button button = (Button) mView.findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                Intent intent = new Intent(mInflater.getContext(), AddTodo.class);
                intent.putExtra("isAdding",true);
                startActivity(intent);
            }
        });
    }


    public void setupList(){
        try {
            SQLiteOpenHelper todolistHelper = new TodoDatabaseHelper(mInflater.getContext());
            db = todolistHelper.getReadableDatabase();
            todolistCursor = db.query("TODO",new String[]{"_id","TITLE","DESCRIPTION"},
                    null,null,null,null,order);
            todolistAdapter = new SimpleCursorAdapter(mInflater.getContext(),
                    android.R.layout.simple_list_item_1,todolistCursor,
                    new String[]{"TITLE"}, new int[]{android.R.id.text1},0);
            setListAdapter(todolistAdapter);
        }catch (SQLException e){
            Toast toast = Toast.makeText(mInflater.getContext(),"Database Unavailable",Toast.LENGTH_SHORT);
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