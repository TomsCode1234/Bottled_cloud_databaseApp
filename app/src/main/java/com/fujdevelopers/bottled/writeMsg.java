package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class writeMsg extends AppCompatActivity {

    final static String  DB_URL="https://bottled-ecf7c.firebaseio.com/";

    private EditText messageEdt;
    private Button sendBtn;
    private int sentNoId;
    private int recNoId;
    private int total;
    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_msg);
        initializeFirebase();
        progressBar = new ProgressDialog(this);
        initializeVeiws();
        onClickListener();

    }

    private void onClickListener() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!messageEdt.getText().toString().equals("")) {

                    progressBar.setMessage("Sending... ");
                    progressBar.show();
                    addMsg();
                    finish();
                    startActivity(new Intent(writeMsg.this, home.class));
                }
            }
        });
    }

    private void initializeVeiws() {

        messageEdt = (EditText)findViewById(R.id.msgEdt);
        sendBtn = (Button)findViewById(R.id.sendBtn);

    }

    //init firebase
    private void initializeFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addMsg(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences mPrefs = getSharedPreferences("msg", 0);
        sentNoId = mPrefs.getInt("sent", 1);
        recNoId = mPrefs.getInt("rec", 0);

        SharedPreferences ePrefs = getSharedPreferences("tally", 0);
        total = ePrefs.getInt("total", 1);

        message m = new message();
        m.setName(user.getDisplayName().toString());
        m.setMessage(messageEdt.getText().toString());
        m.setEmail(user.getEmail().toString());


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String result = (user.getEmail().toString()).replaceAll("[,.-@]","");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages").child((currentDateTimeString) + (result)+ (Integer.toString(sentNoId))+(Integer.toString(total))+(Integer.toString(recNoId)));

        myRef.setValue(m);


        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt("sent", (sentNoId + 1)).commit();


        SharedPreferences.Editor eEditor = ePrefs.edit();
        eEditor.putInt("total", (total + 1)).commit();
    }
}
