package com.example.lx.floor100.engine;

import android.view.View;

/**
 * Created by lx on 2018-12-07.
 */

public class ObjectSizeManager {

    private static final ObjectSizeManager objectSizeManagerInstance = new ObjectSizeManager();

    private ObjectSizeManager(){
    }

    public static ObjectSizeManager getInstance(){
        return objectSizeManagerInstance;
    }

    private int screenW;
    private int screenH;
    private int playerWidth;
    private int playerHeight;
    private int platformWidth;
    private int platformThickness;
    private int platformSpace;
    private int backgroundWidth;
    private int backgroundHeight;
    private int floorWidth;
    private int floorHeight;

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
    }

    public int getPlatformWidth() {
        return platformWidth;
    }

    public void setPlatformWidth(int platformWidth) {
        this.platformWidth = platformWidth;
    }

    public int getPlatformThickness() {
        return platformThickness;
    }

    public void setPlatformThickness(int platformThickness) {
        this.platformThickness = platformThickness;
    }

    public int getPlatformSpace() {
        return platformSpace;
    }

    public void setPlatformSpace(int platformSpace) {
        this.platformSpace = platformSpace;
    }

    public int getScreenW() {
        return screenW;
    }

    public void setScreenW(int screenW) {
        this.screenW = screenW;
    }

    public int getBackgroundWidth() {
        return backgroundWidth;
    }

    public void setBackgroundWidth(int backgroundWidth) {
        this.backgroundWidth = backgroundWidth;
    }

    public int getBackgroundHeight() {
        return backgroundHeight;
    }

    public void setBackgroundHeight(int backgroundHeight) {
        this.backgroundHeight = backgroundHeight;
    }

    public int getFloorWidth() {
        return floorWidth;
    }

    public void setFloorWidth(int floorWidth) {
        this.floorWidth = floorWidth;
    }

    public int getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(int floorHeight) {
        this.floorHeight = floorHeight;
    }

    public void initGameObjectSize(View gameView){
        this.setScreenW(gameView.getWidth());
        this.setScreenH(gameView.getHeight());
        this.setPlayerWidth(screenW/8);
        this.setPlatformThickness(screenH/20);
        this.setPlatformSpace(screenH/10);
        this.setBackgroundWidth(screenW);
        this.setFloorWidth(screenW);
    }
}
