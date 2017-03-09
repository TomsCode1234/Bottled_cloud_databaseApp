package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {

    //views

    private Button btnRegister;
    private EditText edtEmail;
    private EditText edtPasswd;
    private EditText edtConfPasswd;
    private TextView txtEmail;
    private TextView txtPasswd;
    private TextView txtConfPasswd;


    //init views
    public void initViews() {
        //Buttons
        btnRegister = (Button) findViewById(R.id.registerBtn);
        //...

        //edit texts
        edtEmail = (EditText) findViewById(R.id.emailEdt);
        edtPasswd = (EditText) findViewById(R.id.passwdEdt);
        edtConfPasswd = (EditText) findViewById(R.id.confPasswdEdt);
        //...

        //text views
        txtEmail = (TextView) findViewById(R.id.emailTxt);
        txtPasswd = (TextView) findViewById(R.id.passwdTxt);
        txtConfPasswd = (TextView) findViewById(R.id.cnfPasswdTxt);

    }

    //boolean ints
    private int error = 0;

    //vars
    private ProgressDialog progressBar;

    //firebase connection
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "register";

    private void registerUser() {
        //gets content
        String email = edtEmail.getText().toString().trim();
        String Passwd = edtPasswd.getText().toString().trim();
        String PasswdConf = edtConfPasswd.getText().toString().trim();

        //sets original colour
        txtEmail.setTextColor(Color.parseColor("#172d83"));
        txtPasswd.setTextColor(Color.parseColor("#172d83"));
        txtConfPasswd.setTextColor(Color.parseColor("#172d83"));

        //checks if empty
        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter your Email Address!", Toast.LENGTH_SHORT).show();
            //warning
            txtEmail.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }
        if (TextUtils.isEmpty(Passwd)) {
            //password is empty
            Toast.makeText(this, "Please enter a Password!", Toast.LENGTH_SHORT).show();
            //warning
            txtPasswd.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }

        if (TextUtils.isEmpty(PasswdConf)) {
            //password confirm is empty
            Toast.makeText(this, "Please confirm your Password!", Toast.LENGTH_SHORT).show();
            //warning
            txtConfPasswd.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;

        }

        //check password length
        if (Passwd == null || Passwd.length() < 6) {
            error = error+1;
            Toast.makeText(this, "Password must be longer then 6 characters!", Toast.LENGTH_SHORT).show();
        }

        //checks the passwords
        if (Passwd.equals(PasswdConf)) {

        } else {
            error = error + 1;
            Toast.makeText(this, "passwords did not match!", Toast.LENGTH_SHORT).show();
        }

        //stops on error
        if (error > 0) {
            error = 0;
            return;
        }

        //if validations are ok

        //set progress bar
        progressBar.setMessage("Registering User... ");
        progressBar.show();
        //register to server
        mAuth.createUserWithEmailAndPassword(email, Passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(register.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(register.this, register.class));
                            }

                            Toast.makeText(register.this, "Registration Failed!",
                                    Toast.LENGTH_SHORT).show();
                            Log.w(TAG, task.getException());
                        }
                        finish();
                        startActivity(new Intent(register.this, MainActivity.class));
                        // ...
                    }
                });

    }

    //click listner
    public void registerInit() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressBar = new ProgressDialog(this);
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

        //init
        initViews();
        //main
        registerInit();
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

