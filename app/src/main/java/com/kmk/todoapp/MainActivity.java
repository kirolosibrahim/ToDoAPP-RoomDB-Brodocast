package com.kmk.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TodoDatabase todoDatabase;
    TodoDao todoDao;
    List<Todo> todoList;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ToDoAdapter toDoAdapter;
    EditText edittitle, editbody;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Swipe Refresh
        swipeRefreshLayout = findViewById(R.id.swp);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            todoDao = todoDatabase.todoDao();
            todoList = todoDao.getaAlltodo();
            toDoAdapter = new ToDoAdapter(todoList);
            recyclerView.setAdapter(toDoAdapter);
            swipeRefreshLayout.setRefreshing(false);
        });

        //findview
        edittitle = findViewById(R.id.updatetitle);
        editbody = findViewById(R.id.et_body);
        recyclerView = findViewById(R.id.rv_todolist);


        todoDatabase = TodoDatabase.getInstance(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Exit Button
        Button button = findViewById(R.id.btn_exit);
        button.setOnClickListener(v -> {
            finishAffinity();
        });

        //Save Button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            InsertData();
        });


        //SetData
        todoDao = todoDatabase.todoDao();
        todoList = todoDao.getaAlltodo();
        toDoAdapter = new ToDoAdapter(todoList);
        recyclerView.setAdapter(toDoAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    //Insert Data
    List<Todo> InsertData() {

        String body = editbody.getText().toString();
        String title = edittitle.getText().toString();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Title or Body is Empty , Please Enter Data", Toast.LENGTH_SHORT).show();


        } else {

            todoDao = todoDatabase.todoDao();
            Todo todo = new Todo();

            todo.setBody(body);
            todo.setTitle(title);
            todoDao.inserttodo(todo);


            todoDao = todoDatabase.todoDao();
            todoList = todoDao.getaAlltodo();
            toDoAdapter = new ToDoAdapter(todoList);
            recyclerView.setAdapter(toDoAdapter);


            editbody.getText().clear();
            edittitle.getText().clear();
        }




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
            Todo todo = todoList.get(position);
            int id = todo.getId();
            switch (direction) {
                //remove Data
                case ItemTouchHelper.LEFT:

                    todoDao.deleteById(id);
                    todoList.remove(position);
                    toDoAdapter.notifyItemRemoved(position);
                    break;
                //EditData
                case ItemTouchHelper.RIGHT:

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.customtodoupdate, null);
                    alertDialog.setView(view);
                    alertDialog.show();
                    EditText updateTitle = view.findViewById(R.id.updatetitle);
                    EditText updateBody = view.findViewById(R.id.updatebody);


                    updateTitle.setText(todoList.get(position).getTitle());
                    updateBody.setText(todoList.get(position).getBody());

                    //Save Button
                    Button save = view.findViewById(R.id.btnsave);
                    save.setOnClickListener(v -> {

                        todoDao.updateById(id, updateTitle.getText().toString(), updateBody.getText().toString());

                        todoDao = todoDatabase.todoDao();
                        todoList = todoDao.getaAlltodo();
                        toDoAdapter = new ToDoAdapter(todoList);
                        recyclerView.setAdapter(toDoAdapter);
                        swipeRefreshLayout.setRefreshing(false);

                        alertDialog.dismiss();

                    });

                    //Cancle button
                    Button cancele = view.findViewById(R.id.btncancle);
                    cancele.setOnClickListener(v -> alertDialog.dismiss());


                    todoList.remove(position);
                    toDoAdapter.notifyItemRemoved(position);

                    break;
            }

        }

    };

}

