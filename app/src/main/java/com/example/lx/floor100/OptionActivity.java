package com.example.lx.floor100;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {

    private Button saveOptionButton;
    private CheckBox soundSwitch;
    private CheckBox musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        soundSwitch = (CheckBox)findViewById(R.id.sound_switch);
        musicSwitch = (CheckBox)findViewById(R.id.music_switch);
        saveOptionButton = (Button) findViewById(R.id.save_option);
        saveOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("option",MODE_PRIVATE).edit();
                if(soundSwitch.isChecked()){
                    editor.putString("sound_switch","on");
                }
                else {
                    editor.putString("sound_switch","off");
                }
                if(musicSwitch.isChecked()){
                    editor.putString("music_switch","on");
                }
                else {
                    editor.putString("music_switch","off");
                }
                editor.apply();
                finish();

            }
        });
        loadOption();
    }

    private void loadOption() {
        SharedPreferences option = getSharedPreferences("option",MODE_PRIVATE);
        if(option.getString("sound_switch",null).equals("on")){
            soundSwitch.setChecked(true);
        }
        else {
            soundSwitch.setChecked(false);
        }
        if(option.getString("music_switch",null).equals("on")){
            musicSwitch.setChecked(true);
        }
        else {
            musicSwitch.setChecked(false);
        }
    }
}
