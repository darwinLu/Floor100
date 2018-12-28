package com.example.lx.floor100.entity;

import android.graphics.Bitmap;

import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2018-08-08.
 */

public class LRPlatform extends Platform {

    private final int DIRCTION_LEFT = 0;
    private final int DIRCTION_RIGHT = 1;

    private int init_x;
    private int direction = DIRCTION_RIGHT;


    public LRPlatform(int existPlatformNumber, Bitmap bmpPlatform) {
        super(existPlatformNumber, bmpPlatform);
        init_x = super.x;
    }

    @Override
    public void update(GameSurfaceView view) {
        if(direction == DIRCTION_RIGHT){
            x = x + 1;
            if(x > init_x + 80){
                x = init_x + 80;
                direction = DIRCTION_LEFT;
            }
        }
        else if(direction == DIRCTION_LEFT){
            x = x - 1;
            if(x < init_x - 80){
                x = init_x - 80;
                direction = DIRCTION_RIGHT;
            }
        }
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(GameSurfaceView view) {
        if(direction == DIRCTION_RIGHT){
                view.player.addX(1);
        }
        else if(direction == DIRCTION_LEFT){
            view.player.addX(-1);
        }
    }
}
