package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_start);
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

                adapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.simple_list_item_1, lst);

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
    public void realTimeData(View view){
        editText2 = findViewById(R.id.editText2);

        display.setText(record);
        Intent i = new Intent(getApplicationContext(), DataActivity.class);
        Bundle extras = new Bundle();
        extras.putString("STRING_I_NEED",record);
        extras.putString("Name", String.valueOf(editText2.getText()));
        i.putExtras(extras);
        startActivity(i);

    }
    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));

    }
}
