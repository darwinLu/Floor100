package com.example.lx.floor100.entity;

import android.graphics.Bitmap;

import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2018-08-08.
 */

public class UDPlatform extends Platform {

    private final int DIRCTION_UP = 0;
    private final int DIRCTION_DOWN = 1;

    private int init_y;
    private int direction = DIRCTION_UP;

    private int moveDistance = 30;
    private int frameDistance;

    public UDPlatform(int existPlatformNumber, Bitmap bmpPlatform) {
        super(existPlatformNumber, bmpPlatform);
        init_y = super.y;
    }

    @Override
    public void update(GameSurfaceView view) {
        int currentY = this.y;
        if(direction == DIRCTION_UP){
            if(frameDistance < moveDistance){
                y = currentY - 1;
                frameDistance += 1;
            }
            else{
                frameDistance = 0;
                direction = DIRCTION_DOWN;
            }
        }
        else if(direction == DIRCTION_DOWN){
            if(frameDistance < moveDistance){
                y = currentY + 1;
                frameDistance += 1;
            }
            else{
                frameDistance = 0;
                direction = DIRCTION_UP;
            }
        }
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(GameSurfaceView view) {
        if(direction == DIRCTION_UP){
            view.player.addY(-1);
        }
        else if(direction == DIRCTION_DOWN){
            view.player.addY(1);
        }
    }
}
