package com.fujdevelopers.bottled;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;

public class home extends AppCompatActivity {


    public static String userEmail;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "login";

    private TextView usernameTxt;

    private Button Profile;
    private Button Settings;
    private Button WriteBtn;
    private Button ReadBtn;
    private Button myConfBtn;

    private String username;
    private String MsgNo;

    public void init(){
        Profile = (Button)findViewById(R.id.profbtn);
        WriteBtn = (Button)findViewById(R.id.writebtn);
        ReadBtn = (Button)findViewById(R.id.readbtn);
        myConfBtn = (Button)findViewById(R.id.confbtn);

        usernameTxt = (TextView)findViewById(R.id.usnam);
        usernameTxt.setText(username);

    }

    public void checkprofile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //username = user.getDisplayName().toString();
        if (user.getDisplayName() == null){
            finish();
            startActivity(new Intent(home.this, ChangeUserName.class));
        }
        if (user == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void getProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            SharedPreferences mPrefs = getSharedPreferences("info", 0);
            String mString = mPrefs.getString("user", user.getDisplayName());
            usernameTxt.setText(mString);
        }
    }

    public void clicklisten(){
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, profileEdit.class));
            }
        });

        WriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, writeMsg.class));
            }
        });

        ReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, readMsg.class));
            }
        });

        myConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, myMsg.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth.getInstance();


        init();
        getProfile();
        checkprofile();

        clicklisten();
    }
}
