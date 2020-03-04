package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;


public class DataActivity extends AppCompatActivity {

    private static final String TAG = "";
    FirebaseDatabase database ;
    TextView textView6, textView3, textView7  ;
    String valueN ="";
    ArrayList<TextView> List = new ArrayList<>();
    ArrayList<String> L = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId;

    public static String Car_Name = "Name_PREFS";
    public static final String Car_Key = "String_PREFS";
    SharedPreferences sharedPreferences;
    Map<String, String> data = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();

        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        super.onCreate(savedInstanceState);
        String request = "";

        String []  views= {"Test/BME680/Pressure", "Test/BME680/GAS", "Test/BME680/Temperature"};
        database = FirebaseDatabase.getInstance();
        database.getReference("Request/Bdbd" ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(Double.class).toString();
                DatabaseReference userDbRef = FirebaseDatabase.getInstance()
                        .getReference().child("Response")
                        .child("Bdbd");
                userDbRef.setValue( userId)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+  userId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        setContentView(R.layout.activity_data);
        textView6 = findViewById(R.id.textView6);
        textView3 = findViewById(R.id.textView3);
        textView7 = findViewById(R.id.textView7);
        List.add(textView6);
        List.add(textView3);
        List.add(textView7);

        database.getReference("Test/BME680/Temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(Double.class).toString();
                textView3.setText(value + " C");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        database.getReference("Test/BME680/GAS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(Double.class).toString();
                textView7.setText(value + " GAS");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        database.getReference("Test/BME680/Pressure").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(Double.class).toString();
                textView6.setText(value + " PH");
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
//        for(int i=0; i<=List.size()-1;i++){
//            ReadData(views[i]);
//            Log.d(TAG, "Value is: " + valueN);
//            List.get(i).setText(valueN);
//        }
//
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            Log.d(TAG,entry.getKey()+" : "+entry.getValue());
//        }

    }
//    public void updateContacts(String value) {
//
//        valueN=value;
//        Log.d(TAG, "Value isssssssssssssssssss: " + this.valueN);
//    }

//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void ReadData(final String path){
//        Car_Name = "Name_PREFS";
//
//        database.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
//            private static final String TAG ="" ;
//
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String key, value;
//
//
//                valueN = dataSnapshot.getValue(Double.class).toString();
//                value = valueN;
//                key = dataSnapshot.getKey();
//                Log.d(TAG, "key: " + key);
//
//                assert key != null;
//                data.put(key, value);
//                sharedPreferences = getApplicationContext().getSharedPreferences(Car_Name, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(Car_Key, valueN);
//                editor.apply();
//
//
//
//                 Log.d(TAG, "Value is: " + valueN);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//
//
//
//
//            });
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            Log.d(TAG,entry.getKey()+" : "+entry.getValue());
//        }
//        sharedPreferences = getApplicationContext().getSharedPreferences(Car_Name, MODE_PRIVATE);
//        valueN = sharedPreferences.getString(Car_Key, null);
//        sharedPreferences.edit().clear().apply();









}
