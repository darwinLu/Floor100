package com.example.lx.floor100.engine;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by lx on 2018-12-26.
 */

public abstract class Enity {

    public int x;
    public int y;

    public int originWidth;
    public int originHeight;

    public int onScreenWidth;
    public int onScreenHeight;

    public float scaleFactor;

    public boolean isOnScreen;

    public abstract void draw(Canvas canvas, Paint paint);

}
