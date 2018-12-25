package com.example.lx.floor100.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2017-05-31.
 */

public class Player implements IUpdate {

    //帧动画总数，用来切分帧动画图
    private int frameCount = 11;

    //帧动画的宽度，可以用传入的图片长度除以总帧数（单行横向帧图）
    public int frameW;
    //帧动画的高度，等于传入图片的高度（单行横向帧图）
    public int frameH;

    //主角的实际图像距离帧图各边的边距
    private int leftPadding = 10;
    private int rightPadding = 10;
    private int topPadding = 30;
    private int bottomPadding = 70;

    //主角实时位置，代表主角的实际有效图形的左上角位置
    public int x;
    public int y;
    //主角的实际宽度和高度，代表有效的碰撞体积，可以用帧图宽高减去各方向间距
    private int playerWidthOnScreen;
    private int playerHeightOnScreen;

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

    //当前主角所在的platform
    public Platform platform;
    //是否处于平台的标志位
    public boolean isOnPlatform = false;

    //主角图片（由构造函数传入）
    private Bitmap bmpPlayer;
    private Bitmap bmpPlayerJump;

    //主角行走图帧号
    private int currentFrame = 0;
    private int changeFrameCount;

    //控制角色帧图在第几行循环
    private int indexLine;

    //缩放系数
    private float scaleFactor;


    //构造函数
    public Player(Bitmap bmpPlayer,Bitmap bmpPlayerJump,View gameView){
        this.bmpPlayerJump = bmpPlayerJump;
        this.bmpPlayer = bmpPlayer;
        //frameW = bmpPlayer.getWidth()/frameCount;
        //frameH = bmpPlayer.getHeight();
        //playerRealWidth = frameW - leftPadding - rightPadding;
        //playerRealHeight = frameH - topPadding  - bottomPadding;
        //scaleBitmap(playerRealWidth,playerRealHeight,gameView);
        playerWidthOnScreen = ObjectSizeManager.getInstance().getPlayerWidth();
        countPlayerHeightOnScreen();
        //platformThickness = ObjectSizeManager.getInstance().getPlatformThickness();
        this.x = 0;
        this.y = ObjectSizeManager.getInstance().getScreenH() - ObjectSizeManager.getInstance().getPlayerHeight() - ObjectSizeManager.getInstance().getFloorHeight();
        isMoving = true;
    }

    private void countPlayerHeightOnScreen() {
        frameW = bmpPlayer.getWidth()/frameCount;
        frameH = bmpPlayer.getHeight();
        int playerBitmapWidth = frameW - leftPadding - rightPadding;
        scaleFactor = (((float)ObjectSizeManager.getInstance().getPlayerWidth()+leftPadding+rightPadding))/(float)playerBitmapWidth;
        playerHeightOnScreen = (int)((float)(bmpPlayer.getHeight() - topPadding - bottomPadding)*scaleFactor);
        ObjectSizeManager.getInstance().setPlayerHeight(playerHeightOnScreen);
//
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleFactor,scaleFactor);
//        bmpPlayer = Bitmap.createBitmap(bmpPlayer,0,0,bmpPlayer.getWidth(),bmpPlayer.getHeight(),matrix,false);
//        bmpPlayerJump = Bitmap.createBitmap(bmpPlayerJump,0,0,bmpPlayerJump.getWidth(),bmpPlayerJump.getHeight(),matrix,false);
//        frameW = bmpPlayer.getWidth();
//        frameH = bmpPlayer.getHeight();
    }



//    private void scaleBitmap(int playerRealWidthOrigin,int playerRealHeightOrigin,View gameView) {
//        float scalePlayerWidth = (((float)gameView.getWidth())/8)/(float)playerRealWidthOrigin;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scalePlayerWidth,scalePlayerWidth);
//        bmpPlayer = Bitmap.createBitmap(bmpPlayer,0,0,bmpPlayer.getWidth(),bmpPlayer.getHeight(),matrix,false);
//        bmpPlayerJump = Bitmap.createBitmap(bmpPlayerJump,0,0,bmpPlayerJump.getWidth(),bmpPlayerJump.getHeight(),matrix,false);
//        frameW = bmpPlayer.getWidth()/frameCount;
//        frameH = bmpPlayer.getHeight();
//        leftPadding = (int)((float)leftPadding * scalePlayerWidth);
//        rightPadding = (int)((float)rightPadding * scalePlayerWidth);
//        topPadding = (int)((float)topPadding * scalePlayerWidth);
//        bottomPadding = (int)((float)bottomPadding * scalePlayerWidth);
//        //playerRealWidth = frameW - leftPadding - rightPadding;
//        playerRealHeight = frameH - topPadding  - bottomPadding;
//    }

    //此函数在每次主循环中执行一次
    @Override
    public void update(GameSurfaceView view) {
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
        if(!isJumping){
            changeFrameCount++;
            //每隔6+2帧修改一次动画
            if(changeFrameCount > 6){
                if(direction == DIRCTION_RIGHT){
                    indexLine = 0;
                }
                else {
                    indexLine = 0;
                }
                currentFrame++;
                if(currentFrame == 8){
                    currentFrame = 0;
                }
                changeFrameCount = 0;
            }
        }

    }

    public void move(GameSurfaceView view){
        //无论何种情况，水平方向都一直左右移动
        int playerRealWidth = ObjectSizeManager.getInstance().getPlayerWidth();
        if(direction == DIRCTION_RIGHT){
            x = x + moveSpeed;
            if(x + playerRealWidth > ObjectSizeManager.getInstance().getScreenW()){
                x = ObjectSizeManager.getInstance().getScreenW() - playerRealWidth;
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

    public void jump(GameSurfaceView view) {
        double next_vy = vy + GameSurfaceView.DELTA_TIME * g / 1000;
        int deltaY;
        if(vy > 0 && next_vy > 0) {
            vy = next_vy;
            deltaY = (int)((ObjectSizeManager.getInstance().getScreenH()/100)*((vy * GameSurfaceView.DELTA_TIME / 1000 + (g/2)* GameSurfaceView.DELTA_TIME* GameSurfaceView.DELTA_TIME/1000/1000)));
            if((y - deltaY) <ObjectSizeManager.getInstance().getScreenH()/3){
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
            deltaY = (int)((ObjectSizeManager.getInstance().getScreenH()/100)*((vy * GameSurfaceView.DELTA_TIME / 1000 + (g/2)* GameSurfaceView.DELTA_TIME* GameSurfaceView.DELTA_TIME/1000/1000)));
            y = y - deltaY;
            return;
        }
        if(vy<=0){
            vy = next_vy;
            deltaY = (int)((ObjectSizeManager.getInstance().getScreenH()/100)*((vy * GameSurfaceView.DELTA_TIME / 1000 + (g/2)* GameSurfaceView.DELTA_TIME* GameSurfaceView.DELTA_TIME/1000/1000)));
            y = y - deltaY;
        }
//        if(y>MySurfaceView.screenH - frameH){
//            y = MySurfaceView.screenH - frameH;
//            isJumping = false;
//            platform = floor;
//        }
    }

    public void playerDraw(Canvas canvas, Paint paint){
        if(!isJumping){
            int frame_x = currentFrame % frameCount * frameW;
            int frame_y = frameH * indexLine;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            Bitmap playerBitmapOnScreen = Bitmap.createBitmap(bmpPlayer,
                    frame_x+leftPadding,frame_y+topPadding,
                    frameW - leftPadding - rightPadding,frameH - topPadding - bottomPadding,
                    matrix,false);
            canvas.save();
            canvas.clipRect(x,y,x+ObjectSizeManager.getInstance().getPlayerWidth(),
                    y+ObjectSizeManager.getInstance().getPlayerHeight());
            //canvas.clipRect(x,y,x+frameW,y+frameH);
            if(direction == DIRCTION_LEFT){
                canvas.scale(-1,1,x+playerBitmapOnScreen.getWidth()/2,y+playerBitmapOnScreen.getHeight()/2);
                //canvas.scale(-1,1,x-frame_x+frameW,y-frame_y+frameH);
                canvas.drawBitmap(playerBitmapOnScreen,x,y,paint);
            }
            else{
                canvas.drawBitmap(playerBitmapOnScreen,x,y,paint);
            }
            //paint.setColor(Color.RED);
            //canvas.drawRect(x,y,x+playerWidth,y+playerHeight,paint);
            canvas.restore();
        }
        else if (isJumping){
            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor,scaleFactor);
            Bitmap playerBitmapJumpOnScreen = Bitmap.createBitmap(bmpPlayerJump,
                    leftPadding,topPadding,
                    frameW - leftPadding - rightPadding,frameH - topPadding - bottomPadding,
                    matrix,false);
            canvas.save();
            canvas.clipRect(x,y,x+ObjectSizeManager.getInstance().getPlayerWidth(),
                    y+ObjectSizeManager.getInstance().getPlayerHeight());
            //paint.setColor(Color.RED);
            if(direction == DIRCTION_LEFT){
                canvas.scale(-1,1,x+playerBitmapJumpOnScreen.getWidth()/2,y+playerBitmapJumpOnScreen.getHeight()/2);
                //canvas.scale(-1,1,x-frame_x+frameW,y-frame_y+frameH);
                canvas.drawBitmap(playerBitmapJumpOnScreen,x,y,paint);
            }
            else{
                canvas.drawBitmap(playerBitmapJumpOnScreen,x,y,paint);
            }
            //canvas.drawRect(x,y,x+playerWidth,y+playerHeight,paint);
            canvas.restore();
        }
    }

    public boolean isOutOfPlatform(){
        int playerRealWidth = ObjectSizeManager.getInstance().getPlayerWidth();
        if(x+playerRealWidth<platform.x || x>platform.x+platform.length){
            Log.d("looping","now i am true too ,x:"+x+";playerRealWidth:"+playerRealWidth+";platformX:"
            +platform.x+";platformLength:"+platform.length);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isCollisionWithPlatform(Platform platform){

        if(vy<=0){
            if(x+playerWidthOnScreen>platform.x && x<platform.x+platform.length){
                if(Math.abs(y+playerHeightOnScreen - platform.y)<10){
                    Log.d("looping","now i am true");
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

    public boolean onTouchEvent(MotionEvent event, GameSurfaceView view) {
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
                if(view.getContext().getSharedPreferences("option", Context.MODE_PRIVATE)
                        .getString("sound_switch","off").equals("on")){
                    view.soundPool.play(view.jumpSound,1,1,0,0,1);
                }
                if(platform.getClass() == SpringPlatform.class){
                    //vy = 100;
                    vy = view.progress.getEndValue()*10 + 50 ;
                    ((SpringPlatform)platform).playSpringAnimation = true;
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
