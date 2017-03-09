package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "login";
    private EditText emailedt;
    private EditText passwdedt;
    private Button loginbtn;
    private TextView txtEmail;
    private TextView txtPasswd;
    private int error;

    private ProgressDialog progressDialog;

    public void initviews(){
        emailedt = (EditText)findViewById(R.id.emailEdt);
        passwdedt = (EditText)findViewById(R.id.passwdedt);
        loginbtn = (Button)findViewById(R.id.logbt);
        txtEmail = (TextView)findViewById(R.id.emailTxt);
        txtPasswd = (TextView)findViewById(R.id.passwdTxt);

        progressDialog = new ProgressDialog(this);
    }
    private void userlogin(){
        String email = emailedt.getText().toString();
        String password = passwdedt.getText().toString();

        //checks if empty
        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter your Email Address!", Toast.LENGTH_SHORT).show();
            //warning
            txtEmail.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }
        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter a Password!", Toast.LENGTH_SHORT).show();
            //warning
            txtPasswd.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }

        if (error > 0) {
            error = 0;
            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(login.this, "Make sure both Email and password are correct",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(login.this, login.class));
                        }

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        SharedPreferences mPrefs = getSharedPreferences("info", 0);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("user", user.getDisplayName().toString()).commit();

                        if (user.getDisplayName() == null){
                            mEditor.putString("user", "...").commit();
                        }

                        finish();
                        Intent myIntent = new Intent(login.this, home.class);
                        startActivity(myIntent);
                        // ...
                    }
                });
    }

    public void clicklistener(){
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });
    }

    private void getCurrentuser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            //String name = user.getDisplayName();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            //boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //String uid = user.getUid();

            SharedPreferences mPrefs = getSharedPreferences("info", 0);
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putString("user", user.getDisplayName().toString()).commit();

            if (user.getDisplayName() == null){
                mEditor.putString("user", "...").commit();
            }
            finish();
            Intent myIntent = new Intent(login.this, home.class);
            startActivity(myIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        getCurrentuser();
        initviews();
        clicklistener();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
