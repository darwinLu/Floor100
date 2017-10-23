package com.example.lx.floor100;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lx on 2017-06-19.
 */

public class Platform {

    //平台位置
    public int x;
    public int y;
    //平台长度
    public int length;
    //平台序号
//    public int number;
    //平台图片
    private Bitmap bmpPlatform;
    //平台间距
//    static public int SPACE = 60;
    //平台厚度
//    static public int THICKNESS = 20;


    public Platform(int platform_x, int platform_y, int length, Bitmap bmpPlatform) {
        this.x = platform_x;
        this.y = platform_y;
        this.length = length;
//        this.number = number;
        this.bmpPlatform = bmpPlatform;
    }

    public void platformDraw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bmpPlatform,x,y,paint);
    }
}
