package com.example.lx.floor100.engine;

import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-07-02.
 * 此接口用于对游戏场景内的实体进行更新，场景中的实体对象都应实现此接口
 */

public interface IUpdate {

    void update(MySurfaceView view);
}
