package com.fujdevelopers.bottled;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class readMsg extends AppCompatActivity {

    private TextView message;
    private TextView from;
    private TextView user;

    private ProgressDialog progressBar;

    private static final String TAG = "Data";

    private int error = 1;
    public static String result;

    public static String User;
    public static String themessage;
    public static String Email;
    private String res1;
    private String res2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_msg);
        initializeFirebase();
        progressBar = new ProgressDialog(this);
        initializeVeiws();
        SharedPreferences ePerfs = getSharedPreferences("tally", 0);
        int totalscr = ePerfs.getInt("total", 0);
        if (totalscr>0) {
            readfromDatabase();
        }else{
            message.setText("you need to write more messages first");
        }
    }


    private void readfromDatabase() {

        progressBar.setMessage("searching the Oceans... ");
        progressBar.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("messages");

        myRef.orderByPriority().limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e("Count", "" + dataSnapshot.getChildren());
                    final String name = (String) dataSnapshot.getValue().toString();
                    String beforeFirstequal = name.split("=")[0];
                    result = (beforeFirstequal.replaceAll("[{]", ""));

                    myRef.removeEventListener(this);

                    final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("messages").child(result);
                    newRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.e("Count", "" + dataSnapshot.getChildren());
                                User = (String) dataSnapshot.child("name").getValue();
                                themessage = (String) dataSnapshot.child("message").getValue();
                                Email = (String) dataSnapshot.child("email").getValue();
                                //check for email match


                                FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                                if (!Email.equals(curuser.getEmail())) {//change this tho

                                    myRef.child(result).removeValue();

                                    progressBar.dismiss();

                                    message.setText(themessage);
                                    from.setText("from");
                                    user.setText(User);

                                    SharedPreferences mPrefs = getSharedPreferences("msg", 0);
                                    SharedPreferences ePerfs = getSharedPreferences("tally", 0);

                                    int total = mPrefs.getInt("received", 0);

                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    SharedPreferences.Editor eEditor = ePerfs.edit();

                                    mEditor.putInt("received", (total + 1)).commit();
                                    int totalscr = ePerfs.getInt("total", 0);
                                    mEditor.putInt("recived", (totalscr+1)).commit();



                                } else {
                                    if (error<3){
                                    //add reupload here
                                    message.setText("correct");


                                        myRef.orderByPriority().limitToLast(1).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    final String name = (String) dataSnapshot.getValue().toString();
                                                    String beforeFirst = name.split("=")[0];
                                                    result = (beforeFirst.replaceAll("[{]", ""));
                                                    myRef.removeEventListener(this);
                                                    final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference("messages").child(result);
                                                    newRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            User = (String) dataSnapshot.child("name").getValue();
                                                            themessage = (String) dataSnapshot.child("message").getValue();
                                                            Email = (String) dataSnapshot.child("email").getValue();
                                                            //check for email match
                                                            newRef.removeEventListener(this);
                                                            FirebaseUser curuser = FirebaseAuth.getInstance().getCurrentUser();
                                                            if (!Email.equals(curuser.getEmail())) {//change this tho

                                                                myRef.child(result).removeValue();

                                                                //startActivity(new Intent(readMsg.this, messageactivity.class));
                                                                progressBar.dismiss();

                                                                message.setText(themessage);
                                                                from.setText("from");
                                                                user.setText(User);

                                                                SharedPreferences mPrefs = getSharedPreferences("msg", 0);

                                                                int total = mPrefs.getInt("received", 0);

                                                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                                                SharedPreferences ePerfs = getSharedPreferences("tally", 0);

                                                                mEditor.putInt("received", (total + 1)).commit();
                                                                int totalscr = ePerfs.getInt("total", 0);
                                                                mEditor.putInt("recived", (totalscr+1)).commit();

                                                            } else {
                                                                error = error+1;
                                                                readfromDatabase();
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        /*message m = new message();
                                        m.setName(User);
                                        m.setMessage(themessage);
                                        m.setEmail(Email);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference sendRef = database.getReference("messages").child(result);
                                        sendRef.setValue(m);
                                        error = error + 1;
                                        readfromDatabase();*/

                                    }else {
                                        progressBar.dismiss();
                                        message.setText("No Messages to read right now, come back later");}

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "The read failed");
                        }


                    });
                }else {
                    progressBar.dismiss();
                    message.setText("No Messages to read right now, come back later");}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The read failed" );
            }
        });

    }


    private void initializeVeiws() {
        message = (TextView)findViewById(R.id.messageTxt);
        from = (TextView)findViewById(R.id.FromTxt);
        user = (TextView)findViewById(R.id.userTxt);

        from.setText("");
        user.setText("");
    }

    //init firebase
    private void initializeFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    }
}
