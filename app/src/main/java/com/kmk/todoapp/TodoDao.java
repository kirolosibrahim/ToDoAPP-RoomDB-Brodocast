package com.kmk.todoapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TodoDao {

    @Query(" select * from todo_table ")
     List<Todo> getaAlltodo();
    @Insert
    void inserttodo(Todo todo);


    @Delete
    void deletetodo(Todo todo);


    @Query("delete from todo_table where id = :id")
    void deleteById(int id);

    @Query("update todo_table set title = :title , body = :body where id = :id")
    void updateById(int id,String title, String body);


}