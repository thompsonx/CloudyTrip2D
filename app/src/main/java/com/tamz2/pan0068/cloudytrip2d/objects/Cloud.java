package com.tamz2.pan0068.cloudytrip2d.objects;

import android.graphics.Bitmap;
import android.util.Log;

import com.tamz2.pan0068.cloudytrip2d.views.GameView;

/**
 * Created by Tom on 12. 11. 2015.
 */
public class Cloud extends GameObject {

    public static final int DEFAULT_SPEED = -8;

    private boolean active = true;

    public Cloud(int y, GameView view, Bitmap bmp) {
        super(view.getWidth(), y, DEFAULT_SPEED, 0, view, bmp);
    }

    @Override
    protected void update() {
        if (x + xSpeed< -getBitmap().getWidth()) {
            xSpeed = 0;
        }
        x = x + xSpeed;
        Log.d("MOVE", "AREA WIDTH = " + getGameWidth() + " BMP WIDTH = " + getBitmap().getWidth() + " X = " + x + " SPEEDX = " + xSpeed);

    }

    public boolean isActive() {
        return active;
    }

    public void deactive() {
        this.active = false;
    }
}
