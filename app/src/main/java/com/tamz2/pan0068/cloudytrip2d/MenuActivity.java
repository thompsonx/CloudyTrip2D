package com.tamz2.pan0068.cloudytrip2d;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tamz2.pan0068.cloudytrip2d.views.EndGameDialog;
import com.tamz2.pan0068.cloudytrip2d.views.PrepareDialog;

import org.w3c.dom.Text;

public class MenuActivity extends Activity implements View.OnClickListener {

    private SharedPreferences storage;
    private EditText name;
    private TextView sName;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Button start = (Button)findViewById(R.id.buttonStart);
        start.setOnClickListener(this);

        this.storage = getSharedPreferences("CLOUDYTRIP", Context.MODE_PRIVATE);

        this.name = (EditText)findViewById(R.id.editPlayer);
        this.sName = (TextView)findViewById(R.id.txtPlayerName);
        this.score = (TextView)findViewById(R.id.txtScore);

        this.name.setText(storage.getString("dname", "plane01"));
        this.sName.setText("Player: " + storage.getString("name", ""));
        this.score.setText("Score: " + storage.getInt("score", 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(resultCode == RESULT_OK))
            return;
        if (requestCode == 1) {
            Bundle result = data.getExtras();
            int score = result.getInt("score");

            EndGameDialog dialog = new EndGameDialog();
            SharedPreferences.Editor editor = storage.edit();
            if (score > storage.getInt("score", 0)) {
                editor.putString("name", name.getText().toString());
                editor.putInt("score", score);
                editor.commit();
                this.sName.setText("Player: " + name.getText().toString());
                this.score.setText("Score: " + score);
                MediaPlayer player = MediaPlayer.create(this, R.raw.bestscore);
                player.start();
            }
            else {
                dialog.setGameOver();
                MediaPlayer player = MediaPlayer.create(this, R.raw.gameover);
                player.start();
            }

            dialog.setScore(score);
            dialog.show(getFragmentManager(), "gameover");
        }
    }

    public void startGame() {
        Intent game = new Intent(this, GameActivity.class);
        SharedPreferences.Editor editor = storage.edit();
        editor.putString("dname", name.getText().toString());
        editor.commit();
        MediaPlayer player = MediaPlayer.create(this, R.raw.ignition);
        player.start();
        startActivityForResult(game, 1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonStart) {
            PrepareDialog dialog = new PrepareDialog();
            dialog.setMenuActivity(this);
            dialog.show(getFragmentManager(), "prepare");
        }
    }
}
