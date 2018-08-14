package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.lx.floor100.view.MySurfaceView;

import java.util.Random;

/**
 * Created by lx on 2018-08-08.
 */

public class RollingPlatform extends Platform {

    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;

    private int direction;

    public RollingPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform) {
        super(platform_x, platform_y, length, bmpPlatform);
        Random rand = new Random();
        direction = rand.nextInt(1);

    }

    @Override
    public void platformDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        if(isOnScreen) {
            //canvas.drawBitmap(bmpPlatform,x,y,paint);
            canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
        }
    }

    @Override
    public void update(MySurfaceView view) {
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(MySurfaceView view) {
        if(direction == DIRCTION_RIGHT){
            view.player.addX(3);
        }
        else if(direction == DIRCTION_LEFT){
            view.player.addX(-3);
        }
    }
}
