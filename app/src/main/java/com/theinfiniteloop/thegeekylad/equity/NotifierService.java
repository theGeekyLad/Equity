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
                .setContentTitle((context)?"Alert! You have pending meetings":"Alert! You have pending liabilities")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .setSound(Uri.parse(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((context)?121:169, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseDatabase.getInstance().getReference("employee")
                .child(formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("meeting").exists())
                            ping(true);
                        if (dataSnapshot.child("liability").exists())
                            ping(false);
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
