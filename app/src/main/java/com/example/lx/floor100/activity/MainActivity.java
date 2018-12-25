package com.example.lx.floor100.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lx.floor100.view.GameSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameSurfaceView(this));
    }

}
