package com.example.lx.floor100.view;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.lx.floor100.activity.MainActivity;
import com.example.lx.floor100.activity.RankActivity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.hud.Progress;
import com.example.lx.floor100.R;
import com.example.lx.floor100.hud.Rank;
import com.example.lx.floor100.entity.Background;
import com.example.lx.floor100.entity.FloorPlatform;
import com.example.lx.floor100.entity.LRPlatform;
import com.example.lx.floor100.entity.Platform;
import com.example.lx.floor100.entity.Player;
import com.example.lx.floor100.entity.RollingPlatform;
import com.example.lx.floor100.entity.SpringPlatform;
import com.example.lx.floor100.entity.UDPlatform;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by lx on 2017-05-22.
 */

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //FPS
    private static final int FRAME_PRE_SECOND = 50;
    //△t
    public static final int DELTA_TIME = 1000 / FRAME_PRE_SECOND;

    //SurfaceView绘图对象
    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;

    //主循环线程
    private Thread gameLoopThread;
    //主循环标志
    private boolean gameIsRunning;

    //游戏对象尺寸管理器
    private ObjectSizeManager objectSizeManager;

    //游戏对象
    private Background background;
    public Player player;
    private Platform floor;
    private LinkedList<Platform> platformList = new LinkedList<>();
    private int platformNumber;
    //HUD
    public Progress progress;
    public Rank rank;

    //游戏对象更新容器
    private List<IUpdate> updateObjects = new ArrayList<>();

    //随机数生成器，用来随机生成不同类型的平台
    Random rand = new Random();

    //是否滚动背景标志及背景滚动距离
    public boolean isRollingBackground = false;
    public int rollingDistance = 0;

    //图片资源
    private Bitmap bmpPlayer;
    private Bitmap bmpPlayerJump;
    private Bitmap bmpPlatform;
    private Bitmap bmpBackground;
    private Bitmap bmpRollingPlatform;
    private Bitmap bmpFloorPlatform;
    private Bitmap bmpSpringPlatform;
    private Bitmap bmpSpringPlatformCompress;
    private Bitmap bmpSpringPlatformUncompress;

    //弹出菜单handler
    private Handler mHandler;

    //音乐控制
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

    //音效控制
    public SoundPool soundPool;
    public int jumpSound;
    public int powerSound;


    public GameSurfaceView(final Context context) {
        super(context);
        sfh = this.getHolder();
        sfh.addCallback(this);
        setFocusable(true);
        //暂停菜单
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                final int score = msg.getData().getInt("score");
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
                dialogView.findViewById(R.id.submit_score).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"最高分"+score,Toast.LENGTH_LONG).show();
                        submitScore("Myuserid",score);
                        Intent intent = new Intent(context, RankActivity.class);
                        context.startActivity(intent);
                    }
                });
                dialogView.findViewById(R.id.end_game).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ((MainActivity)context).finish();
                    }
                });
            }
        };
    }

    private void submitScore(final String userId, final int score) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    String result = "";
                    URL url = new URL("http://106.12.211.73:8080/floor100_web/insertScore.do");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes("userId="+userId+"&"+"score="+score);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    result = reader.readLine();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void RestartGame() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //播放音乐和音效
        if(getContext().getSharedPreferences("option",Context.MODE_PRIVATE).getString("music_switch","off").equals("on")){
            mediaPlayer = MediaPlayer.create(this.getContext(),R.raw.bell);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(100)
                    .build();
        } else {
            soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        }
        jumpSound = soundPool.load(this.getContext(),R.raw.jump,0);
        powerSound = soundPool.load(this.getContext(),R.raw.power,0);
        //开始游戏
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
        //初始化对象大小管理器，并初始化游戏对象的实际显示大小
        objectSizeManager = ObjectSizeManager.getInstance();
        objectSizeManager.initGameObjectSize(this);
        //初始化画笔
        paint = new Paint();
        //加载图片资源
        bmpBackground = BitmapFactory.decodeResource(this.getResources(),R.drawable.sky_cloud);
        bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.santa_run);
        bmpPlayerJump = BitmapFactory.decodeResource(this.getResources(), R.drawable.santa_jump);
        bmpPlatform = BitmapFactory.decodeResource(this.getResources(), R.drawable.platform);
        bmpRollingPlatform = BitmapFactory.decodeResource(this.getResources(), R.drawable.rolling_platfrom);
        bmpFloorPlatform = BitmapFactory.decodeResource(this.getResources(), R.drawable.floor_platform);
        bmpSpringPlatform = BitmapFactory.decodeResource(this.getResources(), R.drawable.spring_platform);
        bmpSpringPlatformCompress = BitmapFactory.decodeResource(this.getResources(), R.drawable.spring_platform_compress);
        bmpSpringPlatformUncompress = BitmapFactory.decodeResource(this.getResources(), R.drawable.spring_platform_uncompress);
        //初始化游戏对象
        //背景
        background = new Background(bmpBackground);
        //清除平台列表
        platformList.clear();
        //地面
        floor = new FloorPlatform(0,bmpFloorPlatform);
        platformList.add(0,floor);
        //计算初始平台数，通过屏幕高度和平台厚度、距离计算，然后多生成2个平台以便预加载
        platformNumber = ObjectSizeManager.getInstance().getScreenH()/(objectSizeManager.getPlatformSpace()+objectSizeManager.getPlatformThickness())+2;
        //生成初始平台添加到平台绘制集合
        for(int i=1;i<=platformNumber;i++){
            //需要修改，平台的生成位置由自己来生成
            platformList.add(i,new Platform(i,bmpPlatform));
        }
        updateObjects.clear();
        //添加所有平台到更新列表
        for(int i=0;i<platformList.size();i++){
            updateObjects.add(platformList.get(i));
        }
        //主角
        player = new Player(bmpPlayer,bmpPlayerJump,this);
        //添加主角到更新列表
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
                background.draw(canvas,paint);
                for(Iterator<Platform> platformIterator = platformList.iterator();platformIterator.hasNext();){
                    platformIterator.next().platformDraw(canvas,paint);
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
        //主角跳起超过一定高度时，滚动背景
        if(isRollingBackground){
            background.speed = rollingDistance;
            background.update();
            for(int i=0;i<platformList.size();i++){
                platformList.get(i).y += rollingDistance;
            }
        }
        //步进所有物理模拟并进行碰撞检测
        for (int i=0;i<updateObjects.size();i++){
            updateObjects.get(i).update(this);
        }
        progress.udpate();
        //检查每个平台的状态，移除失效平台并生成新平台
        checkEveryPlatformIsDead();
        //检测碰撞
        checkCollision(player,platformList);
        //检测游戏结束条件
        checkGameOver();
    }

    private void checkEveryPlatformIsDead() {
        if(platformList.getFirst().y>objectSizeManager.getInstance().getScreenH()){
            platformList.remove();
            rank.addRank();
            int typeNumber = rand.nextInt(10);
            Platform newPlatform;
            if(typeNumber>7){
                newPlatform = new Platform(platformList.size()+1,bmpPlatform);
            }
            else if(typeNumber>3 && typeNumber<=7){
                newPlatform = new RollingPlatform(platformList.size()+1,bmpRollingPlatform);
            }
            else if(typeNumber>2 && typeNumber<=3){
                newPlatform = new UDPlatform(platformList.size()+1,bmpPlatform);
            }
            else if(typeNumber>1 && typeNumber<=2){
                newPlatform = new SpringPlatform(platformList.size()+1,bmpSpringPlatform,bmpSpringPlatformCompress,bmpSpringPlatformUncompress);
            }
            else {
                newPlatform = new LRPlatform(platformList.size() + 1, bmpPlatform);
            }
            platformList.add(newPlatform);
            updateObjects.add(newPlatform);
        }
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
                    player.y = ((Platform) platformList.get(i)).y - objectSizeManager.getPlayerHeight();
                    player.isJumping = false;
                    player.isOnPlatform = true;
                    player.platform = (Platform)platformList.get(i);
                    //如果是弹簧踏板，特殊处理
                    if(platformList.get(i).getClass()==SpringPlatform.class){
                        ((SpringPlatform)platformList.get(i)).playerFirstTouchSpring= true;
                    }
                }
            }
        }
    }

    private void checkGameOver() {
        if(player.y>objectSizeManager.getInstance().getScreenH() + objectSizeManager.getPlayerHeight()){
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("score",rank.getCurrentRank());
            message.setData(bundle);
            mHandler.sendMessage(message);
            gameIsRunning = false;
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
