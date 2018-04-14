package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Adapter.EventAdapter;
import Model.Post;

public class DetailsActivity extends AppCompatActivity {
private String title, description;
private TextView descIn, titleIN, timeIN;
private Bundle passedThru;
private Button attend, decline;
private FirebaseUser user;
private FirebaseAuth auth;
private FirebaseDatabase db;
private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        passedThru = getIntent().getExtras();
        db = FirebaseDatabase.getInstance();

        dbRef = db.getReference().child("Comments").child(passedThru.getString("CommentsID"));

        if(dbRef == null){
            Toast.makeText(DetailsActivity.this,"We null", Toast.LENGTH_SHORT).show();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //Toast.makeText(DetailsActivity.this,"User "+user.getUid(), Toast.LENGTH_LONG).show();




        attend = (Button) findViewById(R.id.attendingBtn);
        decline = (Button) findViewById(R.id.attendingBtn);
        descIn = (TextView) findViewById(R.id.descInput);
        titleIN = (TextView) findViewById(R.id.titleInput);
        timeIN = (TextView) findViewById(R.id.timeInput);

        timeIN.setText(passedThru.getString("Time"));
        descIn.setText(passedThru.getString("Description"));
        titleIN.setText(passedThru.getString("Title"));


        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if already attending
                //if not add to attending
                //if so display toast


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (dbRef != null) {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Integer> list = new ArrayList<>();
                    //GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<>();
                    List messages = (List) snapshot.getValue();
                    for (int i = 0; i < messages.size(); i++) {
                        Toast.makeText(DetailsActivity.this, "Message " + messages.get(i), Toast.LENGTH_SHORT).show();
                    }

                }

                // onCancelled...

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    }

