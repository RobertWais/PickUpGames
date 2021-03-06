package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import Model.Post;
import Model.ListItem;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText descText;
    private EditText timeText;
    private Button createButton, addHrBtn, subHrBtn, addMinBtn, subMinBtn;
    private TextView hourText, minText;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String timeOption;
    private Button selectDate;
    private DatePicker picker;
    private String date;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        date = "";
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        hourText = findViewById(R.id.hour);
        minText = findViewById(R.id.minute);
        titleText = findViewById(R.id.enterTitle);
        descText = findViewById(R.id.enterDescription);
        timeText = findViewById(R.id.time);
        createButton = findViewById(R.id.submit);
        addHrBtn = findViewById(R.id.addHour);
        subHrBtn = findViewById(R.id.subHour);
        addMinBtn = findViewById(R.id.addMin);
        subMinBtn = findViewById(R.id.subMin);
        selectDate = findViewById(R.id.select);
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

            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int date1 = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            String min = minute + "";
            if(min.length() == 1)
                min = "0" + min;

            String ampm="AM";
            if(hour>12){
                hour = hour-12;
                ampm="PM";
            }
            if(hour==12){
                ampm="PM";
            }

            if(hour==0){
                hour=12;
            }

            String wholeDate = month+"/"+date1+"/"+year+ " "+hour+":"+min + ampm;

                String time = hourText.getText().toString() + ":" + minText.getText().toString() + " " + timeOption;
                dbRef.setValue(new Post(wholeDate, descText.getText().toString(), date + " " + time, titleText.getText().toString(), user.getUid()));
                Intent intent = new Intent(CreateEventActivity.this, EventBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.date_popup, null);
        confirm = view.findViewById(R.id.ok);
        picker = view.findViewById(R.id.datePicker);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = (picker.getMonth()+1) + "/" + picker.getDayOfMonth() + "/" + picker.getYear();
                dialog.hide();
            }
        });
    }
}
