package com.example.airplanedata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;


public class DataActivity extends AppCompatActivity {

    private static final String TAG = "";
    FirebaseDatabase database ;
    TextView textView6, textView3, textView7  ;
    String valueN ="";
    ArrayList<TextView> List = new ArrayList<>();
    ArrayList<String> L = new ArrayList<>();

    public static String Car_Name = "Name_PREFS";
    public static final String Car_Key = "String_PREFS";
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String []  views= {"Test/BME680/Pressure", "Test/BME680/GAS", "Test/BME680/Temperature"};
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_data);
        textView6 = findViewById(R.id.textView6);
        textView3 = findViewById(R.id.textView3);
        textView7 = findViewById(R.id.textView7);
        List.add(textView6);
        List.add(textView3);
        List.add(textView7);

        for(int i=0; i<=List.size()-1;i++){
            ReadData(views[i]);
            Log.d(TAG, "Value is: " + valueN);
            List.get(i).setText(valueN);
        }

    }
//    public void updateContacts(String value) {
//
//        valueN=value;
//        Log.d(TAG, "Value isssssssssssssssssss: " + this.valueN);
//    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ReadData(final String path){
        Car_Name = "Name_PREFS";

        database.getReference(path).addListenerForSingleValueEvent(new ValueEventListener() {
            private static final String TAG ="" ;


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                valueN = dataSnapshot.getValue(Double.class).toString();

                sharedPreferences = getApplicationContext().getSharedPreferences(Car_Name, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Car_Key, valueN);
                editor.apply();



                 Log.d(TAG, "Value is: " + valueN);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }



            });
        sharedPreferences = getApplicationContext().getSharedPreferences(Car_Name, MODE_PRIVATE);
        valueN = sharedPreferences.getString(Car_Key, null);
        sharedPreferences.edit().clear().apply();






    }
}
