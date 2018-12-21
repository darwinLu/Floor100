package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-08.
 */

public class LRPlatform extends Platform {

    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;

    private int init_x;
    private int direction = DIRCTION_RIGHT;


    public LRPlatform(int existPlatformNumber, Bitmap bmpPlatform) {
        super(existPlatformNumber, bmpPlatform);
        init_x = super.x;
    }

//    @Override
//    public void platformDraw(Canvas canvas,Paint paint) {
////        Paint paint = new Paint();
////        paint.setColor(Color.GREEN);
//        if(isOnScreen) {
//            canvas.drawBitmap(bmpPlatform,x,y,paint);
//            //canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
//        }
//    }

    @Override
    public void update(MySurfaceView view) {
        if(direction == DIRCTION_RIGHT){
            x = x + 1;
            if(x > init_x + 80){
                x = init_x + 80;
                direction = DIRCTION_LEFT;
            }
        }
        else if(direction == DIRCTION_LEFT){
            x = x - 1;
            if(x < init_x - 80){
                x = init_x - 80;
                direction = DIRCTION_RIGHT;
            }
        }
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(MySurfaceView view) {
        if(direction == DIRCTION_RIGHT){
                view.player.addX(1);
        }
        else if(direction == DIRCTION_LEFT){
            view.player.addX(-1);
        }
    }
}
