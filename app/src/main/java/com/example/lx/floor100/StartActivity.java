package com.example.lx.floor100;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView titleImageView;
    Button startButton,rankingListButton,optionButton,exitButton,testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        titleImageView = (ImageView) findViewById(R.id.title_image);
        Bitmap titleImage = BitmapFactory.decodeResource(getResources(),R.drawable.title);
        titleImageView.setImageBitmap(titleImage);
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
        testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,TestActivity.class);
                startActivity(intent);
            }
        });

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
