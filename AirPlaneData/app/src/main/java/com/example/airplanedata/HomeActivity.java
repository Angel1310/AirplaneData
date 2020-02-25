package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity {
    TextView fullName,email;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getInstance().getCurrentUser();
        userId = user.getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        fullName = findViewById(R.id.profileName);
//        email = findViewById(R.id.profileEmail);
//
//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//        user = fAuth.getInstance().getCurrentUser();
//        userId = user.getUid();
//
//        if(fAuth.getCurrentUser() == null){
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//            finish();
//        }
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                fullName.setText(dataSnapshot.child(userId).child("fName").getValue(String.class));
//                email.setText(dataSnapshot.child(userId).child("email").getValue(String.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }


//        fStore.collection("users").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    private static final String TAG = "";
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                        fullName.setText(task.("fName"));
//                        email.setText(documentSnapshot.getString("email"));
//                    }

//                    @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                assert documentSnapshot != null;
//                fullName.setText(documentSnapshot.getString("fName"));
//                email.setText(documentSnapshot.getString("email"));
//            }
//
      });
      }
            public void logout(View view) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

}