package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button createButton;
    private Button cancelButton;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        titleText = findViewById(R.id.enterTitle);
        descText = findViewById(R.id.enterDescription);
        timeText = findViewById(R.id.time);
        createButton = findViewById(R.id.submit);
        cancelButton = findViewById(R.id.cancel);

        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference().child("Posts").push();


            createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.setValue(new Post("1:00AM", descText.getText().toString(), "Change later", titleText.getText().toString(), user.getUid()));

                Intent intent = new Intent(CreateEventActivity.this, EventBoardActivity.class);
                Toast.makeText(CreateEventActivity.this, "Event Created", Toast.LENGTH_SHORT).show();
                /*
                intent.putExtra("flag", "create");
                intent.putExtra("title", titleText.getText().toString());
                intent.putExtra("desc", descText.getText().toString());
                */
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
