package com.theinfiniteloop.thegeekylad.equity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.mapwize.mapwize.MWZAccountManager;

public class MainActivity extends AppCompatActivity {

    String t;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        (findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    startService(new Intent(MainActivity.this, NotifierService.class));

                    startActivity(new Intent(getApplicationContext(), IndexActivity.class));

                }

                else {
                    final View loginForm = getLayoutInflater().inflate(R.layout.login_form,null);
                    new AlertDialog.Builder(MainActivity.this)
                            .setView(loginForm)
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                            t = ( (EditText) loginForm.findViewById(R.id.email)).getText().toString(),
                                            ( (EditText) loginForm.findViewById(R.id.password)).getText().toString())
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {

                                                    Toast.makeText(MainActivity.this, "Welcome, "+t, Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            })
                            .create()
                            .show();
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //animate
        YoYo.with(Techniques.Tada).duration(1000).playOn(findViewById(R.id.logo));
    }

}
