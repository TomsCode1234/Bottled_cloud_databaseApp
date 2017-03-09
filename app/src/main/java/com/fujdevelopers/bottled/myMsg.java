package com.fujdevelopers.bottled;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class myMsg extends AppCompatActivity {

    private TextView sent;
    private TextView read;
    private TextView user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_msg);
        initVeiws();

    }

    private void initVeiws() {
        sent = (TextView)findViewById(R.id.sentTxt);
        read = (TextView)findViewById(R.id.readTxt);
        user = (TextView)findViewById(R.id.UserTxt);
        setText();
    }

    private void setText() {
        SharedPreferences mperf = getSharedPreferences("msg", 0);
        int sentint = mperf.getInt("sent", 0);
        sent.setText((Integer.toString(sentint)));

        int recint = mperf.getInt("received", 0);
        read.setText((Integer.toString(recint)));
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setText(mUser.getDisplayName().toString());
    }

}
