package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Post;
import Model.ListItem;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText descText;
    private EditText timeText;
    private Button createButton, cancelButton, addHrBtn, subHrBtn, addMinBtn, subMinBtn;
    private TextView hourText, minText;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String timeOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        hourText = findViewById(R.id.hour);
        minText = findViewById(R.id.minute);
        titleText = findViewById(R.id.enterTitle);
        descText = findViewById(R.id.enterDescription);
        timeText = findViewById(R.id.time);
        createButton = findViewById(R.id.submit);
        cancelButton = findViewById(R.id.cancel);
        addHrBtn = findViewById(R.id.addHour);
        subHrBtn = findViewById(R.id.subHour);
        addMinBtn = findViewById(R.id.addMin);
        subMinBtn = findViewById(R.id.subMin);

        timeOption = "";

        spinner = findViewById(R.id.option);
        adapter = ArrayAdapter.createFromResource(this, R.array.time_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timeOption = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr = Integer.parseInt(hourText.getText().toString());
                if(hr == 12) hr = 1;
                else hr++;
                hourText.setText(hr + "");
            }
        });

        subHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hr = Integer.parseInt(hourText.getText().toString());
                if(hr == 1) hr = 12;
                else hr--;
                hourText.setText(hr + "");
            }
        });

        addMinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = Integer.parseInt(minText.getText().toString());
                if(min == 59) min = 0;
                else min++;
                String minString = min + "";
                if(minString.length() == 1) minString = 0 + minString;
                minText.setText(minString);
            }
        });

        subMinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = Integer.parseInt(minText.getText().toString());
                if(min == 0) min = 59;
                else min--;
                String minString = min + "";
                if(minString.length() == 1) minString = 0 + minString;
                minText.setText(minString);
            }
        });

        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference().child("Posts").push();


        createButton.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                String time = hourText.getText().toString() + ":" + minText.getText().toString() + " " + timeOption;

                dbRef.setValue(new Post("1:00AM", descText.getText().toString(), time, titleText.getText().toString(), user.getUid()));

                Intent intent = new Intent(CreateEventActivity.this, EventBoardActivity.class);
                Toast.makeText(CreateEventActivity.this, "Event Created", Toast.LENGTH_SHORT).show();
                /*
                intent.putExtra("flag", "create");
                intent.putExtra("title", titleText.getText().toString());
                intent.putExtra("desc", descText.getText().toString());
                */
                startActivity(intent);
                finish();
            }
        });



        //MARK: Should change listener to this
        /*
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        */

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEventActivity.this, EventBoardActivity.class);
                intent.putExtra("flag", "cancel");
                startActivity(intent);
                finish();
            }
        });
        
    }


}
