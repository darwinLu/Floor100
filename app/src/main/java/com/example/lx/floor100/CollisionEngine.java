package com.example.lx.floor100;

/**
 * Created by lx on 2017-10-24.
 */

public class CollisionEngine {
    /*------------常量---------------*/


    /*------------变量---------------*/
    Entity sourceEntity;
    Entity targetEntity;
    boolean engineRunningFlag = true;

    /*-----------构造函数------------*/

    public CollisionEngine(Entity sourceEntity,Entity targetEntity) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;//应传入地板以下的一个Entity，宽度为地板宽
    }
    /*-----------父类或接口的复写方法------------*/


    /*------------方法---------------*/
    public void startEngine(){
        new Thread(new CollisionThread()).start();
    }


    /*------------内部类------------- */
    class CollisionThread implements Runnable{

        public CollisionThread() {

        }

        @Override
        public void run() {
            while (engineRunningFlag){
                long start = System.currentTimeMillis();
                if(checkCollision()){
                    //how to callback?
//                    MySurfaceView.onCheckCollision(CollisionEngine.this,sourceEntity);
                };
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

        private boolean checkCollision() {
            if(sourceEntity.y > targetEntity.y - 100){
                return true;
            }
            else {
                return false;
            }
        }
    }

}
