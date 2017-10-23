package com.example.lx.floor100;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lx on 2017-05-22.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //每秒帧数
    public static final int FRAME_PRE_SECOND = 25;
    //帧时间
    public static final int DELTA_TIME = 1000 / FRAME_PRE_SECOND;

    //重力加速度，向下为正
    //public static final int G = 200 ;

    //屏幕宽高
    public static int screenW, screenH;

    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;

    //主循环线程
    private Thread th;
    //主循环标志
    private boolean game_is_running;

    //主角的初始X、Y位置
    int init_player_x;
    int init_player_y;

    public Player player;
    public Platform floor;
    public Platform platform;
    private List<Platform> platformList = new ArrayList<Platform>();

    //图片
    Bitmap bmpPlayer;
    Bitmap bmpPlatform;
    Bitmap bg;

//    int platformNumber;


    public MySurfaceView(Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        th = new Thread(this);
        th.start();
    }

    private void init_game() {
        screenW = this.getWidth();
        screenH = this.getHeight();
        paint = new Paint();
        bg = BitmapFactory.decodeResource(this.getResources(),R.mipmap.bg);
        bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.mipmap.cat);
        bmpPlatform = BitmapFactory.decodeResource(this.getResources(), R.mipmap.platform);
        floor = new Platform(0,screenH,screenW,bmpPlatform);
        init_player_x = 0;
        init_player_y = this.getHeight()-bmpPlayer.getHeight();
        player = new Player(init_player_x,init_player_y,bmpPlayer,floor);
        player.platform = floor;
//        setPlatformList();
//        platformNumber = screenH/(Platform.SPACE+Platform.THICKNESS);
//        for(int i=0;i<platformNumber;i++){
//            platformList.add(i,new Platform(i*40,screenH - (i+1)*(Platform.SPACE+Platform.THICKNESS),70,i+1,bmpPlatform));
//        }
//        player.platform = floor;
        platform = new Platform(screenW/2,screenH/2,200,bmpPlatform);
//        player.isOnPlatform = true;
        game_is_running = true;
    }

    private void display_game() {
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(bg,0,0,paint);
                platform.platformDraw(canvas,paint);
//                for(int i=0;i<10;i++){
//                    platformList.get(i).platformDraw(canvas,paint);
//                }
                player.playerDraw(canvas,paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void update_game() {
        player.move();

        checkCollision(player,platform);
        if(player.x > player.platform.x+player.platform.length ||player.x +bmpPlayer.getWidth() < player.platform.x){
            player.jumpSpeed = 0 ;
            player.jump();
        }
//        if(checkCollision(player,platformList)){
//            player.isJumping = false;
//        };
    }


    private void checkCollision (Player player,Platform platform){
        if( player.y < platform.y && player.y+bmpPlayer.getHeight() > platform.y+bmpPlatform.getHeight() && player.x+bmpPlayer.getWidth() > platform.x && player.x < platform.x +bmpPlatform.getWidth()){
            player.y = platform.y - bmpPlayer.getHeight();
            player.isJumping = false;
            player.platform = platform;
        }
    }
//    private boolean checkCollision(Player player,List<Platform> platformList){
//        for(int i=0;i<platformNumber;i++){
//            if(player.y + bmpPlayer.getHeight() > platformList.get(i).y && (player.jumpState == player.isFalling)){
//                player.isJumping = false;
//                player.platform = platformList.get(i);
//                return true;
//            }
//        }
//        if(player.y + bmpPlayer.getHeight() > floor.y){
//            player.isJumping = false;
//            player.platform = floor;
//            return true;
//        }
//        return false;
//    }

    @Override
    public void run() {
        init_game();
        while (game_is_running) {
            long start = System.currentTimeMillis();
            display_game();
            update_game();
            long end = System.currentTimeMillis();
            try {
                if (end - start < DELTA_TIME) {
                    Thread.sleep(DELTA_TIME - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        game_is_running = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            player.jumpSpeed = 20 ;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            player.jump();
        }
        return true;
    }

    private List<Platform> setPlatformList (){
        for(int i=0;i<10;i++){
            platformList.add(new Platform(0,100*i,200,bmpPlatform));
        }
        return platformList;
    }
}
