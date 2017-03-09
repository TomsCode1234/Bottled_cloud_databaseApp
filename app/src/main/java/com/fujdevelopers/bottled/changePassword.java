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

public class changePassword extends AppCompatActivity {

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "register";

    private ProgressDialog progressBar;

    //button
    private Button change;
    //text

    //editText
    private EditText oldpass;
    private EditText newpass;
    private EditText confnewpass;

    //txt veiw
    private TextView oldpasstxt;
    private TextView newpasstxt;
    private TextView confnewpasstxt;

    public void init(){
        oldpass = (EditText)findViewById(R.id.oldedt);
        newpass = (EditText)findViewById(R.id.newedt);
        confnewpass = (EditText)findViewById(R.id.confnewedt);
        //button
        change = (Button)findViewById(R.id.changePasswdBtn);
        //txt
        oldpasstxt = (TextView)findViewById(R.id.olpsswd);
        oldpasstxt.setTextColor(Color.parseColor("#172d83"));
        newpasstxt = (TextView)findViewById(R.id.newPassWd);
        newpasstxt.setTextColor(Color.parseColor("#172d83"));
        confnewpasstxt = (TextView)findViewById(R.id.confNewPassWd);
        confnewpasstxt.setTextColor(Color.parseColor("#172d83"));
    }

    public void changeuserspassword(){
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Changing... ");
        progressBar.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail().toString();
        String password = oldpass.getText().toString();
        int error = 0;

        //checks if empty
        if (TextUtils.isEmpty(email)) {
            //email is empty
            error = error + 1;
        }
        if ((oldpass.getText().toString().length()) < 6) {
            //password is empty
            Toast.makeText(this, "Please enter old Password!", Toast.LENGTH_SHORT).show();
            //warning
            oldpasstxt.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }else {oldpasstxt.setTextColor(Color.parseColor("#172d83"));}

        if ((newpass.getText().toString().length()) < 6) {
            //password is empty
            Toast.makeText(this, "Please enter new Password longer then 6 characters!", Toast.LENGTH_SHORT).show();
            //warning
            newpasstxt.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }else {newpasstxt.setTextColor(Color.parseColor("#172d83"));}

        if ((confnewpass.getText().toString().length()) < 6) {
            //password is empty
            Toast.makeText(this, "Please confirm new Password!", Toast.LENGTH_SHORT).show();
            //warning
            confnewpasstxt.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }else {confnewpasstxt.setTextColor(Color.parseColor("#172d83"));}

        if ((!newpass.getText().toString().equals(confnewpass.getText().toString()))) {
            //password is empty
            Toast.makeText(this, "passwords do not match!", Toast.LENGTH_SHORT).show();
            //warning
            confnewpasstxt.setTextColor(Color.parseColor("#ff0000"));
            //stop
            error = error + 1;
        }

        if (error > 0) {
            error = 0;
            return;
        }

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
                        }

                        if (task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updatePassword(newpass.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User password address updated.");
                                                FirebaseAuth.getInstance().signOut();
                                                finish();
                                                startActivity(new Intent(changePassword.this, login.class));
                                            }
                                        }
                                    });
                        }
                        // ...
                    }
                });
    }

    public void onClick(){
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeuserspassword();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mAuth = FirebaseAuth.getInstance();
        init();
        onClick();
    }
}
