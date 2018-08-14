package com.example.lx.floor100;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button startButton,rankingListButton,optionButton,exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        rankingListButton = (Button) findViewById(R.id.ranking_list_button);
        rankingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        optionButton = (Button) findViewById(R.id.option_button);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
