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
    private Button login, signUp, signOut;
    private String username;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popupUsername;
    private EditText popupEmail;
    private EditText popupPwd;
    private Button addBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Toast.makeText(MainActivity.this,"Hour: "+hour+" Minutre: "+minute, Toast.LENGTH_LONG).show();


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
        signOut = (Button) findViewById(R.id.signOutBtn);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null){
                    //User signed in
                    //Go to Post Board Activity
                    Log.d("Here: ", "user signed in");
                }else{
                    //user is signed out
                    //Toast.makeText(MainActivity.this,"Signed Out", Toast.LENGTH_LONG).show();
                    Log.d("Here: ", "user not signed in");
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();

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
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() == null){
                    Toast.makeText(MainActivity.this,"No User is signed in", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"Signing out user: "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                    mAuth.signOut();
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
                /*
                mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Show Dialog
                            alert = new AlertDialog.Builder(MainActivity.this);



                            currUser = FirebaseAuth.getInstance().getCurrentUser();
                            User user = new User("TestName","Bob",12);
                            mDatabase.child("users").child(currUser.getUid()).setValue(user);
                            //mDatabase.child("users").child("1").setValue("2");
                            //database.child("users").child(emailText).setValue("yes");
                            Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                */
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
                    //mDatabase.child("users").child("1").setValue("2");
                    //database.child("users").child(emailText).setValue("yes");
                    Toast.makeText(MainActivity.this, "Created Account", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    @Override void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    */

}

