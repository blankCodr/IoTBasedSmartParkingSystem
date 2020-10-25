package com.example.egg;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class park2 extends AppCompatActivity {

    private static final String API_URL= "http://192.168.1.3/practice/parking_system/android_api/api.php";
    private Handler mHandler = new Handler();

    Dialog custom_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park2);

        // TOOLBAR UP TOP
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INFO POPUP
        custom_dialog = new Dialog(this);

        // INFO FOR FIRST TIME OPENING THE APP
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean first_start = prefs.getBoolean("first_start", true);

        if (first_start){
            showInfoDialog();
        }

        // RETURN BUTTON
        FloatingActionButton fab = findViewById(R.id.return_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_on_click = new Intent(park2.this, MainActivity.class);
                startActivity(back_on_click);
            }
        });

        // RUN THIS LINE OF CODES TO UPDATE
        this.mHandler = new Handler();
        this.mHandler.postDelayed(mRunnable, 2500);
    }

    // KEEP REFRESHING THE ACTIVITY
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            // FETCH DATA FROM DATABASE
            fetch_data();

            park2.this.mHandler.postDelayed(mRunnable, 2500);
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
                                String slot_4 = parking_data_object.getString("slot_4");

                                if(id == 2){
                                    TextView park_space_1 = findViewById(R.id.park_2_space_1);
                                    TextView park_space_2 = findViewById(R.id.park_2_space_2);
                                    TextView park_space_3 = findViewById(R.id.park_2_space_3);
                                    TextView park_space_4 = findViewById(R.id.park_2_space_4);
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
                                    if(slot_4.equals("AVAILABLE")){
                                        park_space_4.setBackgroundResource(R.drawable.park_state_available);
                                    }
                                    else{
                                        park_space_4.setBackgroundResource(R.drawable.park_state_unavailable);
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
                Toast.makeText(park2.this, "Error Fetching Data!" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(string_request);
    }
}