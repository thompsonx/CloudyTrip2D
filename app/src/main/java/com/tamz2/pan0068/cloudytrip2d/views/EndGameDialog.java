package com.tamz2.pan0068.cloudytrip2d.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.tamz2.pan0068.cloudytrip2d.R;

/**
 * Created by Tom on 29. 11. 2015.
 */
public class EndGameDialog extends DialogFragment {

    private int score = 0;
    private boolean gameover = false;

    public void setScore(int s) {
        this.score = s;
    }

    public void setGameOver() {this.gameover = true;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder prepareScreen = new AlertDialog.Builder(getActivity());
        if (gameover) {
            prepareScreen.setTitle("Game Over");
        }
        else {
            prepareScreen.setTitle("Best Score");
        }
        prepareScreen.setMessage("Your score is " + score + "!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            }
                });
        return prepareScreen.create();
    }
}
