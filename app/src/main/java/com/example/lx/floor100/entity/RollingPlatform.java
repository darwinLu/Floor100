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
 * Created by lx on 2018-08-08.
 */

public class RollingPlatform extends AbstractPlatform implements IUpdate{

    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;

    private int direction;

    //传送带图帧号（共8帧，横向排列）
    private int currentFrame = 0;
    private int changeFrameCount;

    //传送带图片的帧高度和宽度
    public int frameW;
    public int frameH;

    private Bitmap rollingPlatformBitmap;
    private Bitmap rollingPlatformBitmapOnScreen;

    private Random rand = new Random();

    public RollingPlatform(int existPlatformNumber, Bitmap bmpPlatform) {
        //super(existPlatformNumber, bmpPlatform);
        this.rollingPlatformBitmap = bmpPlatform;
        calculateObjectSizeOnScreen();
        this.x = -frameW/2 + ObjectSizeManager.getInstance().getScreenW()/10*rand.nextInt(10);
        this.y = ObjectSizeManager.getInstance().getScreenH() - (existPlatformNumber)*(ObjectSizeManager.getInstance().getPlatformSpace()+ObjectSizeManager.getInstance().getPlatformThickness());
        Random rand2 = new Random();
        direction = rand2.nextInt(2);
        //scaleRollingPlatformBitmap();
        isOnScreen = true;
    }

    private void calculateObjectSizeOnScreen() {
        frameW = rollingPlatformBitmap.getWidth()/8;
        frameH = rollingPlatformBitmap.getHeight();
        onScreenHeight = ObjectSizeManager.getInstance().getPlatformThickness();
        scaleFactor = (float)onScreenHeight/(float)frameH;
        onScreenWidth = (int)((float)frameW*scaleFactor);
        ObjectSizeManager.getInstance().setPlatformWidth(onScreenWidth);
    }

    private void changeFrame() {
        changeFrameCount++;
        //每隔6+2帧修改一次动画
        if(changeFrameCount > 6){
            currentFrame++;
            if(currentFrame == 8){
                currentFrame = 0;
            }
            changeFrameCount = 0;
        }
    }

    @Override
    public void update(GameSurfaceView view) {
        if(y>ObjectSizeManager.getInstance().getScreenH()){
            isOnScreen = false;
        }
        changeFrame();
    }

    @Override
    public void draw(Canvas canvas,Paint paint) {
        int frame_x = currentFrame % 8 * frameW;
        int frame_y = 0;
        if(isOnScreen) {
            canvas.save();
            canvas.clipRect(x,y,x+onScreenWidth,y+ onScreenHeight);
            if(direction == DIRCTION_LEFT){
                frame_x = (7 - currentFrame % 8) * frameW;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            rollingPlatformBitmapOnScreen = Bitmap.createBitmap(rollingPlatformBitmap,
                    frame_x,frame_y,
                    frameW,frameH,
                    matrix,false);
            canvas.drawBitmap(rollingPlatformBitmapOnScreen,x,y,paint);
            canvas.restore();
        }
    }

    @Override
    public void addEffectToPlayer(GameSurfaceView view) {
        if(direction == DIRCTION_RIGHT){
            view.player.addX(3);
        }
        else if(direction == DIRCTION_LEFT){
            view.player.addX(-3);
        }
    }
}
