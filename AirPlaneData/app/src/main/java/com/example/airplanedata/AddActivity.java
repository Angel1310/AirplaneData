package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    EditText IndCode;
    Button add;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        fAuth = FirebaseAuth.getInstance();
        IndCode = (EditText) findViewById(R.id.IndCode);
        add = findViewById(R.id.add);
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        final String TAG ="" ;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = IndCode.getText().toString();



                DatabaseReference userDbRef = FirebaseDatabase.getInstance()
                        .getReference().child("users")
                        .child(userId).child("systems").child(code);
                userDbRef.setValue(code)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+ userId);
                            }
                        }).addOnFailureListener(new OnFailureListener() {


                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }


        });




    }
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

    }
}
