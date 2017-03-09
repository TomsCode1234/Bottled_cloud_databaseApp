package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangeUserName extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "login";

    private ProgressDialog progressBar;

    private TextView curUsername;
    private TextView Tag;
    private EditText chngusername;
    private Button change;

    public void init(){
        curUsername = (TextView)findViewById(R.id.userTxt);
        chngusername = (EditText)findViewById(R.id.usernameEdt);
        change = (Button)findViewById(R.id.changeBtn);
        Tag = (TextView)findViewById(R.id.textView3);

    }

    public void setTxts(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences mPrefs = getSharedPreferences("info", 0);
        String mString = mPrefs.getString("user", user.getDisplayName());
        if (mString ==null){
            Tag.setText("Make sure not to use your real name!");
            curUsername.setText("");
        }
        curUsername.setText(mString);
    }

    public void onClick(){
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName();
            }
        });
    }

    public void changeName(){
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Changing... ");
        progressBar.setCancelable(false);
        progressBar.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(chngusername.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");

                            SharedPreferences mPrefs = getSharedPreferences("info", 0);
                            SharedPreferences.Editor mEditor = mPrefs.edit();
                            mEditor.putString("user", chngusername.getText().toString()).commit();

                            finish();
                            startActivity(new Intent(ChangeUserName.this, login.class));
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);
        mAuth.getInstance();



        init();
        setTxts();
        onClick();
    }
}
