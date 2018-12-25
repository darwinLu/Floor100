package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

import java.util.Random;

/**
 * Created by lx on 2017-06-19.
 */

public class Platform implements IUpdate {

    //平台显示位置左上角坐标
    public int x;
    public int y;
    //平台实际显示宽度和高度
    private int platformWidthOnScreen;
    private int platformHeightOnScreen;
    //平台长度
    public int length;
    //平台图片
    protected Bitmap bmpPlatform;
    //缩放后平台图片
    private Bitmap platformBitmapOnScreen;
    //平台是否有效的标志位
    public boolean isOnScreen;

    float scaleFactor;

    //随机数,随机产生平台对屏幕的偏移
    private Random rand = new Random();

    public Platform(){}

    public Platform(int existPlatformNumber,Bitmap bmpPlatform){
        this.bmpPlatform = bmpPlatform;
        countPlatformWidthOnScreen();
        this.x = -ObjectSizeManager.getInstance().getPlatformWidth()/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        this.length = ObjectSizeManager.getInstance().getPlatformWidth();
        scalePlatformBitmap();
        //平台实例化时初始为有效，显示在屏幕上
        this.isOnScreen = true;
    }

    private void countPlatformWidthOnScreen() {
        scaleFactor = (((float) ObjectSizeManager.getInstance().getPlatformThickness()))/(float)bmpPlatform.getHeight();
        platformWidthOnScreen = (int)((float)bmpPlatform.getWidth()*scaleFactor);
        ObjectSizeManager.getInstance().setPlatformWidth(platformWidthOnScreen);
        platformHeightOnScreen = ObjectSizeManager.getInstance().getPlatformThickness();
    }

    private void scalePlatformBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
       platformBitmapOnScreen = Bitmap.createBitmap(bmpPlatform,
                0,0,
                bmpPlatform.getWidth(),bmpPlatform.getHeight(),
                matrix,false);
    }

    public void platformDraw(Canvas canvas,Paint paint){
        if(isOnScreen) {
            canvas.drawBitmap(platformBitmapOnScreen,x,y,paint);
        }
    }


    @Override
    public void update(GameSurfaceView view) {
        if(y>ObjectSizeManager.getInstance().getScreenH()){
            isOnScreen = false;
        }
    }

    public void addEffectToPlayer(GameSurfaceView view){
        return;
    }
}
