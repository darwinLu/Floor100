package com.example.lx.floor100.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.example.lx.floor100.engine.ObjectSizeManager;
import com.example.lx.floor100.view.GameSurfaceView;

/**
 * Created by lx on 2018-08-10.
 */

public class FloorPlatform extends Platform {

    private int floorWidthOnScreen;
    private Bitmap bmpFloorPlatformOnScreen;
    private Bitmap bmpFLoorPlatform;
    int x,y;

    public FloorPlatform(int existPlatformNumber,Bitmap bmpPlatform){
        //super(existPlatformNumber,bmpPlatform);
        //super.bmpPlatform = bmpPlatform;
        this.bmpFLoorPlatform = bmpPlatform;
        calculateObjectSizeOnScreen();
        super.x = 0;
        super.y = ObjectSizeManager.getInstance().getScreenH() - ObjectSizeManager.getInstance().getFloorHeight();
        super.length = ObjectSizeManager.getInstance().getScreenW();
        scaleFloorPlatformBitmap();
        isOnScreen = true;
    }

    private void scaleFloorPlatformBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor,scaleFactor);
        bmpFloorPlatformOnScreen = Bitmap.createBitmap(bmpFLoorPlatform,
                0,0,bmpFLoorPlatform.getWidth(),
                bmpFLoorPlatform.getHeight(),
                matrix,false);
    }

    private void calculateObjectSizeOnScreen() {
        scaleFactor = (float) ObjectSizeManager.getInstance().getScreenW()/(float)bmpFLoorPlatform.getWidth();
        ObjectSizeManager.getInstance().setFloorHeight((int)((float)bmpFLoorPlatform.getHeight()*scaleFactor));
    }

    @Override
    public void platformDraw(Canvas canvas,Paint paint) {
        if(isOnScreen) {
            paint.setColor(Color.RED);
            //Log.d("looping","floor platfrom,x"+this.x+";y"+this.y);
            //canvas.drawRect(super.x,super.y,super.x+bmpFloorPlatformOnScreen.getWidth(),super.y+bmpFloorPlatformOnScreen.getHeight(),paint);
            canvas.drawBitmap(bmpFloorPlatformOnScreen,super.x,super.y,paint);
        }
    }

    @Override
    public void update(GameSurfaceView view) {
        super.update(view);
    }

    @Override
    public void addEffectToPlayer(GameSurfaceView view) {
        super.addEffectToPlayer(view);
    }
}
