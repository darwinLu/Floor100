package com.example.lx.floor100;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by lx on 2017-05-31.
 */

public class Player {

    //主角实时位置
    public int x;
    public int y;
    //水平方向移动初速度
    private int moveSpeed = 10;
    //水平方向实时速度
//    private int vx;
    //垂直方向起跳初速度,单位dpi/s
    public int jumpSpeed;
    //重力加速度,单位m/s2，方向向下
    public double g = -10;
    //垂直方向实时速度
    private double vy;

    //水平方向常量
    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;
    //水平移动方向，初始向右
    private int direction = DIRCTION_RIGHT;

    //跳跃状态标志
    public boolean isJumping;
    //跳跃所处位置标志
//    public boolean jumpState;
    //处于上升段标志
    public boolean isRising = false;
    //处于下降段标志
    public boolean isFalling = false;

    //当前主角所在的platform
    public Platform platform;
    public Platform floor;
    //是否处于平台的标志位
    public boolean isOnPlatform;

    //主角图片（由构造函数传入）
    private Bitmap bmpPlayer;

    public Thread jumpThread;


    //构造函数
    public Player(int init_player_x,int init_player_y,Bitmap bmpPlayer,Platform floor){
        this.bmpPlayer = bmpPlayer;
        x = init_player_x;
        y = init_player_y;
        this.floor = floor ;
    }

    public void move(){
        if(direction == DIRCTION_RIGHT){
            x = x + moveSpeed;
            if(x + bmpPlayer.getWidth() > MySurfaceView.screenW){
                direction = DIRCTION_LEFT;
            }
        }
        else if(direction == DIRCTION_LEFT){
            x = x - moveSpeed;
            if(x < 0){
                direction = DIRCTION_RIGHT;
            }
        }
//        if((x+bmpPlayer.getWidth() < platform.x || x > platform.x + platform.length)){
//            jumpSpeed = 0;
//            jump();
//        }
    }


//    public void playerLogic(){
//        move();
//        if(isJumping){
//            jump();
//        }
//    }

    public void playerDraw(Canvas canvas, Paint paint){
        canvas.drawBitmap(bmpPlayer,x,y,paint);
    }

    public void jump() {
//        isOnPlatform = false;
        isJumping = true;
        isRising = true;
        isFalling = false;
        vy = jumpSpeed;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isJumping) {
                    long start = System.currentTimeMillis();
                    vy = vy + MySurfaceView.DELTA_TIME * g / 1000;
                    if(vy < 0){
                        isRising = false;
                        isFalling = true;
                    }
                    y = y - (int)((MySurfaceView.screenH/100)*((vy * MySurfaceView.DELTA_TIME / 1000 + (g/2)*MySurfaceView.DELTA_TIME*MySurfaceView.DELTA_TIME/1000/1000)));
                    if(y>MySurfaceView.screenH - bmpPlayer.getHeight()){
                        y = MySurfaceView.screenH - bmpPlayer.getHeight();
                        isJumping = false;
                        platform = floor;
                    }
//                    if(vy > 0 ){
//                        jumpState = isRising;
//                        vy = vy + MySurfaceView.DELTA_TIME * g / 1000;
//                        y = y + (vy * MySurfaceView.DELTA_TIME / 1000 + (g/2) * MySurfaceView.DELTA_TIME * MySurfaceView.DELTA_TIME / 1000 / 1000) ;
//                    }
//                    else {
//                        jumpState = isFalling;
//                        vy = vy - MySurfaceView.DELTA_TIME * g / 1000;
//                        y = y + ( -vy * MySurfaceView.DELTA_TIME / 1000 + (g/2) * MySurfaceView.DELTA_TIME * MySurfaceView.DELTA_TIME / 1000 / 1000) ;
//                    }
//                    if(y > MySurfaceView.screenH - bmpPlayer.getHeight()){
//                        isJumping = false;
//                    }
                    long end = System.currentTimeMillis();
                    try {
                        if (end - start < MySurfaceView.DELTA_TIME) {
                            Thread.sleep(MySurfaceView.DELTA_TIME - (end - start));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    //    public boolean onTouchEvent(MotionEvent event) {
//        isJumping = true;
//        vy = jumpSpeed;
//        return true;
//    }

}
