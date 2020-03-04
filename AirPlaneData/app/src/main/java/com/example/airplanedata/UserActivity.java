package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    TextView fullName,email;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

            fullName = findViewById(R.id.profileName);
            email    = findViewById(R.id.profileEmail);

            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            user = fAuth.getCurrentUser();
            assert user != null;
            userId = user.getUid();

        database.getReference("users").child(userId).child("fName").addValueEventListener(new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                fullName.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        database.getReference("users").child(userId).child("email").addValueEventListener(new ValueEventListener() {
            private static final String TAG = "";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                email.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        database.getReference("users").child(userId).child("systems").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot result) {
                List<String> lst = new ArrayList<String>();
                for (DataSnapshot dsp : result.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey()));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserActivity.this, android.R.layout.simple_list_item_1, lst);


                ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void back(View view) {

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
