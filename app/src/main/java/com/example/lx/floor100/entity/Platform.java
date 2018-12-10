package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.example.lx.floor100.engine.Entity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2017-06-19.
 */

public class Platform extends Entity implements IUpdate {

    //平台位置
    public int x;
    //平台长度
    public int length;
    //平台序号
//    public int number;
    //平台图片
    protected Bitmap bmpPlatform;
    //平台间距
    static public int SPACE = 300;
    //平台厚度
    static public int THICKNESS;
    //平台是否有效的标志位
    public boolean isOnScreen;

    private int platformRealWidth;
    private int platformRealHeight;


    public Platform(int platform_x, int platform_y, int length, Bitmap bmpPlatform,View gameView) {
        this.x = platform_x;
        this.y = platform_y;
        this.length = length;
//        this.number = number;
        this.bmpPlatform = bmpPlatform;
        platformRealWidth = bmpPlatform.getWidth();
        platformRealHeight = bmpPlatform.getHeight();
        scaleBitmap(platformRealWidth,platformRealHeight,gameView);
        //平台实例化时初始为有效，显示在屏幕上
        this.isOnScreen = true;
    }

    private void scaleBitmap(int platformRealWidthOrigin, int platformRealHeightOrigin, View gameView) {
        float scalePlatformHeight = (((float)gameView.getHeight())/20)/(float)platformRealHeightOrigin;
        Matrix matrix = new Matrix();
        matrix.postScale(scalePlatformHeight,scalePlatformHeight);
        bmpPlatform = Bitmap.createBitmap(bmpPlatform,0,0,bmpPlatform.getWidth(),bmpPlatform.getHeight(),matrix,false);
        platformRealWidth = bmpPlatform.getWidth();
        platformRealHeight = bmpPlatform.getHeight();
        THICKNESS = bmpPlatform.getHeight();
        length = bmpPlatform.getWidth();
    }

    public void platformDraw(Canvas canvas){
//        canvas.drawBitmap(bmpPlatform,x,y,paint);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        if(isOnScreen) {
            canvas.drawBitmap(bmpPlatform,x,y,null);
            //canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
        }
    }


    @Override
    public void update(MySurfaceView view) {
        if(y>MySurfaceView.screenH){
            isOnScreen = false;
        }
    }

    public void addEffectToPlayer(MySurfaceView view){
        return;
    }
}
