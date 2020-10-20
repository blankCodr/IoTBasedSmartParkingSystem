package com.example.egg;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class park2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INFO FOR FIRST TIME OPENING THE APP
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean first_start = prefs.getBoolean("first_start", true);

        if (first_start){
            showInfoDialog();
        }

        // INFO BUTTON
        FloatingActionButton info = findViewById(R.id.info_fab);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(park2.this);

                builder.setCancelable(true);
                builder.setIcon(R.drawable.ic_outline_info_24);
                builder.setTitle("Info");
                builder.setMessage("Click the return button to return bitch");

                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        // RETURN BUTTON
        FloatingActionButton fab = findViewById(R.id.return_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_on_click = new Intent(park2.this, MainActivity.class);
                startActivity(back_on_click);
            }
        });
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_outline_info_24)
                .setTitle("Info")
                .setMessage("Click the return button to return bitch!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_start", false);
        editor.apply();
    }
}