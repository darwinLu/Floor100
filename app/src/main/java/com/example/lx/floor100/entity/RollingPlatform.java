package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    public RollingPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform) {
        super(platform_x, platform_y, length, bmpPlatform);
        Random rand = new Random();
        direction = rand.nextInt(2);
        frameW = bmpPlatform.getWidth()/8;
        frameH = bmpPlatform.getHeight();
    }

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
    public void platformDraw(Canvas canvas) {
        int frame_x = currentFrame % 8 * frameW;
        int frame_y = 0;
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        if(isOnScreen) {
            canvas.save();
            canvas.clipRect(x,y,x+length,y+THICKNESS);
            if(direction == DIRCTION_LEFT){
                frame_x = (7 - currentFrame % 8) * frameW;
            }
            canvas.drawBitmap(bmpPlatform,x-frame_x,y-frame_y,paint);
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
