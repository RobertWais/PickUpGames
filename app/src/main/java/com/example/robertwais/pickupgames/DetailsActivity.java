package com.example.robertwais.pickupgames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
private String title, description;
private TextView descIn, titleIN;
private Bundle passedThru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        passedThru = getIntent().getExtras();

        descIn = (TextView) findViewById(R.id.descInput);
        titleIN = (TextView) findViewById(R.id.titleInput);

        descIn.setText(passedThru.getString("Description"));
        titleIN.setText(passedThru.getString("Title"));
    }
}
