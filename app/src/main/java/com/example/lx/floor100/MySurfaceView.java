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
    Bitmap bmpPlayer;
    Bitmap bmpPlatform;
    Bitmap bg;
    int platformNumber;


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

    private void init() {
        paint = new Paint();
        screenW = this.getWidth();
        screenH = this.getHeight();
        bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.mipmap.cat);
        bmpPlatform = BitmapFactory.decodeResource(this.getResources(), R.mipmap.platform);
        bg = BitmapFactory.decodeResource(this.getResources(),R.mipmap.bg);
        floor = new Platform(0,screenH,screenW,0,bmpPlatform);
        init_player_x = 0;
        init_player_y = this.getHeight()-bmpPlayer.getHeight();
        player = new Player(init_player_x,init_player_y,bmpPlayer);
        platformNumber = screenH/(Platform.SPACE+Platform.THICKNESS);
        for(int i=0;i<platformNumber;i++){
            platformList.add(i,new Platform(i*40,screenH - (i+1)*(Platform.SPACE+Platform.THICKNESS),70,i+1,bmpPlatform));
        }
        player.platform = floor;
        game_is_running = true;
    }

    private void myDraw() {
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(bg,0,0,paint);
                for(int i=0;i<platformNumber;i++){
                    platformList.get(i).platformDraw(canvas,paint);
                }
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

    private void logic() {
        player.move();
        if(checkCollision(player,platformList)){
            player.isJumping = false;
        };
    }


    private boolean checkCollision(Player player,List<Platform> platformList){
        for(int i=0;i<platformNumber;i++){
            if(player.y + bmpPlayer.getHeight() > platformList.get(i).y && (player.jumpState == player.isFalling)){
                player.isJumping = false;
                player.platform = platformList.get(i);
                return true;
            }
        }
        if(player.y + bmpPlayer.getHeight() > floor.y){
            player.isJumping = false;
            player.platform = floor;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        init();
        while (game_is_running) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
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
        if(event.getAction()==MotionEvent.ACTION_UP){
            Log.e("Touch","on Touch Event");
            player.jumpSpeed = 10000;
            player.jump();
        }
        return true;
    }
}
