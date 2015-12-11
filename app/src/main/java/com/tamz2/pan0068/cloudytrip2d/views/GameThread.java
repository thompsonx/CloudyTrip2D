package com.tamz2.pan0068.cloudytrip2d.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
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
    private long cloudSpawnDelay;

    public GameThread(SurfaceHolder holder, Context context, GameView view) {
        this.holder = holder;
        this.running = true;
        this.context = context;
        this.view = view;
        this.cloudSpawnTime = -1;
        this.cloudSpawnDelay = 2500;
    }

    public long getCloudSpawnDelay() {
        return cloudSpawnDelay;
    }
    public void setCloudSpawnDelay(long cloudSpawnDelay) {
        this.cloudSpawnDelay = cloudSpawnDelay;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean isRunning() {return running;}
    public void setCloudSpawnTime(long time) { this.cloudSpawnTime = time; }
    public long getCloudSpawnTime() { return this.cloudSpawnTime; }

    @Override
    public void run() {
        super.run();

        long ticks = 1000 / FPS;
        long startime;
        long sleeptime;

        if (cloudSpawnTime == -1)
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
            }
            catch (Exception e) {}
        }
    }

    private void spawnClouds() {
        if (cloudSpawnTime < System.currentTimeMillis()) {
            this.view.spawnCloud();
            cloudSpawnTime = System.currentTimeMillis() + this.cloudSpawnDelay;
        }
    }

    private void speedReset() {
        this.view.resetSpeed();
    }
}
