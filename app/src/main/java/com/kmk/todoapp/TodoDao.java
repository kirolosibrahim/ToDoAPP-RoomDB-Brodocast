package com.kmk.todoapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;


@Dao
public interface TodoDao {

    @Query("select * from todo_table ")
    List<Todo> getaAlltodo();


    @Insert
    void inserttodo(Todo todo);


    @Query("delete from todo_table where id = :id")
    void deleteById(int id);

    @Query("update todo_table set title = :title , body = :body, Hours =:Hours , Min=:Min where id = :id")
    void updateById(int id, String title, String body, int Hours, int Min);

    @Query("Select body From todo_table where Hours =:Hours And Min=:Min")
    String selectbody(int Hours, int Min);

    @Query("Select title From todo_table where Hours =:Hours And Min=:Min")
    String selectitle(int Hours, int Min);


    @Query("SELECT * from todo_table  where Hours =:Hours And Min=:Min")
    Todo selectAllWhenTime(int Hours, int Min);

    @Query("SELECT * from todo_table  where title = :title")
    Todo selectAllbytitle(String title);

    @Query("SELECT title from todo_table  where title = :title")
    String selecttitlebytitle(String title);

    @Query("SELECT body from todo_table  where title = :title")
    String selectbodybytitle(String title);



}