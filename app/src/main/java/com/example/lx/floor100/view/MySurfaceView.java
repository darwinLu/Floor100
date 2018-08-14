package com.example.lx.floor100.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.lx.floor100.MainActivity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.hud.Progress;
import com.example.lx.floor100.R;
import com.example.lx.floor100.hud.Rank;
import com.example.lx.floor100.entity.BackGround;
import com.example.lx.floor100.entity.FloorPlatform;
import com.example.lx.floor100.entity.LRPlatform;
import com.example.lx.floor100.entity.Platform;
import com.example.lx.floor100.entity.Player;
import com.example.lx.floor100.entity.RollingPlatform;
import com.example.lx.floor100.entity.SpringPlatform;
import com.example.lx.floor100.entity.UDPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by lx on 2017-05-22.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //FPS
    private static final int FRAME_PRE_SECOND = 50;
    //△t
    public static final int DELTA_TIME = 1000 / FRAME_PRE_SECOND;

    public static int screenW, screenH;

    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;

    //主循环线程
    private Thread gameLoopThread;
    //主循环标志
    private boolean gameIsRunning;

    private BackGround backGround;
    public Player player;
    private Platform floor;
    private LinkedList<Platform> platformList = new LinkedList<>();
    private int platformNumber;
    public Progress progress;
    public Rank rank;

    //图片
    private Bitmap bmpPlayer;
    private Bitmap bmpPlatform;
    private Bitmap bg;

    private List<IUpdate> updateObjects = new ArrayList<>();

    //是否滚动背景标志
    public boolean isRollingBackground = false;
    private int bg_x = 0;
    private int bg_y = 0;
    public int rollingDistance = 0;

    Random rand = new Random();

    private Handler mHandler;
    //音乐
    private final int MEDIAPLAYER_PAUSE = 0;
    private final int MEDIAPLAYER_PLAY = 1;
    private final int MEDIAPLAYER_STOP = 2;
    private int mediaState = 0;
    private MediaPlayer mediaPlayer;
    private int currentTime;
    private int musicMaxTime;
    private int currentVol;
    private int setTime = 5000;
    private AudioManager am;
    //音效
    public SoundPool soundPool;
    public int jumpSound;
    public int powerSound;


    public MySurfaceView(final Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        setFocusable(true);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int score = msg.getData().getInt("score");
                LayoutInflater factory =LayoutInflater.from(context);
                View dialogView = factory.inflate(R.layout.game_over_layout,null);
                dialogView.setFocusableInTouchMode(true);
                dialogView.requestFocus();
                TextView scoreText = (TextView) dialogView.findViewById(R.id.game_score);
                scoreText.setText(""+score);
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(dialogView).create();
                dialog.setCancelable(false);
                dialog.show();
                dialogView.findViewById(R.id.restart_game).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        RestartGame();
                    }
                });
                dialogView.findViewById(R.id.end_game).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ((MainActivity)context).finish();
                    }
                });
                //gameIsRunning = false;
            }
        };
    }

    private void RestartGame() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = MediaPlayer.create(this.getContext(),R.raw.bgm1);
        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(100)
                .build();
        jumpSound = soundPool.load(this.getContext(),R.raw.jump,0);
        powerSound = soundPool.load(this.getContext(),R.raw.power,0);

//        musicMaxTime = mediaPlayer.getDuration();
        //am = (AudioManager)this.getContext().getSystemService(Context.AUDIO_SERVICE);
        //((MainActivity)this.getContext()).setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        try {
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        gameLoopThread = new Thread(this);
        gameLoopThread.start();

    }

    @Override
    public void run() {
        init_game();
        while (gameIsRunning) {
            long start = System.currentTimeMillis();
            updateGame();
            displayGame();
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

    private void init_game() {
        //获取屏幕高度宽度
        screenW = this.getWidth();
        screenH = this.getHeight();
        //初始化画笔
        paint = new Paint();
        //加载图片资源
        bg = BitmapFactory.decodeResource(this.getResources(),R.drawable.bg_gray);
        bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.sola);
        bmpPlatform = BitmapFactory.decodeResource(this.getResources(), R.drawable.platform);
        //初始化游戏对象
        //清除平台列表
        platformList.clear();
        //背景
        backGround = new BackGround(bg);
        //地面
        floor = new FloorPlatform(0,screenH-Platform.THICKNESS,screenW,bmpPlatform);
        platformList.add(0,floor);
        //平台
        platformNumber = screenH/(Platform.SPACE+Platform.THICKNESS)+2;
        for(int i=1;i<=platformNumber;i++){
            platformList.add(i,new Platform(-110+ screenW/10*rand.nextInt(10),screenH - (i)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform));
        }
        for(int i=0;i<platformList.size();i++){
            updateObjects.add(platformList.get(i));
        }
        //主角
        player = new Player(bmpPlayer);
        updateObjects.add(player);
        //设置主角初始所在的平台
        player.platform = floor;
        //设置力量条
        progress = new Progress(10,0,500,30,10);
        //设置积分牌
        rank = new Rank(0);
        //设置游戏开始标志位，开启主循环
        gameIsRunning = true;
    }

    private void displayGame() {
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);
                backGround.draw(canvas,paint);
//                canvas.drawBitmap(bg,bg_x,bg_y,paint);
//                platform.platformDraw(canvas,paint);
                for(Iterator<Platform> platformIterator = platformList.iterator();platformIterator.hasNext();){
                    platformIterator.next().platformDraw(canvas);
                }
                player.playerDraw(canvas,paint);
                progress.draw(canvas,paint);
                rank.draw(canvas,paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void updateGame() {
//        player.update();
        //步进所有物理模拟并进行碰撞检测
        if(isRollingBackground){
//            bg_y += rollingDistance;
            backGround.speed = rollingDistance;
            backGround.update();
            for(int i=0;i<platformList.size();i++){
                platformList.get(i).y += rollingDistance;
            }
        }
        for (int i=0;i<updateObjects.size();i++){
            updateObjects.get(i).update(this);
        }
        progress.udpate();
        checkEveryPlatformIsDead();
        checkCollision(player,platformList);
        checkGameOver();
//        for(Iterator<Platform> platformIterator = platformList.iterator(); platformIterator.hasNext();){
//            checkCollision(player,platformIterator.next());
//        }
//        if(player.x > player.platform.x+player.platform.length ||player.x +bmpPlayer.getWidth() < player.platform.x){
//            player.jumpSpeed = 0 ;
//            player.jump();
//        }
//        if(checkCollision(player,platformList)){
//            player.isJumping = false;
//        };
    }

    private void checkGameOver() {
        if(player.y>screenH+player.playerHeight){
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("score",rank.getCurrentRank());
            message.setData(bundle);
            mHandler.sendMessage(message);
            //Log.d("DIALOG","show dialog");
            gameIsRunning = false;
        }
    }

    private void checkEveryPlatformIsDead() {
        if(platformList.getFirst().y>screenH){
            platformList.remove();
            rank.addRank();
            int typeNumber = rand.nextInt(10);
            Platform newPlatform;
            if(typeNumber>7){
                newPlatform = new Platform(-110+ screenW/10*typeNumber,screenH - (platformList.size()+1)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform);
            }
            else if(typeNumber>4 && typeNumber<7){
                newPlatform = new RollingPlatform(-110+ screenW/10*typeNumber,screenH - (platformList.size()+1)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform);
            }
            else if(typeNumber>2 && typeNumber<=4){
                newPlatform = new UDPlatform(-110+ screenW/10*typeNumber,screenH - (platformList.size()+1)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform);
            }
            else if(typeNumber>1 && typeNumber<=2){
                newPlatform = new SpringPlatform(-110+ screenW/10*typeNumber,screenH - (platformList.size()+1)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform);
            }
            else {
                newPlatform = new LRPlatform(-110 + screenW / 10 * typeNumber, screenH - (platformList.size() + 1) * (Platform.SPACE + Platform.THICKNESS), 500, bmpPlatform);
            }
            platformList.add(newPlatform);
            updateObjects.add(newPlatform);
        }
//        int oldSize = platformList.size();
//        Log.d("PLATFORM","本次检测总计有平台"+oldSize+"个");
//        for(int i=0;i<oldSize;i++){
//            Platform platform = platformList.get(i);
//            if(platform.isOnScreen == false){
//                platformList.remove(i);
//                Log.d("PLATFORM","移除第"+i+"个平台");
//                //移除最下方平台，在最上方增加平台
////                Random rand = new Random(10);
////                platformList.add(platformList.size(),new Platform(-110+ screenW/10*rand.nextInt(10),screenH - (platformList.size()+1)*(Platform.SPACE+Platform.THICKNESS),220,bmpPlatform));
//                platformList.add(platformList.size(),new Platform(screenW/2,platformList.get(platformList.size()-1).y-Platform.SPACE-Platform.THICKNESS,220,bmpPlatform));
//                Log.d("PLATFORM","增加平台后，总计有"+platformList.size()+"个");
//            }
//        }

    }


    private void checkCollision (Player player,List platformList){
        if(!player.isJumping){
            if(player.isOutOfPlatform()){
                player.vy = 0;
                player.isJumping = true;
            }
        }
        else {
            //使用光线投射法检测碰撞

            for(int i=0;i<platformList.size();i++){
                if(player.isCollisionWithPlatform((Platform)platformList.get(i))){
                    player.y = ((Platform) platformList.get(i)).y - player.playerHeight;
                    //System.out.println("player y:"+player.y+","+((Platform) platformList.get(i)).y+","+player.frameH);
                    player.isJumping = false;
                    player.isOnPlatform = true;
                    player.platform = (Platform)platformList.get(i);
                }
            }
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.stop();
        gameIsRunning = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        progress.onTouchEvent(event,this);
        player.onTouchEvent(event,this);
        return true;
    }

}
