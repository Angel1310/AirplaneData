package com.example.airplanedata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.DoubleBuffer;


public class DataActivity extends AppCompatActivity {

    FirebaseDatabase database ;
    DatabaseReference myRef ;
    TextView textView6, textView3,  textView3textView7  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_data);
        myRef = database.getReference("Test/BME680");
        textView6 = findViewById(R.id.textView6);
        textView3 = findViewById(R.id.textView3);
        ReadData();

    }
    public void ReadData(){
        myRef.addValueEventListener(new ValueEventListener() {
            private static final String TAG ="" ;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = dataSnapshot.getValue(Double.class).toString();
                Log.d(TAG, "Value is: " + value);
                textView6.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
