package com.example.lx.floor100.hud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.lx.floor100.view.GameSurfaceView;

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

    private int x;
    private int y;

    public Progress(int max, int current, int length, int height,int column) {

        this.max = max;
        this.current = current;
        this.length = length;
        this.height = height;
        this.column = column;
        this.x = 30;
        this.y = 30;
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
        paint.setColor(Color.YELLOW);
        canvas.drawRect(x,y,x+length,y+height,paint);
        paint.setColor(Color.BLUE);
        for(int i=0;i<current;i++){
            canvas.drawRect(x+i*(length/column),y,x+(i+1)*(length/column),y+height,paint);
        }
    }

    public boolean onTouchEvent(MotionEvent event, GameSurfaceView view) {
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
