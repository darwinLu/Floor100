package com.example.lx.floor100;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
                Intent intent = new Intent(StartActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
        optionButton = (Button) findViewById(R.id.option_button);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,OptionActivity.class);
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
        createOption();
    }

    private void createOption() {
        SharedPreferences option = getSharedPreferences("option", MODE_PRIVATE);
        boolean isFirstRun = option.getBoolean("isFirstRun",true);
        if(isFirstRun){
            SharedPreferences.Editor editor = option.edit();
            editor.putBoolean("isFirstRun",false);
            editor.putString("sound_switch", "on");
            editor.putString("music_switch", "on");
            editor.apply();
        }
    }
}
