package com.theinfiniteloop.thegeekylad.equity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;

import zxing.IntentIntegrator;
import zxing.IntentResult;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // init
        //hideBars();

        //animating
        YoYo.with(Techniques.BounceIn).duration(200).playOn(findViewById(R.id.scan));
        YoYo.with(Techniques.BounceIn).duration(500).playOn(findViewById(R.id.navigate));
        YoYo.with(Techniques.BounceIn).duration(800).playOn(findViewById(R.id.book));
        YoYo.with(Techniques.BounceIn).duration(1100).playOn(findViewById(R.id.schedule));
        YoYo.with(Techniques.BounceIn).duration(1400).playOn(findViewById(R.id.liabilities));


        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(IndexActivity.this);
                integrator.initiateScan();
            }
        });

        findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this, BookActivity.class));
            }
        });

        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this, PlanActivity.class));
            }
        });

        findViewById(R.id.schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this, ScheduleActivity.class));
            }
        });

        findViewById(R.id.liabilities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IndexActivity.this, LiabilitiesActivity.class));
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                RW rw = new RW();
                rw.write(new File(getFilesDir(), "url.txt"), scanResult.getContents());
                Toast.makeText(this, "Map downloaded!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Nothing to show!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            new File(getFilesDir(), "url.txt").delete();
        }
    }

    /*
    private void hideBars() {
        final View systemView = getWindow().getDecorView();
        systemView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        systemView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                systemView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            }
        });
    }
    */
}
