package com.tamz2.pan0068.cloudytrip2d.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.tamz2.pan0068.cloudytrip2d.MenuActivity;
import com.tamz2.pan0068.cloudytrip2d.R;

/**
 * Created by Tom on 29. 11. 2015.
 */
public class PrepareDialog extends DialogFragment {

    private MenuActivity activity;

    public void setMenuActivity(MenuActivity a) {
        this.activity = a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder prepareScreen = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        prepareScreen.setView(inflater.inflate(R.layout.prepare_screen, null))
                .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startGame();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return prepareScreen.create();
    }
}
