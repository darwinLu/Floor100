package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-10.
 */

public class FloorPlatform extends Platform {

    public FloorPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform) {
        super(platform_x, platform_y, length, bmpPlatform);
    }

    @Override
    public void platformDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
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
        super.addEffectToPlayer(view);
    }
}
