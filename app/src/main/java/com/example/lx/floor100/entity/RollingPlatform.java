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

public class RollingPlatform extends Platform {

    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;

    private int direction;

    //传送带图帧号（共8帧，横向排列）
    private int currentFrame = 0;
    private int changeFrameCount;

    //传送带图片的帧高度和宽度
    public int frameW;
    public int frameH;

    private int rollingPlatformWidthOnScreen;

    private Bitmap rollingPlatformBitmapOnScreen;

    private Random rand = new Random();

//    public RollingPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform,View gameView) {
//        super(platform_x, platform_y, length, bmpPlatform,gameView);
//        Random rand = new Random();
//        direction = rand.nextInt(2);
//        frameW = bmpPlatform.getWidth()/8;
//        frameH = bmpPlatform.getHeight();
//    }

    public RollingPlatform(int existPlatformNumber, Bitmap bmpPlatform) {
        super(existPlatformNumber, bmpPlatform);
        countRollingPlatformWidthOnScreen();
        this.x = -frameW/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        Random rand2 = new Random();
        direction = rand2.nextInt(2);
        //scaleRollingPlatformBitmap();
        //isOnScreen = true;
    }

    private void countRollingPlatformWidthOnScreen() {
        frameW = bmpPlatform.getWidth()/8;
        frameH = bmpPlatform.getHeight();
        scaleFactor = (float)ObjectSizeManager.getInstance().getPlatformThickness()/(float)frameH;
        rollingPlatformWidthOnScreen = (int)((float)frameW*scaleFactor);
        length = rollingPlatformWidthOnScreen;
    }

//    private void scaleRollingPlatformBitmap() {
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleFactor,scaleFactor);
//        rollingPlatformBitmapOnScreen = Bitmap.createBitmap(bmpPlatform,
//                0,0,bmpPlatform.getWidth(),
//                bmpPlatform.getHeight(),
//                matrix,false);
//    }

    private void changeFrame() {
        changeFrameCount++;
        //每隔6+2帧修改一次动画
        if(changeFrameCount > 6){
//            if(direction == DIRCTION_RIGHT){
//                indexLine = 2;
//            }
//            else {
//                indexLine = 1;
//            }
            currentFrame++;
            if(currentFrame == 8){
                currentFrame = 0;
            }
            changeFrameCount = 0;
        }
    }

    @Override
    public void platformDraw(Canvas canvas,Paint paint) {
        int frame_x = currentFrame % 8 * frameW;
        int frame_y = 0;

//        Paint paint = new Paint();
//        paint.setColor(Color.YELLOW);
        if(isOnScreen) {
            canvas.save();
            canvas.clipRect(x,y,x+length,y+ ObjectSizeManager.getInstance().getPlatformThickness());
            if(direction == DIRCTION_LEFT){
                frame_x = (7 - currentFrame % 8) * frameW;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            rollingPlatformBitmapOnScreen = Bitmap.createBitmap(bmpPlatform,
                    frame_x,frame_y,
                    frameW,frameH,
                    matrix,false);
            canvas.drawBitmap(rollingPlatformBitmapOnScreen,x,y,paint);
            //canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
            canvas.restore();
        }
    }

    @Override
    public void update(MySurfaceView view) {
        super.update(view);
        changeFrame();
    }

    @Override
    public void addEffectToPlayer(MySurfaceView view) {
        if(direction == DIRCTION_RIGHT){
            view.player.addX(3);
        }
        else if(direction == DIRCTION_LEFT){
            view.player.addX(-3);
        }
    }
}
