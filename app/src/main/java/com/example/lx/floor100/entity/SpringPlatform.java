package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.MySurfaceView;

import java.util.Random;

/**
 * Created by lx on 2018-08-08.
 */

public class SpringPlatform extends Platform {

    private Bitmap bmpSpringPlatformCompress;
    private Bitmap bmpSpringPlatformUncompress;
    private Bitmap bmpSpringPlatformNormal;

    public Boolean playerFirstTouchSpring = false;

    private static int SPRING_STATUS_NORMAL = 0;
    private static int SPRING_STATUS_COMPRESS = 1;
    private static int SPRING_STATUS_UNCOMPRESS = 2;

    private int spring_status = SPRING_STATUS_NORMAL;

    public Boolean playSpringAnimation = false;

    private int currentFrame = 0;

    private int i=0;

    private int springPlatformWidthOnScreen;
    private int springPlatformHeightOnScreen;

    private int compressDiffWithNormal;
    private int uncompressDiffWithNormal;

    //随机数,随机产生平台对屏幕的偏移
    private Random rand = new Random();

//    public SpringPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform,Bitmap bmpSpringPlatformCompress,Bitmap bmpSpringPlatformUncompress,View gameView) {
//        super(platform_x, platform_y, length, bmpPlatform,gameView);
//        this.bmpSpringPlatformCompress = bmpSpringPlatformCompress;
//        this.bmpSpringPlatformUncompress = bmpSpringPlatformUncompress;
//        this.bmpSpringPlatformNormal = bmpPlatform;
//    }

    public SpringPlatform(int existPlatformNumber, Bitmap bmpPlatform,Bitmap bmpSpringPlatformCompress,Bitmap bmpSpringPlatformUncompress) {
        super(existPlatformNumber, bmpPlatform);
        this.bmpSpringPlatformCompress = bmpSpringPlatformCompress;
        this.bmpSpringPlatformUncompress = bmpSpringPlatformUncompress;
        this.bmpSpringPlatformNormal = bmpPlatform;
        countSpringPlatformWidthOnScreen();
        countCompressAndUncompress();
        this.x = -ObjectSizeManager.getInstance().getPlatformWidth()/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        this.length = ObjectSizeManager.getInstance().getPlatformWidth();
    }

    private void countCompressAndUncompress() {
        compressDiffWithNormal = (int)((bmpSpringPlatformNormal.getHeight() - bmpSpringPlatformCompress.getHeight())*scaleFactor);
        uncompressDiffWithNormal =(int)((bmpSpringPlatformUncompress.getHeight() - bmpSpringPlatformNormal.getHeight())*scaleFactor);
    }

    private void countSpringPlatformWidthOnScreen() {
        scaleFactor = (((float) ObjectSizeManager.getInstance().getPlatformThickness()))/(float)bmpSpringPlatformNormal.getHeight();
        springPlatformWidthOnScreen = (int)((float)bmpSpringPlatformNormal.getWidth()*scaleFactor);
        ObjectSizeManager.getInstance().setPlatformWidth(springPlatformWidthOnScreen);
        springPlatformHeightOnScreen = ObjectSizeManager.getInstance().getPlatformThickness();
    }

    @Override
    public void platformDraw(Canvas canvas,Paint paint) {
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap springPlatformNormalBitmap = Bitmap.createBitmap(bmpPlatform,
                0,0,
                bmpPlatform.getWidth(),bmpPlatform.getHeight(),
                matrix,false);
        Bitmap springPlatformCompressBitmap = Bitmap.createBitmap(bmpSpringPlatformCompress,
                0,0,
                bmpSpringPlatformCompress.getWidth(),bmpSpringPlatformCompress.getHeight(),
                matrix,false);
        Bitmap springPlatformUncompressBitmap = Bitmap.createBitmap(bmpSpringPlatformUncompress,
                0,0,
                bmpSpringPlatformUncompress.getWidth(),bmpSpringPlatformUncompress.getHeight(),
                matrix,false);
        Log.d("rolling","x:"+x+";y:"+y+";");
        if(isOnScreen) {
            if(spring_status == SPRING_STATUS_NORMAL){
                canvas.drawBitmap(springPlatformNormalBitmap,x,y,paint);
            }
            else if(spring_status == SPRING_STATUS_COMPRESS){
                canvas.drawBitmap(springPlatformCompressBitmap,x,y,paint);
            }
            else if(spring_status == SPRING_STATUS_UNCOMPRESS){
                canvas.drawBitmap(springPlatformUncompressBitmap,x,y,paint);
            }
            //canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
        }
    }

    @Override
    public void update(MySurfaceView view) {
        super.update(view);
        if(playerFirstTouchSpring){
            this.y += compressDiffWithNormal;
            view.player.addY(compressDiffWithNormal);
            spring_status = SPRING_STATUS_COMPRESS;
            playerFirstTouchSpring = false;
        }
        if(playSpringAnimation){
            changeSpringPerFrame();
        }
    }

    private void changeSpringPerFrame() {
        currentFrame++;
        if(currentFrame == 1){
            this.y -= compressDiffWithNormal;
            spring_status = SPRING_STATUS_NORMAL;
        }
        else if (currentFrame == 2) {
            this.y -= uncompressDiffWithNormal;
            spring_status = SPRING_STATUS_UNCOMPRESS;
        }
        else if (currentFrame == 3) {
            this.y += uncompressDiffWithNormal;
            spring_status = SPRING_STATUS_NORMAL;
        }
        if(currentFrame > 3) {
            currentFrame = 0;
            playSpringAnimation = false;
        }
    }


    @Override
    public void addEffectToPlayer(MySurfaceView view) {
        super.addEffectToPlayer(view);
    }


}
