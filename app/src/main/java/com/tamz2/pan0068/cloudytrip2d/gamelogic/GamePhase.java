package com.tamz2.pan0068.cloudytrip2d.gamelogic;

/**
 * Created by Tom on 11. 12. 2015.
 */
public class GamePhase {

    private int score;
    private long cloudSpawnTime;
    private float speedMultiple;

    public GamePhase(int score, long cloudSpawnTime, float speedMultiple) {
        this.score = score;
        this.cloudSpawnTime = cloudSpawnTime;
        this.speedMultiple = speedMultiple;
    }

    public int getScore() {
        return score;
    }

    public long getCloudSpawnTime() {
        return cloudSpawnTime;
    }

    public float getSpeedMultiple() {
        return speedMultiple;
    }
}
