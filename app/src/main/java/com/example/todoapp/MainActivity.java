package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements TodoListFragment.Listener {

    private int statusSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void filterTodo(int statusSelection) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", statusSelection);
        // set Fragmentclass Arguments
        Fragment frag = new TodoListFragment();
        frag.setArguments(bundle);
    }

    @Override
    public void itemClicked(long id) {
        View view = findViewById(R.id.frame_layout);
        if(view!=null){
            TodoDetailFragment details = new TodoDetailFragment();
            details.setID(id);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout,details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }else{
            Intent intent = new Intent(this,TodoDetail.class);
            intent.putExtra(TodoDetail.TODO_ID,id);
            startActivity(intent);
        }
    }

    public void onClickAddButton(View view) {
        Intent intent = new Intent(this, AddTodo.class);
        intent.putExtra("isAdding",true);
        startActivity(intent);
    }

}