package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-06.
 */

public class Background {

    private Bitmap bmpBackGround1;
    private Bitmap bmpBackGround2;

    private int bg1x,bg1y,bg2x,bg2y;

    public int speed;

    public Background(Bitmap bmpBackGround){
        this.bmpBackGround1 = bmpBackGround;
        this.bmpBackGround2 = bmpBackGround;

        bg1y = -Math.abs(bmpBackGround1.getHeight() - MySurfaceView.screenH);
        bg2y = bg1y - bmpBackGround1.getHeight() + 111;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bmpBackGround1,bg1x,bg1y,paint);
        canvas.drawBitmap(bmpBackGround2,bg2x,bg2y,paint);
    }

    public void update(){
        bg1y += speed;
        bg2y += speed;

        if(bg1y>MySurfaceView.screenH){
            bg1y = bg2y - bmpBackGround1.getHeight() + 111;
        }

        if(bg2y>MySurfaceView.screenH){
            bg2y = bg1y - bmpBackGround1.getHeight() + 111;
        }
    }

}
