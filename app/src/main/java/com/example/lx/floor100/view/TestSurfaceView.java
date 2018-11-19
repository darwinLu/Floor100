package com.example.lx.floor100.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by lx on 2018-11-12.
 */

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private SurfaceHolder sfh;

    public TestSurfaceView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int playerWidth = screenWidth/10;
        int playerHeight = playerWidth * 3 / 2;
        Canvas canvas = holder.lockCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(0,0,playerWidth,playerHeight,paint);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }
}
