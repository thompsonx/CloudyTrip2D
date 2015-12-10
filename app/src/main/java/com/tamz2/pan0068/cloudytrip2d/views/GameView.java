package com.tamz2.pan0068.cloudytrip2d.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tamz2.pan0068.cloudytrip2d.IEndGameListener;
import com.tamz2.pan0068.cloudytrip2d.R;
import com.tamz2.pan0068.cloudytrip2d.objects.Cloud;
import com.tamz2.pan0068.cloudytrip2d.objects.GameObject;
import com.tamz2.pan0068.cloudytrip2d.objects.PauseButton;
import com.tamz2.pan0068.cloudytrip2d.objects.Plane;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Tom on 4. 11. 2015.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private SurfaceHolder holder;
    private Context context;
    private GameThread thread;
    private boolean initThread = false;
    private boolean firstresume = true;
    private boolean firstSurface = true;

    private Plane planeSprite;
    private Bitmap cloudBitmap;
    private List<GameObject> obstacles;
    private Bitmap lifeBitmap;
    private Bitmap pauseBitmap;
    private Bitmap playBitmap;
    private PauseButton pauseButton;
    private boolean pausePressed = false;

    private int lives = 3;
    private int score = 0;
    private long spawnDelay = 0;
    private final int cloudSpawnTime = 2500;
    private final int spawnDecrease = 500;
    private final int decreaseTime = 15000;
    private final int maxSpeed = Cloud.DEFAULT_SPEED * 3;

    private long speedResetTime = Long.MAX_VALUE;
    private boolean speedReseted = false;

    private SensorManager sensorManager;
    private Sensor acclerometer;
    private boolean firstrun = true;
    private float defualtStateX;
    private final float tolarenceX = 1.3f;

    private IEndGameListener listener;

    Vibrator vibrator;

    public GameView(Context context) {
        super(context);
        this.context = context;
        this.listener = (IEndGameListener) context;
        this.holder = this.getHolder();
        this.holder.addCallback(this);
        this.obstacles = new ArrayList<>();

        // SPIRTES AND GAME OBJECTS
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.plane);
        this.planeSprite = new Plane(0, 10, this, bmp);
        this.cloudBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
        this.lifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        this.playBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play);
        this.pauseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        this.pauseButton = new PauseButton(pauseBitmap, playBitmap);

        // SENSORS AND CONTROLS
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.acclerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.vibrator = (Vibrator)this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void resume() {
        // RESUME ONLY IF GAME HAS JUST BEEN CREATED OR WHEN PAUSE BUTTON IS PRESSED
        if (!(firstresume || pausePressed))
            return;
        if (firstresume) {
            this.firstresume = false;
        }
        if (!initThread) {
            this.thread = new GameThread(holder, this.context, this);
            sensorManager.registerListener(this, acclerometer, SensorManager.SENSOR_DELAY_GAME);
            initThread = true;
            if (this.pausePressed) {
                pausePressed = false;
                if (this.spawnDelay > 0)
                    this.thread.setCloudSpawnTime(System.currentTimeMillis() + this.spawnDelay);
                this.thread.start();
            }
        }
    }

    public void pauseGame() {
        this.pausePressed = false;
        this.pause();
    }

    private void pause() {
        if (!this.thread.isRunning())
            return;
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        this.spawnDelay = thread.getCloudSpawnTime() - System.currentTimeMillis();
        this.initThread = false;
        sensorManager.unregisterListener(this);

        if (!pausePressed) {
            this.pauseButton.pause();
        }
        reDraw();
    }

    private void reDraw() {
        Canvas c = holder.lockCanvas();
        if (c != null) {
            this.draw(c);
            holder.unlockCanvasAndPost(c);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.resume();

        // WHEN SURFACE IS CREATED FOR THE FIRST TIME
        if (firstSurface) {
            if (this.spawnDelay > 0)
                this.thread.setCloudSpawnTime(System.currentTimeMillis() + this.spawnDelay);
            pauseButton.setX(getWidth() - pauseBitmap.getWidth()*2);
            pauseButton.setY(10);
            this.thread.start();
            this.firstSurface = false;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Canvas c = holder.lockCanvas(null);
        draw(c);
        holder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.pauseGame();
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.BLUE);
        planeSprite.draw(canvas);
        for (GameObject go: obstacles) {
            go.draw(canvas);
        }

        // SCORE TEXT
        Paint scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setColor(Color.WHITE);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextSize(35);
        canvas.drawText(String.format("Score: %d", score),
                canvas.getWidth()/4,
                45,
                scorePaint);

        // LIVES
        canvas.drawText("Lives: ", canvas.getWidth()/2, 45, scorePaint);
        int lifeX = canvas.getWidth()/2 + 100;
        for (int i = 0; i < lives; i++) {
            canvas.drawBitmap(this.lifeBitmap, lifeX, 15, null);
            lifeX += 40;
        }

        // PAUSE BUTTON
        this.pauseButton.draw(canvas);
    }

    public synchronized void checkLiveObjects() {
        Queue<GameObject> objectsToDelete = new ArrayDeque<>();
        for (GameObject go: obstacles) {
            if (go.isCollision(planeSprite)) {
                if (go instanceof Cloud) {
                    Cloud cloud = (Cloud) go;
                    if (cloud.isActive()) {
                        planeSprite.slowDown();
                        speedResetTime = System.currentTimeMillis() + 4000;
                        vibrator.vibrate(500);
                        lives--;
                        if (lives > 0) {
                            MediaPlayer player = MediaPlayer.create(this.context, R.raw.cloudcollision);
                            player.start();
                        }
                        cloud.deactive();
                        if (score > 0)
                            score--;
                    }
                }
            }
            if (go.getxSpeed() == 0) {
                objectsToDelete.add(go);
                //Log.d("DELETE CLOUD", "DELETED");
            }
        }
        for (GameObject go: objectsToDelete) {
            obstacles.remove(go);
            if (go instanceof Cloud) {
                Cloud c = (Cloud)go;
                if (c.isActive())
                    score++;
            }
        }

        if (this.lives <= 0) {
            endGame();
        }

    }

    public synchronized void spawnCloud() {
        Random rand = new Random();
        int y = rand.nextInt(getHeight() - cloudBitmap.getHeight());
        Cloud c = new Cloud(y, this, cloudBitmap);
        obstacles.add(c);
    }

    public synchronized void resetSpeed() {
        if (speedResetTime < System.currentTimeMillis() && !speedReseted) {
            planeSprite.resetSpeed();
            speedReseted = false;
        }
    }

    private void endGame() {
        Bundle data = new Bundle();
        data.putInt("score", this.score);
        this.listener.onGameEnd(data);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (firstrun) {
            defualtStateX = event.values[0];
            firstrun = false;
        }
        else {
            float x = event.values[0];

            if (x > (defualtStateX + tolarenceX)) {
                    planeSprite.moveDown();
            }
            else if(x < (defualtStateX - tolarenceX)) {
                    planeSprite.moveUp();
            }
            else {
                    planeSprite.setySpeed(0);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean keyEventDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            planeSprite.moveRight();
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            planeSprite.moveLeft();
        }
        else {
            return false;
        }
        return  true;
    }

    public boolean keyEventUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            planeSprite.setxSpeed(0);
        }
        else {
            return false;
        }
        return  true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return false;
        if (this.pauseButton.isPressed(event.getX(), event.getY())) {
            this.pausePressed = true;
            if (pauseButton.isPause()) {
                resume();
            }
            else {
                pause();
            }
            return true;
        }
        return false;
    }
}
