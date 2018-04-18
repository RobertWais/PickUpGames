package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Adapter.EventAdapter;
import Model.ListItem;
import Model.Post;

public class EventBoardActivity extends AppCompatActivity {

    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private FirebaseAuth auth;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private List<Post> postList;
    private Button createButton;
    private Button signOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        fDatabase = FirebaseDatabase.getInstance();
        dbRef = fDatabase.getReference().child("Posts");
        postList = new ArrayList<>();




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_board);

        createButton = findViewById(R.id.create);
        signOut = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra("flag");

        /*
        if(checkFlag.equals("create")) {
            ListItem item = new ListItem(
                    intent.getStringExtra("title"),
                    intent.getStringExtra("desc")
            );
            listItems.add(item);
        }


        for(int i = 0; i < 15; i++) {
             ListItem item = new ListItem(
                     "Item" + (i+1),
                     "Description"+(i)
             );

             listItems.add(item);
        }
*/

        //adapter = new EventAdapter(this, listItems);
        //recyclerView.setAdapter(adapter);

        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventBoardActivity.this, CreateEventActivity.class);
                //intent.putExtra("list", listItems);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Post post = dataSnapshot.getValue(Post.class);
                postList.add(post);

                adapter = new EventAdapter(EventBoardActivity.this, postList);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


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

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EventBoardActivity.this,"Signing out user: "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(EventBoardActivity.this, MainActivity.class));
            }
        });
    }
}
