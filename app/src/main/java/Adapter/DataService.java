package Adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by robertwais on 4/2/18.
 */

public class DataService {

    private FirebaseDatabase db;
    private DatabaseReference dbRef, mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final DataService ourInstance = new DataService();
    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }

    public void connectToAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(){

    }

}
