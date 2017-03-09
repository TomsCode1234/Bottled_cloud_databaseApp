package com.fujdevelopers.bottled;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class profileEdit extends AppCompatActivity {

    private TextView username;
    private TextView email;

    private Button chngUserBtn;
    private Button chngEmailBtn;
    private Button ChngPasswdBtn;
    private Button LogOutBtn;

    private String provider;

    private static final String TAG = "login";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void init(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        chngUserBtn = (Button)findViewById(R.id.userBtn);
        chngEmailBtn = (Button)findViewById(R.id.emailBtn);
        ChngPasswdBtn = (Button)findViewById(R.id.passwordBtn);
        LogOutBtn = (Button)findViewById(R.id.LogoutBtn);

        username = (TextView) findViewById(R.id.userTxt);
        email = (TextView) findViewById(R.id.emailTxt);

        username.setText(user.getDisplayName().toString());
        email.setText(user.getEmail().toString());
    }

    private void onClick(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        provider = "firebase";
        ChngPasswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provider.equals(user.getProviderId().toString())) {
                    startActivity(new Intent(profileEdit.this, changePassword.class));
                }
            }
        });
        chngEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provider.equals(user.getProviderId().toString())) {
                    startActivity(new Intent(profileEdit.this, changeEmail.class));
                }
            }
        });
        chngUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileEdit.this, ChangeUserName.class));
            }
        });
        LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(profileEdit.this, MainActivity.class));
            }
        });
    }

    public void textset(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d(TAG, user.getProviderId().toString());

        SharedPreferences mPrefs = getSharedPreferences("info", 0);
        String mString = mPrefs.getString("user", user.getDisplayName());

        username.setText(mString);

        email.setText(user.getEmail().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        mAuth.getInstance();

        init();
        textset();
        onClick();
    }


}
