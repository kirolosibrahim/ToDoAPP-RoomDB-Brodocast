package com.kmk.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

public class FullScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fullscreen_main);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity( new Intent(FullScreen.this,MainActivity.class)) ;
        }, 2000);

    }



}