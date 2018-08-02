package com.example.lx.floor100;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lx on 2017-05-31.
 */

public class Player extends Entity implements IUpdate{

    //主角实时位置
    public int x;
    public int y;
    //水平方向移动初速度
    private int moveSpeed = 5;
    //水平方向实时速度
//    private int vx;
    //垂直方向起跳初速度,单位dpi/s
    public int jumpSpeed;
    //重力加速度,单位m/s2，方向向下
    public double g = -30;
    //垂直方向实时速度
    public double vy;

    //水平方向常量
    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;
    //水平移动方向，初始向右
    private int direction = DIRCTION_RIGHT;

    public boolean isMoving;
    //跳跃状态标志
    public boolean isJumping;
    //跳跃所处位置标志
//    public boolean jumpState;
    //处于上升段标志
    public boolean isRising;
    //处于下降段标志
    public boolean isFalling;

    //当前主角所在的platform
    public Platform platform;
    public Platform floor;
    //是否处于平台的标志位
    public boolean isOnPlatform = false;
    public boolean isOnFloor = true;

    //主角图片（由构造函数传入）
    private Bitmap bmpPlayer;

    public Thread jumpThread;

    //主角行走图帧号
    private int currentFrame;
    private int changeFrameCount;
    int frameW;
    int frameH;


    //构造函数
    public Player(Bitmap bmpPlayer){
        this.bmpPlayer = bmpPlayer;
//        frameW = bmpPlayer.getWidth()/4 - 20;
//        frameH = bmpPlayer.getHeight()/4 - 30;
        frameW = bmpPlayer.getWidth()/4;
        frameH = bmpPlayer.getHeight()/4;
        this.x = 0;
        this.y = MySurfaceView.screenH - frameH;
        isMoving = true;
    }

    //此函数在每次主循环中执行一次
    @Override
    public void update(MySurfaceView view) {
        changeFrame();
        if(isMoving){
            move(view);
        }
        if(isJumping){
            jump(view);
        }
//        checkIfOnPlatform();
    }

    private void changeFrame() {
        changeFrameCount++;
        if(changeFrameCount > 6){
            if(direction == DIRCTION_RIGHT){
                currentFrame++;
                if(currentFrame>=12){
                    currentFrame = 8;
                }
            }
            else {
                currentFrame++;
                if(currentFrame>=8){
                    currentFrame = 4;
                }
            }
            changeFrameCount = 0;
        }
    }

    public void move(MySurfaceView view){
        //无论何种情况，水平方向都一直左右移动
        if(direction == DIRCTION_RIGHT){
            x = x + moveSpeed;
            if(x + frameW > MySurfaceView.screenW){
                x = MySurfaceView.screenW - frameW;
                direction = DIRCTION_LEFT;
            }
        }
        else if(direction == DIRCTION_LEFT){
            x = x - moveSpeed;
            if(x < 0){
                x = 0;
                direction = DIRCTION_RIGHT;
            }
        }
    }

    public void jump(MySurfaceView view) {
        double next_vy = vy + MySurfaceView.DELTA_TIME * g / 1000;
        int deltaY;
        if(vy > 0 && next_vy > 0) {
            vy = next_vy;
            deltaY = (int)((MySurfaceView.screenH/100)*((vy * MySurfaceView.DELTA_TIME / 1000 + (g/2)*MySurfaceView.DELTA_TIME*MySurfaceView.DELTA_TIME/1000/1000)));
            if((y - deltaY) <MySurfaceView.screenH/3){
                view.isRollingBackground = true;
                view.rollingDistance = deltaY;
            }
            else {
                y = y - deltaY;
            }
            return;
        }
        if(vy > 0 && next_vy < 0){
            vy = 0;
            view.isRollingBackground = false;
            view.rollingDistance = 0;
//            isRising = false;
//            isFalling = true;
            deltaY = (int)((MySurfaceView.screenH/100)*((vy * MySurfaceView.DELTA_TIME / 1000 + (g/2)*MySurfaceView.DELTA_TIME*MySurfaceView.DELTA_TIME/1000/1000)));
            y = y - deltaY;
            return;
        }
        if(vy<=0){
            vy = next_vy;
            deltaY = (int)((MySurfaceView.screenH/100)*((vy * MySurfaceView.DELTA_TIME / 1000 + (g/2)*MySurfaceView.DELTA_TIME*MySurfaceView.DELTA_TIME/1000/1000)));
            y = y - deltaY;
        }
//        if(y>MySurfaceView.screenH - frameH){
//            y = MySurfaceView.screenH - frameH;
//            isJumping = false;
//            platform = floor;
//        }
    }

    public void playerDraw(Canvas canvas, Paint paint){
        int frame_x = currentFrame % 4 * frameW;
        int frame_y = currentFrame / 4 * frameH;
//        int frame_x = currentFrame % 4 * (bmpPlayer.getWidth()/4) + 10;
//        int frame_y = currentFrame / 4 * (bmpPlayer.getHeight()/4) + 15;
        canvas.save();
        canvas.clipRect(x,y,x+bmpPlayer.getWidth()/4,y+bmpPlayer.getHeight()/4);
        //canvas.clipRect(x,y,x+frameW,y+frameH);
//        if(direction == DIRCTION_RIGHT){
//            canvas.scale(-1,1,x-frame_x+bmpPlayer.getWidth()/2,y-frame_y+bmpPlayer.getHeight()/2);
//            //canvas.scale(-1,1,x-frame_x+frameW,y-frame_y+frameH);
//        }
        canvas.drawBitmap(bmpPlayer,x-frame_x,y-frame_y,paint);
        canvas.restore();
    }

    public boolean isOutOfPlatform(){
        if(x+frameW<platform.x || x>platform.x+platform.length){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isCollisionWithPlatform(Platform platform){
        if(vy<=0){
            if(x+frameW>platform.x && x<platform.x+platform.length){
                if(Math.abs(y+frameH - platform.y)<10){
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
//        if(platform.isOnScreen){
//            if (x >= platform.x && x >= platform.x+platform.length) {
//                return false;
//            } else if (x <= platform.x && x + frameW <= platform.x) {
//                return false;
//            } else if (y >= platform.y && y >= platform.x + Platform.THICKNESS) {
//                return false;
//            } else if (y <= platform.y && y + frameH <= platform.y) {
//                return false;
//            }
//            return true;
//        }
//        else {
//            return false;
//        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            vy = 50 ;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            isJumping = true;
            isOnPlatform = false;
        }
        return true;
    }

}
