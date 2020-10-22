package com.example.egg;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogRecord;

public class park1 extends AppCompatActivity {

    private static final String API_URL= "http://192.168.1.3/practice/parking_system/android_api/api.php";
    private Handler mHandler = new Handler();

    Dialog custom_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park1);

        // TOOLBAR UP TOP
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FOR CUSTOM POPUP
        custom_dialog = new Dialog(this);


        // OPEN THE INFO DIALOG FOR FIRST TIME OPENING THE APP
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean first_start = prefs.getBoolean("first_start", true);

        if (first_start){
            showInfoDialog();
        }

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


    // CUSTOM POP-UP WHEN FIRST OPENING THE APP
    private void showInfoDialog() {
        Button button_ok;
        custom_dialog.setContentView(R.layout.custom_popup);
        button_ok = custom_dialog.findViewById(R.id.ok_button);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog.cancel();
            }
        });
        custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_dialog.show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_start", false);
        editor.apply();
    }

    // SHOW CUSTOM POPUP WHEN BUTTON IS CLICK
    public void show_custom_popup(View view) {
        Button button_ok;
        custom_dialog.setContentView(R.layout.custom_popup);
        button_ok = custom_dialog.findViewById(R.id.ok_button);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog.cancel();
            }
        });
        custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        custom_dialog.show();
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
                }, 
                // IF THERE IS AN ERROR SHOW A TOAST ERROR
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(park1.this, "Error Fetching Data!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(string_request);
    }

}