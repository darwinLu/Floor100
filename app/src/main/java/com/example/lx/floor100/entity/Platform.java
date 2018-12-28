package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.lx.floor100.engine.Enity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

import java.util.Random;

/**
 * Created by lx on 2017-06-19.
 */

public class Platform extends AbstractPlatform implements IUpdate {

    //平台图片
    protected Bitmap bmpPlatform;
    //缩放后平台图片
    private Bitmap platformBitmapOnScreen;

    //随机数,随机产生平台对屏幕的偏移
    private Random rand = new Random();

    public Platform(int existPlatformNumber,Bitmap bmpPlatform){
        this.bmpPlatform = bmpPlatform;
        calculateObjectSizeOnScreen();
        this.x = -ObjectSizeManager.getInstance().getPlatformWidth()/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        //this.length = ObjectSizeManager.getInstance().getPlatformWidth();
        scaleBitmap();
        //平台实例化时初始为有效，显示在屏幕上
        isOnScreen = true;
    }

    private void calculateObjectSizeOnScreen() {
        originWidth = bmpPlatform.getWidth();
        originHeight = bmpPlatform.getHeight();
        onScreenHeight = ObjectSizeManager.getInstance().getPlatformThickness();
        scaleFactor = (((float) onScreenHeight))/(float)bmpPlatform.getHeight();
        onScreenWidth = (int)((float)bmpPlatform.getWidth()*scaleFactor);
        ObjectSizeManager.getInstance().setPlatformWidth(onScreenWidth);
    }

    private void scaleBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        platformBitmapOnScreen = Bitmap.createBitmap(bmpPlatform,
                0,0,
                originWidth,originHeight,
                matrix,false);
    }

    @Override
    public void update(GameSurfaceView view) {
        if(y>ObjectSizeManager.getInstance().getScreenH()){
            isOnScreen = false;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(isOnScreen) {
            canvas.drawBitmap(platformBitmapOnScreen,x,y,paint);
        }
    }

    public void addEffectToPlayer(GameSurfaceView view) {
        return;
    }

}
