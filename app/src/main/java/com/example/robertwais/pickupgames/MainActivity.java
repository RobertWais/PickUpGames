package com.example.robertwais.pickupgames;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
//test test testRob testRob2
    private AlertDialog.Builder alert;

    private FirebaseDatabase db;
    private DatabaseReference dbRef, mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email;
    private EditText password;
    private Button login, signUp;
    private String username;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupUsername;
    private EditText popupEmail;
    private EditText popupPwd;
    private Button addBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("tester");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbRef.setValue("test1");


        email = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.passwordID);
        login = (Button) findViewById(R.id.LoginBtn);
        signUp = (Button) findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();

                if(email.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Please fill out username", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"Please fill out password", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this,"Loading...", Toast.LENGTH_SHORT).show();

                    mAuth.signInWithEmailAndPassword(emailText,passwordText)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                                        dbRef.setValue("test2");
                                        Intent intent = new Intent(MainActivity.this, EventBoardActivity.class);
                                        intent.putExtra("flag", "login");
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailText = email.getText().toString();
                username = emailText;
                String passwordText = password.getText().toString();
                if(!emailText.equals("") && !passwordText.equals("")){

                }

                createPopupDialog();
            }
        });

    }

    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        popupUsername = view.findViewById(R.id.userName);
        popupEmail = view.findViewById(R.id.email);
        popupPwd = view.findViewById(R.id.password);
        addBtn = view.findViewById(R.id.add);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        addBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addUser(popupEmail.getText().toString(), popupPwd.getText().toString(), popupUsername.getText().toString());
                dialog.hide();
            }
        });
    }

    @Override
    protected  void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void addUser(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Show Dialog
                    alert = new AlertDialog.Builder(MainActivity.this);

                    currUser = FirebaseAuth.getInstance().getCurrentUser();
                    User user = new User(email, username, 12);
                    mDatabase.child("users").child(currUser.getUid()).setValue(user);
                    Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}

