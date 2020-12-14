package com.kmk.todoapp;

public class Todo {

    String title, body;
    public Todo(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Todo() {

    }

    @Override
    public String toString() {
        return "Todo{" +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
