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
    private int flag = 1;


    @Override
    protected void onStop() {
        super.onStop();
        flag = 0;
    }

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


        //adapter = new EventAdapter(this, listItems);
        //recyclerView.setAdapter(adapter);

        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventBoardActivity.this, CreateEventActivity.class);
                //intent.putExtra("list", listItems);
                startActivity(intent);
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

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatabaseReference temp = dataSnapshot.getRef();

                //DONT DELETE
                //Toast.makeText(EventBoardActivity.this,"ID: "+ temp.getKey(), Toast.LENGTH_LONG).show();

                Post post = dataSnapshot.getValue(Post.class);
                post.setPostId(temp.getKey());
                adapter = new EventAdapter(EventBoardActivity.this, postList);
                recyclerView.setAdapter(adapter);
                postList.add(post);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            ///ADD REMOVE
                String key = dataSnapshot.getKey();
                Post p = dataSnapshot.getValue(Post.class);
                if(postList.contains(p)){
                    postList.remove(p);
                    adapter.notifyDataSetChanged();
                }
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
    protected void onStart() {
        super.onStart();
            if(flag==1) {

            }
    }
}
