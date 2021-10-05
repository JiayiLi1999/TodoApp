package com.example.todoapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TodoDetailFragment extends Fragment {
    private long id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getLong("ID"); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view!=null){
            TextView titleView = view.findViewById(R.id.todoTitle);
            titleView.setText(Todo.todoList.get((int)id).getName());
            TextView textView = view.findViewById(R.id.todoText);
            textView.setText(Todo.todoList.get((int)id).getDescription());
            TextView statusView = view.findViewById(R.id.todoStatus);
            statusView.setText(Todo.todoList.get((int)id).getStatus());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("ID",id);
    }

    public void setID(long id){
        this.id = id;
    }
}