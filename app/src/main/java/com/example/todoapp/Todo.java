package com.example.todoapp;

import java.util.ArrayList;
import java.util.List;

public class Todo {
    private String name;
    private String description;
    private int status;
    // 1 - todo; 2 - doing; 3 - done;

    public static List<Todo> todoList = new ArrayList<>();

    public Todo(String name, String description,int status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getStatus(){
        String res;
        switch (status) {
            case 1:
                res = "todo";
                break;
            case 2:
                res = "doing";
                break;
            case 3:
                res = "done";
                break;
            default:
                res = "todo";
        }
        return res;
    }
    
    public void setStatus(int index){
        status = index;
        return;
    }

    public String toString() {
        return this.name;
    }
}
