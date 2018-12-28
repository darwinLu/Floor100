package com.example.lx.floor100.entity;

import com.example.lx.floor100.engine.Enity;
import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2018-12-26.
 */

public abstract class AbstractPlatform extends Enity implements IUpdate{

    public abstract void addEffectToPlayer(GameSurfaceView view);

}
