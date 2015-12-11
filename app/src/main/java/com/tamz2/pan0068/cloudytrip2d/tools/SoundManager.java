package com.tamz2.pan0068.cloudytrip2d.tools;

import android.content.Context;
import android.media.MediaPlayer;

import com.tamz2.pan0068.cloudytrip2d.R;

/**
 * Created by Tom on 11. 12. 2015.
 */
public class SoundManager {

    private final static SoundManager manager = new SoundManager();

    private SoundManager(){}

    public static SoundManager getInstance() {
        return SoundManager.manager;
    }

    private boolean on = true;

    public void turnOn() {
        this.on = true;
    }

    public void turnOff() {
        this.on = false;
    }

    public void playSound(Context ctx, int resourceId) {
        if (!this.on)
            return;
        MediaPlayer player = MediaPlayer.create(ctx, resourceId);
        player.start();
    }
}
