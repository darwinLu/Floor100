package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.lx.floor100.engine.ObjectSizeManager;

/**
 * Created by lx on 2018-08-06.
 */

public class Background {

    private Bitmap bmpBackGround1;
    private Bitmap bmpBackGround2;

    private Bitmap bmpBackGround1OnScreen;
    private Bitmap bmpBackGround2OnScreen;

    //背景图对象的左上角坐标
    private int bg1x,bg1y,bg2x,bg2y;

    //缩放系数
    private float scaleFactor;

    //背景图对象实际在屏幕上的尺寸
    private int backgroundHeightOnScreen;

    //背景滚动速度，实际上是像素距离
    public int speed;

    //两张背景图的重叠量，用于体现无限重复滚动的背景
    private int backgroundAcross;

    public Background(Bitmap bmpBackGround){
        this.bmpBackGround1 = bmpBackGround;
        this.bmpBackGround2 = bmpBackGround;
        backgroundAcross = 111;
        calculateObjectSizeOnScreen();
        bg1x = 0;
        bg1y = -Math.abs(ObjectSizeManager.getInstance().getBackgroundHeight() - ObjectSizeManager.getInstance().getScreenH());
        bg2x = 0;
        bg2y = bg1y - ObjectSizeManager.getInstance().getBackgroundHeight() + backgroundAcross;
        scaleBackgroundBitmap();
    }

    private void calculateObjectSizeOnScreen() {
        scaleFactor = (float) ObjectSizeManager.getInstance().getBackgroundWidth()/(float)bmpBackGround1.getWidth();
        backgroundHeightOnScreen = (int)((float)bmpBackGround1.getHeight()*scaleFactor);
        ObjectSizeManager.getInstance().setBackgroundHeight(backgroundHeightOnScreen);
    }

    private void scaleBackgroundBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        bmpBackGround1OnScreen = Bitmap.createBitmap(bmpBackGround1,
                0,0,ObjectSizeManager.getInstance().getBackgroundWidth(),
                ObjectSizeManager.getInstance().getBackgroundHeight(),
                matrix,false);
        bmpBackGround2OnScreen = Bitmap.createBitmap(bmpBackGround2,
                0,0,ObjectSizeManager.getInstance().getBackgroundWidth(),
                ObjectSizeManager.getInstance().getBackgroundHeight(),
                matrix,false);
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bmpBackGround1OnScreen,bg1x,bg1y,paint);
        canvas.drawBitmap(bmpBackGround2OnScreen,bg2x,bg2y,paint);
    }

    public void update(){
        bg1y += speed;
        bg2y += speed;
        if(bg1y>ObjectSizeManager.getInstance().getScreenH()){
            bg1y = bg2y - ObjectSizeManager.getInstance().getBackgroundHeight() + backgroundAcross;
        }
        if(bg2y>ObjectSizeManager.getInstance().getScreenH()){
            bg2y = bg1y - ObjectSizeManager.getInstance().getBackgroundHeight() + backgroundAcross;
        }
    }

}
