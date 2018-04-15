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
import java.util.HashSet;
import java.util.List;

import Adapter.EventAdapter;
import Model.Post;

public class DetailsActivity extends AppCompatActivity {
private String title, description,keyAttending;
private TextView descIn, titleIN, timeIN;
private Bundle passedThru;
private Button attend, decline;
private FirebaseUser user;
private FirebaseAuth auth;
private FirebaseDatabase db;
private DatabaseReference dbRef, refPost,refComments,refAttending;
private HashSet<String> attendingSet = new HashSet<>();
private List localList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        passedThru = getIntent().getExtras();
        db = FirebaseDatabase.getInstance();

        dbRef = db.getReference().child("Comments").child(passedThru.getString("CommentsID")).child("comments");
        refPost = db.getReference().child("Comments").child(passedThru.getString("CommentsID"));
        refComments = refPost.child("comments");
        refAttending = refPost.child("attending");

        if(dbRef == null){
            Toast.makeText(DetailsActivity.this,"We null", Toast.LENGTH_SHORT).show();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //Toast.makeText(DetailsActivity.this,"User "+user.getUid(), Toast.LENGTH_LONG).show();




        attend = (Button) findViewById(R.id.attendingBtn);
        decline = (Button) findViewById(R.id.declineBtn);
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
                if (attendingSet.contains(user.getUid())){
                    Toast.makeText(DetailsActivity.this, "Already Attending ", Toast.LENGTH_SHORT).show();
                } else{
                    String id = Integer.toString(attendingSet.size());

                    refAttending.child(id).setValue((String)user.getUid());
                    Toast.makeText(DetailsActivity.this, "Not attending yet", Toast.LENGTH_SHORT).show();
                }
                //if not add to attending
                //if so display toast


            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attendingSet.contains(user.getUid())){
                    //Remove from list
                    int removeIndex=0;
                    for(int i=0;i<localList.size();i++){
                        if(localList.get(i).equals(user.getUid())){
                            Toast.makeText(DetailsActivity.this, "Its in there", Toast.LENGTH_SHORT).show();
                            removeIndex=i;
                        }
                    }
                    localList.remove(removeIndex);
                    refAttending.setValue(localList);
                    Toast.makeText(DetailsActivity.this, "No longer attending event", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetailsActivity.this, "User was never attending", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        if (refComments != null) {
            refComments.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<Integer> list = new ArrayList<>();
                    //GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<>();
                    List messages = (List) snapshot.getValue();
                    for (int i = 0; i < messages.size(); i++) {
                        //Toast.makeText(DetailsActivity.this, "Message " + messages.get(i), Toast.LENGTH_SHORT).show();
                    }

                }

                // onCancelled...

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        refAttending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List attending = (List) dataSnapshot.getValue();
                localList = attending;

                attendingSet = new HashSet<>();
                for(int i=0; i<attending.size();i++){
                    if(((String)attending.get(i)).equals(user.getUid())){

                    }
                    attendingSet.add((String)attending.get(i));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    }

