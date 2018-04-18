package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Model.ListItem;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText descText;
    private Button createButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleText = findViewById(R.id.enterTitle);
        descText = findViewById(R.id.enterDescription);
        createButton = findViewById(R.id.submit);
        cancelButton = findViewById(R.id.cancel);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateEventActivity.this, EventBoardActivity.class);
                intent.putExtra("flag", "create");
                intent.putExtra("title", titleText.getText().toString());
                intent.putExtra("desc", descText.getText().toString());
                startActivity(intent);
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
