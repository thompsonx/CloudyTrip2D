package com.tamz2.pan0068.cloudytrip2d.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.tamz2.pan0068.cloudytrip2d.views.GameView;

/**
 * Created by Tom on 12. 11. 2015.
 */
public abstract class GameObject {
    protected float x;
    protected int y;
    protected int xSpeed;
    protected int ySpeed;

    private GameView view;
    private Bitmap bmp;

    public GameObject(int x, int y, int xSpeed, int ySpeed, GameView view, Bitmap bmp) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.view = view;
        this.bmp = bmp;
    }

    public boolean isCollision(GameObject go) {
        if (x < go.x + go.bmp.getWidth() &&
                x + bmp.getWidth() > go.x &&
                y < go.y + go.bmp.getHeight() &&
                bmp.getHeight() + y > go.y) {
            return true;
        }
        return false;
    }

    public Bitmap getBitmap() {
        return bmp;
    }

    public int getGameWidth() {
        return view.getWidth();
    }

    public int getGameHeight() {
        return view.getHeight();
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    protected abstract void update();

    public void draw(Canvas c) {
        update();
        c.drawBitmap(getBitmap(), x, y, null);
    }
}
