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

public class park1 extends AppCompatActivity {

    public String slot_1, slot_2, slot_3;
    public int id;

    private static final String API_URL= "http://192.168.1.10/practice/parking_system/android_api/api.php";
    private Handler mHandler = new Handler();

    Dialog custom_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park1);

        // TEXT VIEW AS BUTTON
        TextView txt_view_1 = findViewById(R.id.park_1_space_1);
        TextView txt_view_2 = findViewById(R.id.park_1_space_2);
        TextView txt_view_3 = findViewById(R.id.park_1_space_3);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        FloatingActionButton back = findViewById(R.id.return_fab);

        this.mHandler = new Handler();

        // TOOLBAR UP TOP
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // INFO POPUP
        custom_dialog = new Dialog(this);

        // OPEN THE INFO DIALOG FOR FIRST TIME OPENING THE APP
        boolean first_start = prefs.getBoolean("first_start", true);

        if (first_start){
            show_custom_popup(null);
        }

        // RETURN BUTTON
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_on_click = new Intent(park1.this, MainActivity.class);
                startActivity(back_on_click);
            }
        });

        // RUN THIS LINE OF CODES TO UPDATE
        this.mHandler.postDelayed(mRunnable, 500);

        // PARKING SPACE 1
        txt_view_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_available(view, slot_1);
            }
        });

        // PARKING SPACE 2
        txt_view_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_available(view, slot_2);
            }
        });

        // PARKING SPACE 3
        txt_view_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_available(view, slot_3);
            }
        });
    }

    // KEEP REFRESHING THE ACTIVITY
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            // FETCH DATA FROM DATABASE
            fetch_data();

            park1.this.mHandler.postDelayed(mRunnable, 500);
        }
    };

    // STOP REFRESHING WHEN THE BACK BUTTON IS CALLED
    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        finish();
    }

    // CUSTOM POP-UP
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

        // FOR FIRST TIME OPENING THE APP
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_start", false);
        editor.apply();
    }

    // METHOD FOR DETERMINING IF THEY CAN RESERVE THE SPACE
    public void is_available(View view, String slot) {
        Button cancel_button, proceed_button, ok_button_1;

        if (slot.equals("AVAILABLE")) {
            custom_dialog.setContentView(R.layout.custom_popup_reserve_available);
            cancel_button = custom_dialog.findViewById(R.id.cancel_button);
            proceed_button = custom_dialog.findViewById(R.id.proceed_button);

            // PROCEED
            proceed_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(park1.this, "Its working!", Toast.LENGTH_SHORT).show();
                }
            });

            // CANCEL
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custom_dialog.cancel();
                }
            });
        }
        else {
            custom_dialog.setContentView(R.layout.custom_popup_reserve_unavailable);
            ok_button_1 = custom_dialog.findViewById(R.id.ok_button_1);

            // OK BUTTON FOR UNAVAILABLE
            ok_button_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custom_dialog.cancel();
                }
            });
        }
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

                                // Initiate Text view
                                TextView park_space_1 = findViewById(R.id.park_1_space_1);
                                TextView park_space_2 = findViewById(R.id.park_1_space_2);
                                TextView park_space_3 = findViewById(R.id.park_1_space_3);

                                // IF ID = 1 THEN DO THIS
                                id = parking_data_object.getInt("id");

                                if(id == 1){
                                    slot_1 = parking_data_object.getString("slot_1");
                                    slot_2 = parking_data_object.getString("slot_2");
                                    slot_3 = parking_data_object.getString("slot_3");

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
                        }
                        catch (JSONException e) {
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