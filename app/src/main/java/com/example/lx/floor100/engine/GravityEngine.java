package com.example.lx.floor100.engine;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lx on 2017-10-24.
 */

public class GravityEngine {

    /*------------常量---------------*/
    static final double G = -9.8 ;

    /*------------变量---------------*/
    List<Entity> entityList ;
    boolean startEngineFlag = false ;

    /*-----------构造函数------------*/

    public GravityEngine(List<Entity> entityList) {
        this.entityList = entityList;
    }

    /*-----------父类或接口的复写方法------------*/

    /*------------方法---------------*/
    public void startEngine(){
        for(Iterator<Entity> entityIterator = entityList.iterator();entityIterator.hasNext();){
            GravityThread gravityThread = new GravityThread(entityIterator.next());
            new Thread(gravityThread).start();
        }
        startEngineFlag = true;
    }

    /*------------内部类------------- */
    class GravityThread implements Runnable{

        public Entity entity;

        public GravityThread(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            while (entity.doGravityFlag){
                long start = System.currentTimeMillis();
                doGravity();
                long end = System.currentTimeMillis();
                try {
                    if (end - start < 50) {
                        Thread.sleep(50 - (end - start));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void doGravity() {
            if(startEngineFlag){
                entity.y = entity.y + 10;
//                if(entity.y>MySurfaceView.screenH - 100){
//                    entity.doGravityFlag = false;
//                }
            }
        }
    }

}
