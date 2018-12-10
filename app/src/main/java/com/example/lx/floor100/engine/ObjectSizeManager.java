package com.example.lx.floor100.engine;

/**
 * Created by lx on 2018-12-07.
 */

public class ObjectSizeManager {

    private static ObjectSizeManager objectSizeManager;

    public int getScreenW() {
        return screenW;
    }

    public void setScreenW(int screenW) {
        this.screenW = screenW;
    }

    public int getScreenH() {
        return screenH;
    }

    public void setScreenH(int screenH) {
        this.screenH = screenH;
    }

    public int screenW;
    public int screenH;
    public int playerWidth;
    public int playerHeight;
    public int platformWidth;
    public int platformHeight;

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

    public int getPlatformHeight() {
        return platformHeight;
    }

    public void setPlatformHeight(int platformHeight) {
        this.platformHeight = platformHeight;
    }

    private ObjectSizeManager(){
    }

    public static ObjectSizeManager getObjectSizeManager(){
        if(objectSizeManager != null){
            objectSizeManager = new ObjectSizeManager();
            return objectSizeManager;
        }
        else {
            return objectSizeManager;
        }
    }

}
