package com.tamz2.pan0068.cloudytrip2d.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tamz2.pan0068.cloudytrip2d.views.GameView;

/**
 * Created by Tom on 10. 12. 2015.
 */
public class PauseButton {

    private int x;
    private int y;
    private Bitmap pause;
    private Bitmap play;
    private boolean paused;

    public PauseButton(Bitmap pause, Bitmap play) {
        this.pause = pause;
        this.play = play;
        this.paused = false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPause() {
        return !paused;
    }

    public void pause() {
        this.paused = true;
    }

    public boolean isPressed(float touchX, float touchY) {
        if (touchX > x && touchX < x + pause.getWidth() && touchY > y && touchY < y + pause.getHeight()) {
            this.paused = !paused;
            return true;
        }
        return false;
    }

    public void draw(Canvas c) {
        if (!paused) {
            c.drawBitmap(pause, x, y, null);
        }
        else {
            c.drawBitmap(play, x, y, null);
        }
    }
}
