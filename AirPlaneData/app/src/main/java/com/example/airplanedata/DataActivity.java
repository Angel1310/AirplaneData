package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.Callback;


public class DataActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    String System, Name;
    boolean Recording = true;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;
    private static final String TAG = "";
    FirebaseDatabase database;
    TextView textView6, textView3, textView7, textView5, textView8, textView9;


    Handler handler = new Handler();
    Runnable refresh, refreshForGraph;





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        System = extras.getString("STRING_I_NEED");
        Name= extras.getString("Name");




        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();

        setContentView(R.layout.activity_data);
        textView6 = findViewById(R.id.textView6);
        textView3 = findViewById(R.id.textView3);
        textView7 = findViewById(R.id.textView7);
        textView5 = findViewById(R.id.textView5);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);





        database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Temperature").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(round( dataSnapshot.getValue(Double.class),2));
                textView3.setText(value + " *C");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/GAS").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(round( dataSnapshot.getValue(Double.class),2));
                textView7.setText(value + " KOhms");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        database.getReference(new StringBuilder("Systems/").append(System).append("/Speed").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(dataSnapshot.getValue(Float.class));
                textView5.setText(value + " m/s");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Humidity").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(round( dataSnapshot.getValue(Double.class),2));
                textView8.setText(value + " %");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Pressure").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(round( dataSnapshot.getValue(Double.class),2));
                textView6.setText(value + " hPa");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Approx-Altitude").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = String.valueOf(round( dataSnapshot.getValue(Double.class),2));
                textView9.setText(value + " m");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        refresh = new Runnable() {
            public void run() {
                handler.postDelayed(refresh, 10);
            }
        };
        refreshForGraph = new Runnable() {
            public void run() {


                if(Recording){

                    database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Temperature").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = (float) round(dataSnapshot.getValue(Double.class),2);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Pressure").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Pressure").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = (float) round(dataSnapshot.getValue(Float.class),2);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Pressure").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Humidity").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Humidity").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = (float) round(dataSnapshot.getValue(Float.class),2);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Humidity").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/Approx-Altitude").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Approx-Altitude").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = (float) round(dataSnapshot.getValue(Float.class),2);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Approx-Altitude").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    database.getReference(new StringBuilder("Systems/").append(System).append("/Speed").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Speed").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = dataSnapshot.getValue(Float.class);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Speed").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    database.getReference(new StringBuilder("Systems/").append(System).append("/BME680/GAS").toString()).addValueEventListener(new ValueEventListener() {
                        final String id = database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("GAS").push().getKey();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            float y=0;
                            y = (float) round(dataSnapshot.getValue(Double.class),2);

                            database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("GAS").child(id).setValue(y);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                handler.postDelayed(refreshForGraph, 5000);
            }
        };
        handler.post(refresh);
        handler.post(refreshForGraph);


    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
//    public void map(View view) {
//        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
//
//    }




    public void Stop(View view) {
        Recording = false;
        handler.removeCallbacks(refreshForGraph);
        handler.removeCallbacks(refresh);
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));


    }
}