package com.example.lx.floor100.hud;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-10.
 */

public class Rank {

    private int currentRank;

    private int level = 4;

    private int rankFrame = 0;

    public Rank(int currentRank) {
        this.currentRank = currentRank;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.RED);
        paint.setTextSize(80);
        canvas.drawText("rank:"+currentRank, MySurfaceView.screenW-400,100,paint);
    }

    public void addRank(){
        if(rankFrame<level){
            rankFrame++;
        }
        else{
            this.currentRank += 1;
            rankFrame = 0;
        }
    }

    public int getCurrentRank() {
        return currentRank;
    }
}
