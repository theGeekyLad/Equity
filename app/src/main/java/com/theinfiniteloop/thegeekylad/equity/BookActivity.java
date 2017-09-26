package com.theinfiniteloop.thegeekylad.equity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import io.mapwize.mapwize.MWZAccountManager;
import io.mapwize.mapwize.MWZCoordinate;
import io.mapwize.mapwize.MWZMapOptions;
import io.mapwize.mapwize.MWZMapView;
import io.mapwize.mapwize.MWZMapViewListener;
import io.mapwize.mapwize.MWZPlace;
import io.mapwize.mapwize.MWZUserPosition;
import io.mapwize.mapwize.MWZVenue;

public class BookActivity extends AppCompatActivity implements MWZMapViewListener {

    MWZMapView mwzMapView;

    View[] employeeBars;
    Iterator<DataSnapshot> iterator;

    DatabaseReference databaseReference;

    String s, sName, sPost;

    /*
    LinearLayout placeLayout;
    TextView name;
    TextView desc;
    Button navigate;
    Button close;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //place details
        /*
        placeLayout = (LinearLayout) findViewById(R.id.placeLayout);
        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
        navigate = (Button) findViewById(R.id.navigate);
        close = (Button) findViewById(R.id.close);
        */

        //init
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sName = getIntent().getStringExtra("schedule_title");

        MWZAccountManager.start(this, "348500eb41f685461617977346a4b0eb");
        MWZMapOptions mwzMapOptions = new MWZMapOptions();
        mwzMapOptions.setAccessKey("geOuSqi8ltL51Ekl");
        mwzMapOptions.setApiKey("348500eb41f685461617977346a4b0eb");

        mwzMapView = new MWZMapView(this, mwzMapOptions);
        mwzMapView.setListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mwzMapView.setLayoutParams(layoutParams);
        ((RelativeLayout) findViewById(R.id.rL)).addView(mwzMapView);

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

    public String unFormatEmail(String s) {

        String str = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '^')
                str += ".";
            else
                str += s.charAt(i);
        }

        return str;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mwzMapView.onDestroy();
    }

    @Override
    public void onPlaceClick(final MWZPlace place) {

        //name.setText(place.getName());

        /*
        placeLayout.setVisibility(View.VISIBLE);
        placeLayout.bringToFront();
        YoYo.with(Techniques.FadeInDown).duration(200).playOn(placeLayout);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.FadeOutUp).duration(200).playOn(placeLayout);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        placeLayout.setVisibility(View.INVISIBLE);
                    }
                }, 200000);

            }
        });

        findViewById(R.id.book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View employeeSelect = getLayoutInflater().inflate(R.layout.employee_select, null);
                ( (LinearLayout) findViewById(R.id.employees)).addView(employeeSelect);
                ( (LinearLayout) findViewById(R.id.employees)).bringToFront();

            }
        });
        */

        final View employeeSelectBase = getLayoutInflater().inflate(R.layout.employee_select_base, null);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                //checking for existing booking
                if (sName==null) {
                    Iterator<DataSnapshot> iteratorBookings = dataSnapshot.child("booking").getChildren().iterator();
                    while (iteratorBookings.hasNext()) {
                        if (iteratorBookings.next().getValue(String.class).endsWith(place.getName())) {
                            Toast.makeText(BookActivity.this, "Booking exists in " + place.getName() + "!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                iterator = dataSnapshot.child("employee").getChildren().iterator();
                employeeBars = new View[(int) dataSnapshot.child("employee").getChildrenCount()];

                for (int i=0; i<employeeBars.length; i++) {

                    employeeBars[i] = getLayoutInflater().inflate(R.layout.employee_select, null);
                    ((TextView)employeeBars[i].findViewById(R.id.name)).setText(iterator.next().child("name").getValue(String.class));

                    ((LinearLayout)employeeSelectBase.findViewById(R.id.ll)).addView(employeeBars[i]);

                }

                iterator = dataSnapshot.child("employee").getChildren().iterator();

                final View calendar = getLayoutInflater().inflate(R.layout.calendar_widget, null);
                new AlertDialog.Builder(BookActivity.this)
                        .setView(calendar)
                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DatePicker datePicker = (DatePicker) calendar.findViewById(R.id.calendar);
                                s = (sName==null)?"":sName+"#"+datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear()+"#";

                                final View time = getLayoutInflater().inflate(R.layout.time_widget, null);
                                new AlertDialog.Builder(BookActivity.this)
                                        .setView(time)
                                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                TimePicker timePicker = (TimePicker) time.findViewById(R.id.time);
                                                s += timePicker.getHour()+"_"+timePicker.getMinute()+"#"+place.getName();

                                                new AlertDialog.Builder(BookActivity.this)
                                                        .setTitle("Select Employees")
                                                        .setView(employeeSelectBase)
                                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int j) {

                                                                if (sName != null)
                                                                    databaseReference.child("employee")
                                                                            .child(formatEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                                                                            .child("schedule")
                                                                            .push()
                                                                            .setValue(s);

                                                                DataSnapshot dEmp = iterator.next();
                                                                sPost = "";
                                                                for (int i=0; i<employeeBars.length; i++) {
                                                                    if (((CheckBox) employeeBars[i].findViewById(R.id.box)).isChecked()) {
                                                                        sPost += "#" + unFormatEmail(dEmp.getKey()) + ",";
                                                                        databaseReference
                                                                                .child("employee")
                                                                                .child(dEmp.getKey())
                                                                                .child((sName != null) ? "liability" : "meeting")
                                                                                .push()
                                                                                .setValue((sName != null) ? s + "#" + dataSnapshot
                                                                                        .child("employee")
                                                                                        .child(new RW().formatEmail(FirebaseAuth
                                                                                                .getInstance()
                                                                                                .getCurrentUser()
                                                                                                .getEmail()))
                                                                                        .child("name")
                                                                                        .getValue(String.class) : s);
                                                                    }
                                                                    try {
                                                                        dEmp = iterator.next();
                                                                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                                                                    }
                                                                }
                                                                if (sName==null)
                                                                    databaseReference.child("booking").push().setValue(s+sPost.substring(0, sPost.length()-1));

                                                                Toast.makeText(BookActivity.this, (sName!=null)?"Schedule created!":"Booking successful!", Toast.LENGTH_SHORT).show();

                                                            }
                                                        })
                                                        .create()
                                                        .show();

                                            }
                                        })
                                        .create()
                                        .show();

                            }
                        })
                        .create()
                        .show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BookActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    @Deprecated
    public void onMapLoad() {

    }

    @Override
    public void onReceivedError(String error) {

    }

    @Override
    public void onZoomEnd(Integer zoom) {

    }

    @Override
    public void onClick(MWZCoordinate latlon) {

    }

    @Override
    public void onContextMenu(MWZCoordinate latlon) {

    }

    @Override
    public void onFloorChange(Integer floor) {

    }

    @Override
    public void onFloorsChange(Integer[] floors) {

    }

    @Override
    public void onVenueClick(MWZVenue venue) {

        mwzMapView.centerOnVenue(venue);
        mwzMapView.setZoom(16);

    }

    @Override
    public void onMarkerClick(MWZCoordinate position) {

    }

    @Override
    public void onMoveEnd(MWZCoordinate latlon) {

    }

    @Override
    public void onUserPositionChange(MWZUserPosition userPosition) {

    }

    @Override
    public void onFollowUserModeChange(boolean followUserMode) {

    }

    @Override
    public void onDirectionsStart(String info) {

    }

    @Override
    public void onDirectionsStop(String info) {

    }

    @Override
    public void onMonitoredUuidsChange(String[] uuids) {

    }

    @Override
    public void onMissingPermission(String accessFineLocation) {

    }

    @Override
    public void onMapLoaded() {

        mwzMapView.setFollowUserMode(true);

    }

}
