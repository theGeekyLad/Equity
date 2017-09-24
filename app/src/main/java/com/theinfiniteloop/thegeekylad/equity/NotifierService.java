package com.theinfiniteloop.thegeekylad.equity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.NoSuchElementException;

public class NotifierService extends Service {
    public NotifierService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void ping(boolean context) {

        Intent intent = new Intent(this, (context)?ScheduleActivity.class:LiabilitiesActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle((context)?"Meeting":"Liability")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setSound(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase.getInstance().getReference("employee")
                .child(formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("meeting")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {

                            ping(true);

                        } catch (NoSuchElementException nsee) {
                            Toast.makeText(NotifierService.this, "You have no meetings lined up!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("employee")
                .child(formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .child("liability")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {

                            ping(false);

                        } catch (NoSuchElementException nsee) {
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return START_STICKY;

    }

    public String formatEmail(String s) {

        String str = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.')
                str += "^";
            else
                str += s.charAt(i);
        }

        return str;

    }

}
