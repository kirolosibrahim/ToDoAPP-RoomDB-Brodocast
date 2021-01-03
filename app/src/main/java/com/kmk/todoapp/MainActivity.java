package com.kmk.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


    int H, M, UpdateHours, UpdateMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Button btntime = findViewById(R.id.notificationtimepicker);
        btntime.setOnClickListener(v -> {
            InsertTime();
        });


        //Swipe Refresh
        swipeRefreshLayout = findViewById(R.id.swp);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            todoDao = todoDatabase.todoDao();
            todoList = todoDao.getaAlltodo();
            toDoAdapter = new ToDoAdapter(todoList, this);
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

            String body = editbody.getText().toString();
            String title = edittitle.getText().toString();


            if (title.isEmpty() || body.isEmpty()) {
                Toast.makeText(this, "Title or Body is Empty , Please Enter Data", Toast.LENGTH_SHORT).show();

            } else {
                InsertData(H, M, title, body);
            }


            // Intent
            Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);

            intent.putExtra("title", title);
            intent.putExtra("body", body);

            // PendingIntent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
            );

            // AlarmManager
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);



            // Create time.
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, H);
            startTime.set(Calendar.MINUTE, M);
            startTime.set(Calendar.SECOND, 0);
            long alarmStartTime = startTime.getTimeInMillis();


                // Set Alarm
                alarmManager.set(AlarmManager.RTC_WAKEUP,alarmStartTime, pendingIntent);
                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();




        });


        //SetData
        todoDao = todoDatabase.todoDao();
        todoList = todoDao.getaAlltodo();
        toDoAdapter = new ToDoAdapter(todoList, this);
        recyclerView.setAdapter(toDoAdapter);

//        Todo todo = new Todo();
//        todo.setBody(todoDao.selectbodybytitle("ehe"));
//        Toast.makeText(this, "" + todo.getBody(), Toast.LENGTH_SHORT).show();
//        Todo t = todoDao.selectAllbytitle("shasa");
//        Toast.makeText(this, "" + t.getBody(), Toast.LENGTH_SHORT).show();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    //Insert Data
    List<Todo> InsertData(int h, int m, String t, String b) {
        TodoDao todoDao = todoDatabase.todoDao();
        Todo todo = new Todo();



        todo.setTitle(t);
        todo.setBody(b);
        todo.setHours(h);
        todo.setMin(m);
        todoDao.inserttodo(todo);


        todoDao = todoDatabase.todoDao();
        todoList = todoDao.getaAlltodo();
        toDoAdapter = new ToDoAdapter(todoList, this);
        recyclerView.setAdapter(toDoAdapter);

        editbody.getText().clear();
        edittitle.getText().clear();


        return todoList;
    }

    private void InsertTime() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth, (view, hourOfDay, minute) -> {
            H = hourOfDay;
            M = minute;
        }, 12, 0, false);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(H, M);
        timePickerDialog.show();
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
                    UpdateData(position, id);
                    break;
            }

        }


    };

    private void UpdateData(int position, int id) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.customtodoupdate, null);
        alertDialog.setView(view);
        alertDialog.show();
        EditText updateTitle = view.findViewById(R.id.updatetitle);
        EditText updateBody = view.findViewById(R.id.updatebody);
        Button updatenotificationtime = view.findViewById(R.id.updatenotificationtimepicker);


        updatenotificationtime.setOnClickListener(v -> {


            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth, (view1, hourOfDay, minute) -> {
                UpdateHours = hourOfDay;
                UpdateMin = minute;

            }, 12, 0, false);

            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(UpdateHours, UpdateMin);
            timePickerDialog.show();

        });

        updateTitle.setText(todoList.get(position).getTitle());
        updateBody.setText(todoList.get(position).getBody());


        //Save Button
        Button save = view.findViewById(R.id.btnsave);
        save.setOnClickListener(v -> {

            todoDao.updateById(id, updateTitle.getText().toString(), updateBody.getText().toString(), UpdateHours, UpdateMin);


            todoDao = todoDatabase.todoDao();
            todoList = todoDao.getaAlltodo();
            toDoAdapter = new ToDoAdapter(todoList, this);
            recyclerView.setAdapter(toDoAdapter);
            swipeRefreshLayout.setRefreshing(false);

            alertDialog.dismiss();

        });

        //Cancle button
        Button cancele = view.findViewById(R.id.btncancle);
        cancele.setOnClickListener(v -> alertDialog.dismiss());


        todoList.remove(position);
        toDoAdapter.notifyItemRemoved(position);


    }


}

