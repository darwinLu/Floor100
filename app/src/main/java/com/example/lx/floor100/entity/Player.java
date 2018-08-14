package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.lx.floor100.R;
import com.example.lx.floor100.engine.Entity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2017-05-31.
 */

public class Player extends Entity implements IUpdate {

    //主角实时位置，代表主角左上角位置
    public int x;
    public int y;

    //主角的实际图像和帧图切分后的图像距离
    private int paddingX;
    private int paddingY;

    //主角的宽度和高度，代表实际的碰撞体积
    public int playerWidth;
    public int playerHeight;

    //水平方向移动初速度，每次主循环的移动增量
    private int moveSpeed = 5;

    //垂直方向起跳初速度,单位dpi/s
    public int jumpSpeed;

    //重力加速度,单位m/s2，方向向下
    public double g = -30;
    //垂直方向实时速度
    public double vy;

    //水平移动方向常量
    public final int DIRCTION_LEFT = 0;
    public final int DIRCTION_RIGHT = 1;
    //水平移动方向，初始向右
    public int direction = DIRCTION_RIGHT;

    //是否移动的标志
    public boolean isMoving;
    //是否跳跃的标志
    public boolean isJumping;
    //处于上升段标志
    public boolean isRising;
    //处于下降段标志
    public boolean isFalling;

    //当前主角所在的platform
    public Platform platform;
    //是否处于平台的标志位
    public boolean isOnPlatform = false;

    //主角图片（由构造函数传入）
    private Bitmap bmpPlayer;

    //主角行走图帧号
    private int currentFrame = 0;
    private int changeFrameCount;

    //主角帧动画的宽度和高度，结合主角的实际宽高，可以确定如何裁剪帧动画
    public int frameW;
    public int frameH;

    //控制角色帧图在第几行循环
    private int indexLine;


    //构造函数
    public Player(Bitmap bmpPlayer){
        this.bmpPlayer = bmpPlayer;
//        frameW = bmpPlayer.getWidth()/4 - 20;
//        frameH = bmpPlayer.getHeight()/4 - 30;
        frameW = bmpPlayer.getWidth()/4;
        frameH = bmpPlayer.getHeight()/4;
        playerWidth = bmpPlayer.getWidth()/4 - 20;
        playerHeight = bmpPlayer.getHeight()/4 - 40;
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
        if(!isJumping){
            if(!isOutOfPlatform()){
                platform.addEffectToPlayer(view);
            }
        }
//        checkIfOnPlatform();
    }

    private void changeFrame() {
        changeFrameCount++;
        //每隔6+2帧修改一次动画
        if(changeFrameCount > 6){
            if(direction == DIRCTION_RIGHT){
                indexLine = 2;
            }
            else {
                indexLine = 1;
            }
            currentFrame++;
            if(currentFrame == 4){
                currentFrame = 0;
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
        int frame_y = frameH * indexLine;
//        int frame_x = currentFrame % 4 * (bmpPlayer.getWidth()/4) + 10;
//        int frame_y = currentFrame / 4 * (bmpPlayer.getHeight()/4) + 15;
        canvas.save();
        canvas.clipRect(x,y,x+playerWidth,y+playerHeight);
        //canvas.clipRect(x,y,x+frameW,y+frameH);
//        if(direction == DIRCTION_RIGHT){
//            canvas.scale(-1,1,x-frame_x+bmpPlayer.getWidth()/2,y-frame_y+bmpPlayer.getHeight()/2);
//            //canvas.scale(-1,1,x-frame_x+frameW,y-frame_y+frameH);
//        }
        canvas.drawBitmap(bmpPlayer,x-frame_x-10,y-frame_y-20,paint);
        //paint.setColor(Color.RED);
        //canvas.drawRect(x,y,x+playerWidth,y+playerHeight,paint);
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
                if(Math.abs(y+playerHeight - platform.y)<10){
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

    public boolean onTouchEvent(MotionEvent event,MySurfaceView view) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            if(platform.getClass() == SpringPlatform.class){
//                //vy = 100;
//                vy = view.progress.getEndValue()*10 + 50 ;
//            }
//            else{
//                //vy = 50 ;
//                vy = view.progress.getEndValue()*10;
//            }
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(isJumping == false){
                view.soundPool.play(view.jumpSound,1,1,0,0,1);
                if(platform.getClass() == SpringPlatform.class){
                    //vy = 100;
                    vy = view.progress.getEndValue()*10 + 50 ;
                }
                else{
                    //vy = 50 ;
                    vy = view.progress.getEndValue()*10;
                }
                isJumping = true;
                isOnPlatform = false;
            }
        }
        return true;
    }

    public void addX(int deltaX){
        x = x + deltaX;
    }

    public void addY(int deltaY){
        y = y + deltaY;
    }

}
