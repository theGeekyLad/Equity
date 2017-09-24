package com.theinfiniteloop.thegeekylad.equity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Scanner;

public class ScheduleActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Schedule");
        setSupportActionBar(toolbar);

        //init
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.ll);

        databaseReference
                .child("employee")
                .child(new RW().formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("schedule")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        linearLayout.removeAllViews();

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {

                            String s = iterator.next().getValue(String.class);

                            //Scanner likeScanner = new Scanner(s)``;
                            //likeScanner.useDelimiter("_");
                            Scanner unlikeScanner = new Scanner(s);
                            unlikeScanner.useDelimiter("#");

                            View scheduleView = getLayoutInflater().inflate(R.layout.schedule_layout, null);
                            ((TextView)scheduleView.findViewById(R.id.title)).setText(unlikeScanner.next());
                            ((TextView)scheduleView.findViewById(R.id.date)).setText(new RW().formatDate(unlikeScanner.next()));
                            ((TextView)scheduleView.findViewById(R.id.time)).setText(new RW().formatTime(unlikeScanner.next()));
                            ((TextView)scheduleView.findViewById(R.id.location)).setText(unlikeScanner.next());

                            linearLayout.addView(scheduleView);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                final View titleView = getLayoutInflater().inflate(R.layout.schedule_edittext, null);
                new AlertDialog.Builder(this)
                        .setTitle("Title")
                        .setView(titleView)
                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ScheduleActivity.this, BookActivity.class);
                                intent.putExtra("schedule_title", ((EditText)titleView.findViewById(R.id.title)).getText().toString());
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
