package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.protobuf.StringValueOrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class HomeActivity extends AppCompatActivity {
    EditText editText2;

    Spinner sp ;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore fStore;
    String userId;
    TextView display;


    ArrayAdapter<String> adapter;


    String record= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        display = (TextView)findViewById(R.id.display);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        sp = (Spinner)findViewById(R.id.spinner);




        database.getReference("users").child(userId).child("systems").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot result) {

                final List<String> lst = new ArrayList<String>();
                for (DataSnapshot dsp : result.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey()));
                }

                adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, lst);

                sp.setAdapter(adapter);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        record = lst.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


      }
    public void diplsyResult(View view){
        editText2 = findViewById(R.id.editText2);

        display.setText(record);
        Intent i = new Intent(getApplicationContext(), DataActivity.class);
        Bundle extras = new Bundle();
        extras.putString("STRING_I_NEED",record);
        extras.putString("Name", String.valueOf(editText2.getText()));
        i.putExtras(extras);
        startActivity(i);
//        Intent i = new Intent(getApplicationContext(), DataActivity.class);
//        i.putExtra("STRING_I_NEED", record );
//        i.putExtra("Name", editText2.getText());
//
//        startActivity(i);
    }

            public void add(View view) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));

            }
            public void logout(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
    public void history(View view) {
        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
    }

}