package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.lx.floor100.engine.IUpdate;
import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2018-08-10.
 */

public class FloorPlatform extends AbstractPlatform implements IUpdate {

    private Bitmap floorPlatformBitmapOnScreen;
    private Bitmap floorPlatformBitmap;

    public FloorPlatform(int existPlatformNumber,Bitmap bmpPlatform){
        this.floorPlatformBitmap = bmpPlatform;
        calculateObjectSizeOnScreen();
        this.x = 0;
        this.y = ObjectSizeManager.getInstance().getScreenH() - ObjectSizeManager.getInstance().getFloorHeight();
        //super.length = ObjectSizeManager.getInstance().getScreenW();
        scaleBitmap();
        isOnScreen = true;
    }

    private void calculateObjectSizeOnScreen() {
        originWidth = floorPlatformBitmap.getWidth();
        originHeight = floorPlatformBitmap.getHeight();
        onScreenWidth = ObjectSizeManager.getInstance().getScreenW();
        scaleFactor = (float) onScreenWidth/(float)floorPlatformBitmap.getWidth();
        onScreenHeight = (int)((float)floorPlatformBitmap.getHeight()*scaleFactor);
        ObjectSizeManager.getInstance().setFloorHeight(onScreenHeight);
    }

    private void scaleBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        floorPlatformBitmapOnScreen = Bitmap.createBitmap(floorPlatformBitmap,
                0,0,
                originWidth,originHeight,
                matrix,false);
    }

    @Override
    public void update(GameSurfaceView view) {
        if(y>ObjectSizeManager.getInstance().getScreenH()){
            isOnScreen = false;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(isOnScreen) {
            canvas.drawBitmap(floorPlatformBitmapOnScreen,x,y,paint);
        }
    }


    @Override
    public void addEffectToPlayer(GameSurfaceView view) {
        return;
    }
}
