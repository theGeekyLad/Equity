package com.theinfiniteloop.thegeekylad.equity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Scanner;

public class LiabilitiesActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liabilities);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Liabilities");
        setSupportActionBar(toolbar);

        //init
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.ll);

        databaseReference
                .child("employee")
                .child(new RW().formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("liability")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        linearLayout.removeAllViews();

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {

                            String s = iterator.next().getValue(String.class);

                            //Scanner likeScanner = new Scanner(s);
                            //likeScanner.useDelimiter("_");
                            Scanner unlikeScanner = new Scanner(s);
                            unlikeScanner.useDelimiter("#");



                            View scheduleView = getLayoutInflater().inflate(R.layout.liabilities_layout, null);
                            ((TextView)scheduleView.findViewById(R.id.title)).setText(unlikeScanner.next());
                            ((TextView)scheduleView.findViewById(R.id.date)).setText(new RW().formatDate(unlikeScanner.next()));
                            ((TextView)scheduleView.findViewById(R.id.time)).setText(new RW().formatTime(unlikeScanner.next()));
                            ((TextView)scheduleView.findViewById(R.id.location)).setText(unlikeScanner.next());
                            ((TextView)scheduleView.findViewById(R.id.supervisor)).setText(unlikeScanner.next());

                            linearLayout.addView(scheduleView);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
