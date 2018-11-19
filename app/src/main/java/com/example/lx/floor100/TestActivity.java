package com.example.lx.floor100;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lx.floor100.view.TestSurfaceView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TestSurfaceView(this));
    }
}
