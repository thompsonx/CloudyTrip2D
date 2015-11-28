package com.tamz2.pan0068.cloudytrip2d;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tamz2.pan0068.cloudytrip2d.views.GameView;

public class GameActivity extends Activity implements IEndGameListener{

    private GameView playground;
    private boolean paused = false;
    private Bundle results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.playground = new GameView(this);
        setContentView(playground);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        this.playground.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        this.playground.pause();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return this.playground.keyEventDown(keyCode);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return this.playground.keyEventUp(keyCode);
    }

    @Override
    public void onGameEnd(Bundle data) {
        this.results = data;
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtras(this.results);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
