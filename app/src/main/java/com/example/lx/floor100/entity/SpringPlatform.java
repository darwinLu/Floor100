package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.lx.floor100.view.MySurfaceView;

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

    public SpringPlatform(int platform_x, int platform_y, int length, Bitmap bmpPlatform,Bitmap bmpSpringPlatformCompress,Bitmap bmpSpringPlatformUncompress,View gameView) {
        super(platform_x, platform_y, length, bmpPlatform,gameView);
        this.bmpSpringPlatformCompress = bmpSpringPlatformCompress;
        this.bmpSpringPlatformUncompress = bmpSpringPlatformUncompress;
        this.bmpSpringPlatformNormal = bmpPlatform;
    }

    @Override
    public void platformDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        if(isOnScreen) {
            if(spring_status == SPRING_STATUS_NORMAL){
                canvas.drawBitmap(bmpPlatform,x,y,paint);
            }
            else if(spring_status == SPRING_STATUS_COMPRESS){
                canvas.drawBitmap(bmpSpringPlatformCompress,x,y,paint);
            }
            else if(spring_status == SPRING_STATUS_UNCOMPRESS){
                canvas.drawBitmap(bmpSpringPlatformUncompress,x,y,paint);
            }
            //canvas.drawRect(x, y, x + length, y + THICKNESS, paint);
        }
    }

    @Override
    public void update(MySurfaceView view) {
        super.update(view);
        if(playerFirstTouchSpring){
            this.y += 18;
            view.player.addY(18);
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
            this.y -= 18;
            spring_status = SPRING_STATUS_NORMAL;
        }
        else if (currentFrame == 2) {
            this.y -= 15;
            spring_status = SPRING_STATUS_UNCOMPRESS;
        }
        else if (currentFrame == 3) {
            this.y += 15;
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
