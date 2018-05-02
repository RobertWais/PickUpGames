package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import Model.Stats;

public class DetailsActivity extends AppCompatActivity {
    private String title, description, keyAttending;
    private TextView descIn, titleIN, timeIN, commentField,attendingCount;
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
    private int commentSize;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button confirm, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //RECYCLER VIEWS
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Comment comm = new Comment("Bob", "This sucks");
        }

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

        attendingCount = (TextView) findViewById(R.id.attendingDisplay);
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
                if (!user.getUid().equals(postID)) {
                    View view2 = DetailsActivity.this.getCurrentFocus();
                   delete.setVisibility(view2.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentField.getText().toString().equals("")){
                    //NO COMMENTS
                }else{

                    Comment tempComm = new Comment(userName,commentField.getText().toString());
                    setcomment = db.getReference().child("Comments").child(passedThru.getString("CommentsID")).child("comments").child(Integer.toString(commentSize));
                    setcomment.setValue(tempComm);
                    View view2 = DetailsActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(DetailsActivity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                    }
                    commentField.setText("");
                }

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

        refAttending.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> attending = (ArrayList) dataSnapshot.getValue();
                //List attending = (List) dataSnapshot.getValue();

                localList = attending;
                attendingSet = new HashSet<>();
                if (attending != null) {
                    if(attending.size()==1){
                        attendingCount.setText("("+attending.size()+" person going"+")");

                    }else{
                        attendingCount.setText("("+attending.size()+" people going"+")");

                    }
                    for (int i = 0; i < attending.size(); i++) {
                        if (((String) attending.get(i)).equals(user.getUid())) {

                        }
                        attendingSet.add((String) attending.get(i));
                    }
                } else {
                    attending = new ArrayList<>();
                    attendingCount.setText("(No one yet attending)");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refComments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listItems.removeAll(listItems);
                ArrayList<HashMap<String,String>> messages = (ArrayList) snapshot.getValue();

                //HashMap<String, Comment> messages = (HashMap) snapshot.getValue();
                if (messages != null) {

                    commentSize= messages.size();
                Stats.getInstance().setCommentSize(commentSize);

                    for(int i=0;i<messages.size();i++){

                        String tempUserName = messages.get(i).get("user");
                        String tempComment =  messages.get(i).get("comment");
                        Comment tempComm = new Comment(tempUserName,tempComment);

                        adapter = new commentAdapter(DetailsActivity.this, listItems);
                        recyclerView.setAdapter(adapter);
                        listItems.add(tempComm);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.are_you_sure, null);
        confirm = view.findViewById(R.id.yes);
        cancel = view.findViewById(R.id.no);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (user.getUid().equals(postID)) {
                    postRef.removeValue();
                    finish();
                } else {
                    Toast.makeText(DetailsActivity.this, "You cannot delete this Post", Toast.LENGTH_SHORT).show();

                }
                dialog.hide();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
    }
}



