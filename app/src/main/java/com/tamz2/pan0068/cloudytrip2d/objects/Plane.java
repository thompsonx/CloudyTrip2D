package com.tamz2.pan0068.cloudytrip2d.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.tamz2.pan0068.cloudytrip2d.views.GameView;

/**
 * Created by Tom on 12. 11. 2015.
 */
public class Plane extends GameObject {

    private static final int DEFUALT_SPEED = 5;
    private int currentSpeed = DEFUALT_SPEED;

    public Plane(int x, int y, GameView view, Bitmap bmp) {
        super(x, y, 0, 0, view, bmp);
    }

    @Override
    protected void update() {
        if (x > getGameWidth() - getBitmap().getWidth() - xSpeed) {
            xSpeed = 0;
        }
        if (x + xSpeed< 0) {
            xSpeed = 0;
        }
        x = x + xSpeed;
        //Log.d("MOVE", "AREA WIDTH = " + getGameWidth() + " BMP WIDTH = " + getBitmap().getWidth() + " X = " + x + " SPEEDX = " + xSpeed );

        if (y > getGameHeight() - getBitmap().getHeight() - ySpeed) {
            ySpeed = 0;
        }
        if (y + ySpeed < 0) {
            ySpeed = 0;
        }
        y = y + ySpeed;
    }

    public void moveUp() {
        ySpeed = -currentSpeed;
    }

    public void moveDown() {
        ySpeed = currentSpeed;
    }

    public void moveRight() {
        xSpeed = currentSpeed;
    }

    public void moveLeft() {
        xSpeed = -currentSpeed;
    }

    public void slowDown() {
        currentSpeed = 2;
    }

    public void resetSpeed() {
        currentSpeed = DEFUALT_SPEED;
    }
}
