package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.SyncStateContract;
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

public class changeEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "login";

    private ProgressDialog progressBar;

    private TextView curEmail;
    private EditText newEmail;
    private Button change;
    public void init(){
        curEmail = (TextView)findViewById(R.id.curEmailTxt);
        newEmail = (EditText)findViewById(R.id.newEmailEdt);
        change = (Button)findViewById(R.id.UchangeBtn);

    }

    public void setTxts(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        curEmail.setText(user.getEmail());
    }

    public void onClick(){
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeEmail();

            }
        });
    }

    public void changeEmail(){

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Changing... ");
        progressBar.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.w(TAG, "enter loop");
        user.updateEmail(newEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.w(TAG, "secloop");
                        if (task.isSuccessful()) {
                            Log.w(TAG, "User email address updated.");
                            //FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(changeEmail.this, login.class));
                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        init();
        setTxts();
        onClick();
    }
}
