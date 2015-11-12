package com.tamz2.pan0068.cloudytrip2d.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Tom on 5. 11. 2015.
 */
public class GameThread extends Thread {

    private static long FPS = 30;

    private SurfaceHolder holder;
    private GameView view;
    private boolean running;
    private Context context;

    private long cloudSpawnTime;
    private boolean cloudSpawned = false;


    public GameThread(SurfaceHolder holder, Context context, GameView view) {
        this.holder = holder;
        this.running = true;
        this.context = context;
        this.view = view;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        super.run();

        long ticks = 1000 / FPS;
        long startime;
        long sleeptime;

        cloudSpawnTime = System.currentTimeMillis();

        while (running) {
            startime = System.currentTimeMillis();

            this.view.checkLiveObjects();
            spawnClouds();
            speedReset();

            Canvas c = holder.lockCanvas();
            if (c != null) {
                view.draw(c);
                holder.unlockCanvasAndPost(c);
            }
            sleeptime = ticks - (System.currentTimeMillis() - startime);
            try {
                if (sleeptime > 0) {
                    this.sleep(sleeptime);
                }
                else {
                    this.sleep(FPS);
                }
            }
            catch (Exception e) {}
        }
    }

    private void spawnClouds() {
        if (cloudSpawnTime < System.currentTimeMillis() && !cloudSpawned) {
            this.view.spawnCloud();
            cloudSpawned = false;
            cloudSpawnTime = System.currentTimeMillis() + 2500;
        }
    }

    private void speedReset() {
        this.view.resetSpeed();
    }
}
