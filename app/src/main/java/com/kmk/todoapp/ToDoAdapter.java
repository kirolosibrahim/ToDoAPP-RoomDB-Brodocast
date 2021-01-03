package com.kmk.todoapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {


    List<Todo> todoolist;
     Context context;

    public ToDoAdapter(List<Todo> todoolist, Context context) {
        this.todoolist = todoolist;
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todolayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        String time;
        SimpleDateFormat f24hours;
        SimpleDateFormat f12hours;
        Date date;

        holder.title.setText(todoolist.get(position).getTitle());
        holder.body.setText(todoolist.get(position).getBody());
        f24hours = new SimpleDateFormat("hh:mm");
        f12hours = new SimpleDateFormat("hh:mm aa");

        try {
            time = todoolist.get(position).getHours() + ":" + todoolist.get(position).getMin();
            date = f24hours.parse(time);

            String TimeDate = f12hours.format(date);
            holder.notificationdate.setText(TimeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return todoolist.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        TextView title, body, notificationdate;
       CardView cardView;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txttitle);
            body = itemView.findViewById(R.id.txtbody);
            notificationdate = itemView.findViewById(R.id.notificationdate);
            cardView = itemView.findViewById(R.id.cardview);


        }
    }
}
