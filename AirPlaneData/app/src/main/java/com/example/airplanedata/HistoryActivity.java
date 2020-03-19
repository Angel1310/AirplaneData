package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class HistoryActivity extends AppCompatActivity {
    EditText editText2;
    Button button3;

    Spinner sp, sp2 ;
    FirebaseAuth fAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore fStore;
    String userId;
    TextView display;
    String record= "", record2 ="";
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        sp = (Spinner)findViewById(R.id.spinner);
        sp2 = (Spinner)findViewById(R.id.spinner2);
        button3 = findViewById(R.id.button3);

        database.getReference("users").child(userId).child("systems").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot result) {

                final List<String> lst = new ArrayList<String>();
                for (DataSnapshot dsp : result.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey()));
                }

                adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1, lst);

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
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference("users").child(userId).child("systems").child(record).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot result) {

                        final List<String> lst = new ArrayList<String>();
                        for (DataSnapshot dsp : result.getChildren()) {
                            lst.add(String.valueOf(dsp.getKey()));
                        }

                        adapter2 = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1, lst);

                        sp2.setAdapter(adapter2);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                record2 = lst.get(position);
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
        });


    }
    public void Graph(View view){


        Intent intent = new Intent(this, TestActivity.class);
        Bundle extras = new Bundle();
        extras.putString("System",record);
        extras.putString("Name", record2);
        intent.putExtras(extras);
        startActivity(intent);
//        Intent i = new Intent(getApplicationContext(), DataActivity.class);
//        i.putExtra("STRING_I_NEED", record );
//        i.putExtra("Name", editText2.getText());
//
//        startActivity(i);
    }
}
