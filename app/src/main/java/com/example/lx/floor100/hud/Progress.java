package com.example.lx.floor100.hud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.lx.floor100.R;
import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-09.
 */

public class Progress {

    private int max;
    private int current;

    private int length;
    private int column;
    private int height;

    private boolean isIncreasing = false;

    private int delayFrame = 5;
    private int currentFrame = 0;

    private int endValue;

    private int powerSoundStream;

    public Progress(int max, int current, int length, int height,int column) {
        this.max = max;
        this.current = current;
        this.length = length;
        this.height = height;
        this.column = column;
    }

    public void increaseValue(){
        if(currentFrame<delayFrame){
            currentFrame++;
        }
        else {
            if(isIncreasing){
                if(current<=max){
                    current ++ ;
                }
                if(current>max){
                    current = 0;
                }
            }
            else {
                current = 0;
            }
            currentFrame = 0;
        }

    }

    public void udpate(){
        increaseValue();
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,length,height,paint);
        paint.setColor(Color.BLUE);
        for(int i=0;i<current;i++){
            canvas.drawRect(i*(length/column),0,(i+1)*(length/column),height,paint);
        }
    }

    public boolean onTouchEvent(MotionEvent event,MySurfaceView view) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(view.getContext().getSharedPreferences("option", Context.MODE_PRIVATE)
                    .getString("sound_switch","off").equals("on")){
                powerSoundStream = view.soundPool.play(view.powerSound,1,1,0,-1,1);
            }
            isIncreasing = true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(view.getContext().getSharedPreferences("option", Context.MODE_PRIVATE)
                    .getString("sound_switch","off").equals("on")){
                view.soundPool.stop(powerSoundStream);
            }
            isIncreasing = false;
            endValue = current;
        }
        return true;
    }

    public int getEndValue(){
        return this.endValue;
    }
}
