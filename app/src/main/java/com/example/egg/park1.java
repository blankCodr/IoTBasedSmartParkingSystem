package com.example.egg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogRecord;

public class park1 extends AppCompatActivity {

    private static final String API_URL= "http://192.168.1.3/practice/android_api/api.php";
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park1);

        // TOOLBAR UP TOP
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // OPEN THE INFO DIALOG FOR FIRST TIME OPENING THE APP
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(park1.this);

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
        FloatingActionButton back = findViewById(R.id.return_fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_on_click = new Intent(park1.this, MainActivity.class);
                startActivity(back_on_click);
            }
        });

        // RUN THIS LINE OF CODES TO UPDATE
        this.mHandler = new Handler();
        this.mHandler.postDelayed(mRunnable, 5000);
    }

    // KEEP REFRESHING THE ACTIVITY
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            // FETCH DATA FROM DATABASE
            fetch_data();

            park1.this.mHandler.postDelayed(mRunnable, 5000);
        }
    };

    // STOP REFRESHING WHEN THE BACK BUTTON IS CALLED
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        finish();
    }

    // INFO BOX
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

    // FETCH DATA
    private void fetch_data() {
        StringRequest string_request = new StringRequest(Request.Method.GET, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray parking_data = new JSONArray(response);
                            for (int i=0; i<parking_data.length(); i++){
                                JSONObject parking_data_object = parking_data.getJSONObject(i);

                                int id = parking_data_object.getInt("id");
                                String establishment_id = parking_data_object.getString(
                                        "establishment_id");
                                String slot_1 = parking_data_object.getString("slot_1");
                                String slot_2 = parking_data_object.getString("slot_2");
                                String slot_3 = parking_data_object.getString("slot_3");

                                if(id == 1){
                                    TextView park_space_1 = findViewById(R.id.park_space_1);
                                    TextView park_space_2 = findViewById(R.id.park_space_2);
                                    TextView park_space_3 = findViewById(R.id.park_space_3);
                                    if(slot_1.equals("AVAILABLE")){
                                        park_space_1.setBackgroundResource(R.drawable.park_state_available);
                                    }
                                    else{
                                        park_space_1.setBackgroundResource(R.drawable.park_state_unavailable);
                                    }
                                    if(slot_2.equals("AVAILABLE")){
                                        park_space_2.setBackgroundResource(R.drawable.park_state_available);
                                    }
                                    else{
                                        park_space_2.setBackgroundResource(R.drawable.park_state_unavailable);
                                    }
                                    if(slot_3.equals("AVAILABLE")){
                                        park_space_3.setBackgroundResource(R.drawable.park_state_available);
                                    }
                                    else{
                                        park_space_3.setBackgroundResource(R.drawable.park_state_unavailable);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(park1.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(string_request);
    }
}