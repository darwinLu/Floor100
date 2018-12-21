package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.MySurfaceView;

/**
 * Created by lx on 2018-08-10.
 */

public class FloorPlatform extends Platform {

    private int floorWidthOnScreen;
    private Bitmap bmpFloorPlatformOnScreen;
    int x,y;

    public FloorPlatform(int existPlatformNumber,Bitmap bmpPlatform){
        super(existPlatformNumber,bmpPlatform);
        //super.bmpPlatform = bmpPlatform;
        calculateObjectSizeOnScreen();
        this.x = 0;
        this.y = ObjectSizeManager.getInstance().getScreenH() - ObjectSizeManager.getInstance().getFloorHeight();
        super.length = ObjectSizeManager.getInstance().getFloorWidth();
        scaleFloorPlatformBitmap();
    }

    private void scaleFloorPlatformBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        bmpFloorPlatformOnScreen = Bitmap.createBitmap(bmpPlatform,
                0,0,bmpPlatform.getWidth(),
                bmpPlatform.getHeight(),
                matrix,false);
    }

    private void calculateObjectSizeOnScreen() {
        scaleFactor = (float) ObjectSizeManager.getInstance().getScreenW()/(float)bmpPlatform.getWidth();
        ObjectSizeManager.getInstance().setFloorHeight((int)((float)bmpPlatform.getHeight()*scaleFactor));
    }

    @Override
    public void platformDraw(Canvas canvas,Paint paint) {
        if(isOnScreen) {
            canvas.drawBitmap(bmpFloorPlatformOnScreen,x,y,paint);
        }
    }

    @Override
    public void update(MySurfaceView view) {
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(MySurfaceView view) {
        super.addEffectToPlayer(view);
    }
}
