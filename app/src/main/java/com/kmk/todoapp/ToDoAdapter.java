package com.kmk.todoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {


    List<Todo> todoolist;
    private Context context;

    public ToDoAdapter(List<Todo> todoolist, Context context) {
        this.todoolist = todoolist;
        this.context = context;
    }

    @NonNull
    @Override
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todolayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        holder.title.setText(todoolist.get(position).getTitle());
        holder.body.setText(todoolist.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return todoolist.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        TextView title, body;



        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txttitle);
            body = itemView.findViewById(R.id.txtbody);


        }
    }
}
