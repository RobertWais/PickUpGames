package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Adapter.EventAdapter;
import Adapter.commentAdapter;
import Model.Comment;
import Model.Post;

public class DetailsActivity extends AppCompatActivity {
    private String title, description, keyAttending;
    private TextView descIn, titleIN, timeIN, commentField;
    private Bundle passedThru;
    private Button attend, decline, delete,commentBtn;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference dbRef, refPost, refComments,
            refAttending, postRef, postUserID,userNameRef,setcomment;
    private HashSet<String> attendingSet = new HashSet<>();
    private ArrayList<String> localList;
    private String postID,userName;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Comment> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //DATABASE for comments


        //RECYCLER VIEWS
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Comment comm = new Comment("Bob", "This sucks");
            //listItems.add(comm);
        }
        //adapter = new commentAdapter(this,listItems);
        //recyclerView.setAdapter(adapter);


        passedThru = getIntent().getExtras();
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        userNameRef = db.getReference().child("users").child(user.getUid()).child("name");
        postRef = db.getReference().child("Posts").child(passedThru.getString("CommentsID"));
        postUserID = db.getReference().child("Posts").child(passedThru.getString("CommentsID")).child("userID");
        dbRef = db.getReference().child("Comments").child(passedThru.getString("CommentsID")).child("comments");
        refPost = db.getReference().child("Comments").child(passedThru.getString("CommentsID"));
        refComments = refPost.child("comments");
        refAttending = refPost.child("attending");

        if (dbRef == null) {
            Toast.makeText(DetailsActivity.this, "We null", Toast.LENGTH_SHORT).show();
        }


        commentBtn = (Button) findViewById(R.id.addComment);
        commentField = (EditText) findViewById(R.id.commentInput);
        delete = (Button) findViewById(R.id.deleteBtn);
        attend = (Button) findViewById(R.id.attendingBtn);
        decline = (Button) findViewById(R.id.declineBtn);
        descIn = (TextView) findViewById(R.id.descInput);
        titleIN = (TextView) findViewById(R.id.titleInput);
        timeIN = (TextView) findViewById(R.id.timeInput);


        descIn.setMovementMethod(new ScrollingMovementMethod());

        timeIN.setText(passedThru.getString("Time"));
        descIn.setText(passedThru.getString("Description"));
        titleIN.setText(passedThru.getString("Title"));

        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postUserID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postID = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUid().equals(postID)) {
                    postRef.removeValue();
                    finish();
                } else {
                    Toast.makeText(DetailsActivity.this, "You cannoot delete this Post", Toast.LENGTH_SHORT).show();

                }

            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setcomment = db.getReference().child("Comments").child(passedThru.getString("CommentsID")).child("comments").child(userName);
                setcomment.setValue(commentField.getText().toString());

            }
        });

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if already attending
                if (attendingSet.contains(user.getUid())) {
                    Toast.makeText(DetailsActivity.this, "Already Attending ", Toast.LENGTH_SHORT).show();
                } else {
                    String id = Integer.toString(attendingSet.size());

                    refAttending.child(id).setValue((String) user.getUid());
                    Toast.makeText(DetailsActivity.this, "You are now attending this event", Toast.LENGTH_SHORT).show();
                }
                //if not add to attending
                //if so display toast


            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendingSet.contains(user.getUid())) {
                    //Remove from list
                    int removeIndex = 0;
                    for (int i = 0; i < localList.size(); i++) {
                        if (localList.get(i).equals(user.getUid())) {
                            Toast.makeText(DetailsActivity.this, "Its in there", Toast.LENGTH_SHORT).show();
                            removeIndex = i;
                        }
                    }
                    localList.remove(removeIndex);
                    refAttending.setValue(localList);
                    Toast.makeText(DetailsActivity.this, "No longer attending event", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "User was never attending", Toast.LENGTH_SHORT).show();
                }
            }
        });

        refPost.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DatabaseReference temp = dataSnapshot.getRef();

                //DONT DELETE

                Toast.makeText(DetailsActivity.this,"Username?we: "+ temp.getKey()+"val: "+s, Toast.LENGTH_LONG).show();
                /*
                String tempUser = s;
                String tempComment = (String) dataSnapshot.getValue();
                Comment tempFull = new Comment(tempUser, tempComment);
                adapter = new commentAdapter(DetailsActivity.this, listItems);
                recyclerView.setAdapter(adapter);
                listItems.add(tempFull);
                adapter.notifyDataSetChanged();
                */
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }
        @Override
        protected void onStart () {
            super.onStart();

            if (refComments != null) {
                refComments.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //ArrayList<Integer> list = new ArrayList<>();
                        //GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<>();
                        //ArrayList<String> messages = (ArrayList) snapshot.getValue();
                        //Toast.makeText(DetailsActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

                            try{
                                HashMap<String, String> messages = (HashMap) snapshot.getValue();
                                if (messages != null) {
                                    for (String s : messages.keySet()) {
                                        Toast.makeText(DetailsActivity.this, "User " + s + " Message: " + messages.get(s), Toast.LENGTH_SHORT).show();
                                        Comment tempComm = new Comment(s,messages.get(s));

                                        adapter = new commentAdapter(DetailsActivity.this, listItems);
                                        recyclerView.setAdapter(adapter);
                                        listItems.add(tempComm);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(DetailsActivity.this, "No Messages", Toast.LENGTH_SHORT).show();
                                }
                            } catch(Exception e){
                                //Null
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
                    ArrayList<String> attending = (ArrayList) dataSnapshot.getValue();
                    //List attending = (List) dataSnapshot.getValue();
                    localList = attending;

                    attendingSet = new HashSet<>();
                    if (attending != null) {
                        for (int i = 0; i < attending.size(); i++) {
                            if (((String) attending.get(i)).equals(user.getUid())) {

                            }
                            attendingSet.add((String) attending.get(i));
                        }
                    } else {
                        attending = new ArrayList<>();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }



