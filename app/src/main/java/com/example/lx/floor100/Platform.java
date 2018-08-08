package com.example.lx.floor100;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by lx on 2017-06-19.
 */

public class Platform extends Entity implements IUpdate{

    //平台位置
    public int x;
    //平台长度
    public int length;
    //平台序号
//    public int number;
    //平台图片
    protected Bitmap bmpPlatform;
    //平台间距
    static public int SPACE = 400;
    //平台厚度
    static public int THICKNESS = 40;
    //平台是否有效的标志位
    public boolean isOnScreen;


    public Platform(int platform_x, int platform_y, int length, Bitmap bmpPlatform) {
        this.x = platform_x;
        this.y = platform_y;
        this.length = length;
//        this.number = number;
        this.bmpPlatform = bmpPlatform;
        //平台实例化时初始为有效，显示在屏幕上
        this.isOnScreen = true;
    }

    public void platformDraw(Canvas canvas){
//        canvas.drawBitmap(bmpPlatform,x,y,paint);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        if(isOnScreen) {
            canvas.drawBitmap(bmpPlatform,x,y,paint);
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
