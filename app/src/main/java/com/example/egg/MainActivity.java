package com.example.egg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;  // Instantiate spinner object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);

        // LIST OF PARKING LOTS
        List<String> parking_lots = new ArrayList<>();
        parking_lots.add(0, "Choose Parking Lot");
        parking_lots.add("Bernabe Shopping Mall");
        parking_lots.add("Dutdutan Building");

        // STYLE AND POPULATE THE SPINNER
        ArrayAdapter<String> data_adapter;
        data_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parking_lots);

        // DROPDOWN STYLE
        data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // ATTACHING data_adapter to spinner
        spinner.setAdapter(data_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Choose Parking Lot")) {
                    // If null is selected do nothing
                } else {
                    //selecting either of the two parking lot
                    String item = parent.getItemAtPosition(position).toString();

                    //Show Selected Spinner Item using Toast
                    //Toast.makeText(parent.getContext(), "Selected Parking Lot: " + item, Toast.LENGTH_SHORT).show();

                    // ANYTHING YOU WANT TO ADD

                    // BERNABE SHOPPING MALL
                    if (parent.getItemAtPosition(position).equals("Bernabe Shopping Mall")) {
                        Intent intent = new Intent(MainActivity.this, park1.class);
                        startActivity(intent);
                    }

                    // DUTDUTAN BUILDING
                    if (parent.getItemAtPosition(position).equals("Dutdutan Building")) {
                        Intent intent = new Intent(MainActivity.this, park2.class);
                        startActivity(intent);


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent exit_app = new Intent(Intent.ACTION_MAIN);
        exit_app.addCategory(Intent.CATEGORY_HOME);
        exit_app.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit_app);
        finish();
    }
}