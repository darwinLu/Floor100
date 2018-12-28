package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

import java.util.Random;

/**
 * Created by lx on 2018-08-08.
 */

public class SpringPlatform extends AbstractPlatform implements IUpdate {

    private Bitmap bmpSpringPlatformCompress;
    private Bitmap bmpSpringPlatformUncompress;
    private Bitmap bmpSpringPlatformNormal;

    public Boolean playerFirstTouchSpring = false;

    private static int SPRING_STATUS_NORMAL = 0;
    private static int SPRING_STATUS_COMPRESS = 1;
    private static int SPRING_STATUS_UNCOMPRESS = 2;

    private int spring_status = SPRING_STATUS_NORMAL;

    public boolean playSpringAnimation = false;

    private int currentFrame = 0;

    private int compressDiffWithNormal;
    private int uncompressDiffWithNormal;

    //随机数,随机产生平台对屏幕的偏移
    private Random rand = new Random();

    public SpringPlatform(int existPlatformNumber, Bitmap bmpPlatform,Bitmap bmpSpringPlatformCompress,Bitmap bmpSpringPlatformUncompress) {
        this.bmpSpringPlatformCompress = bmpSpringPlatformCompress;
        this.bmpSpringPlatformUncompress = bmpSpringPlatformUncompress;
        this.bmpSpringPlatformNormal = bmpPlatform;
        calculateObjectSizeOnScreen();
        countCompressAndUncompressDiff();
        this.x = -ObjectSizeManager.getInstance().getPlatformWidth()/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        isOnScreen = true;
    }

    private void countCompressAndUncompressDiff() {
        compressDiffWithNormal = (int)((bmpSpringPlatformNormal.getHeight() - bmpSpringPlatformCompress.getHeight())*scaleFactor);
        uncompressDiffWithNormal =(int)((bmpSpringPlatformUncompress.getHeight() - bmpSpringPlatformNormal.getHeight())*scaleFactor);
    }

    private void calculateObjectSizeOnScreen() {
        originWidth = bmpSpringPlatformNormal.getWidth();
        originHeight = bmpSpringPlatformNormal.getHeight();
        onScreenHeight = ObjectSizeManager.getInstance().getPlatformThickness();
        scaleFactor = ((onScreenHeight))/(float)bmpSpringPlatformNormal.getHeight();
        onScreenWidth = (int)((float)bmpSpringPlatformNormal.getWidth()*scaleFactor);
        ObjectSizeManager.getInstance().setPlatformWidth(onScreenWidth);
    }

    @Override
    public void update(GameSurfaceView view) {
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

    @Override
    public void draw(Canvas canvas,Paint paint) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap springPlatformNormalBitmap = Bitmap.createBitmap(bmpSpringPlatformNormal,
                0,0,
                bmpSpringPlatformNormal.getWidth(),bmpSpringPlatformNormal.getHeight(),
                matrix,false);
        Bitmap springPlatformCompressBitmap = Bitmap.createBitmap(bmpSpringPlatformCompress,
                0,0,
                bmpSpringPlatformCompress.getWidth(),bmpSpringPlatformCompress.getHeight(),
                matrix,false);
        Bitmap springPlatformUncompressBitmap = Bitmap.createBitmap(bmpSpringPlatformUncompress,
                0,0,
                bmpSpringPlatformUncompress.getWidth(),bmpSpringPlatformUncompress.getHeight(),
                matrix,false);
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
    public void addEffectToPlayer(GameSurfaceView view) {
        return;
    }


}
