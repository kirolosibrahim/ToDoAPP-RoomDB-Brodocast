package com.kmk.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Todo> todoList;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ToDoAdapter toDoAdapter;
    EditText edittitle, editbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoList = new ArrayList<Todo>();

          //findview
        edittitle = findViewById(R.id.et_title);
        editbody = findViewById(R.id.et_body);
        recyclerView = findViewById(R.id.rv_todolist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button = findViewById(R.id.btn_exit);
        button.setOnClickListener(v -> {
            finishAffinity();
        });

        //Exit Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            InsertData();
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

        //Insert Data
    private List<Todo> InsertData() {
        Todo todo = new Todo();
        todo.setBody(editbody.getText().toString());
        todo.setTitle(edittitle.getText().toString());
        todoList.add(todo);
        toDoAdapter = new ToDoAdapter(todoList, this);
        recyclerView.setAdapter(toDoAdapter);
        editbody.getText().clear();
        edittitle.getText().clear();
        return todoList;
    }

          //SwipeAction
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                //remove Data
                case ItemTouchHelper.LEFT:
                    todoList.remove(position);
                    toDoAdapter.notifyItemRemoved(position);
                    break;
                 //EditData
                case ItemTouchHelper.RIGHT:
                    Todo todo = new Todo();
                    edittitle.setText(todoList.get(position).getTitle());
                    editbody.setText(todoList.get(position).getBody());

                    todoList.remove(position);
                    toDoAdapter.notifyItemRemoved(position);

                    todo.setBody(editbody.getText().toString());
                    todo.setTitle(edittitle.getText().toString());

                    break;
            }

        }

    };

}

