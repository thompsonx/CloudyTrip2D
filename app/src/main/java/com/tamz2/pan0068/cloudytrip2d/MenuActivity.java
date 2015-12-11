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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.tamz2.pan0068.cloudytrip2d.tools.SoundManager;
import com.tamz2.pan0068.cloudytrip2d.views.EndGameDialog;
import com.tamz2.pan0068.cloudytrip2d.views.PrepareDialog;

import org.w3c.dom.Text;

public class MenuActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SharedPreferences storage;
    private EditText name;
    private TextView sName;
    private TextView score;
    private Switch soundswitch;

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
        this.soundswitch = (Switch)findViewById(R.id.switchSound);

        this.name.setText(storage.getString("dname", "plane01"));
        this.sName.setText("Player: " + storage.getString("name", ""));
        this.score.setText("Score: " + storage.getInt("score", 0));

        this.soundswitch.setOnCheckedChangeListener(this);
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
                SoundManager.getInstance().playSound(this, R.raw.bestscore);
            }
            else {
                dialog.setGameOver();
                SoundManager.getInstance().playSound(this, R.raw.gameover);
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
        SoundManager.getInstance().playSound(this, R.raw.ignition);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SoundManager.getInstance().turnOn();
        }
        else {
            SoundManager.getInstance().turnOff();
        }
    }
}
