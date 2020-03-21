package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    String System, Name;
    boolean Recording = true;
    Button button;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;
    private static final String TAG = "";
    FirebaseDatabase database;
    TextView textView9;


    private LineChart mChart;




    private LineDataSet temperatureSet;
    private LineDataSet pressureSet;
    private LineDataSet gasSet;
    final ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ArrayList<ILineDataSet> addDataSets = new ArrayList<>(); ;
    LineData data2;


    Spinner sp ;



    ArrayAdapter<String> adapter;


    String record= "";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        System = extras.getString("System");
        Name= extras.getString("Name");
        textView9 = findViewById(R.id.textView9);


        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        setContentView(R.layout.activity_test);
        button = findViewById(R.id.buttonn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        sp = (Spinner)findViewById(R.id.spinner);
        mChart = (LineChart) findViewById(R.id.linechart);
//        mChart.setDragEnabled(true);
//        mChart.setScaleEnabled(false);
//        mChart.setScaleMinima(0f, 0f);
//        mChart.fitScreen();
//        float scaleX = chart.getScaleX();
//        float scaleY = chart.getScaleY();
//        float xValue = chart.getViewPortHandler().getContentCenter().x;
//        float yValue = chart.getViewPortHandler().getContentCenter().y;





        final List<String> lst = new ArrayList<String>();
        lst.add("Pressure");
        lst.add("Temperature");
        lst.add("GAS");





                adapter = new ArrayAdapter<String>(TestActivity.this, android.R.layout.simple_list_item_1, lst);

                sp.setAdapter(adapter);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                record = lst.get(position);
                                CreateSets();
                            }


                        });


                }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }


                });



    }

    private void CreateSets() {
        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Entry> values = new ArrayList<>();
                List<String> lst = new ArrayList<String>();
                int i=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(dsp.getValue()));
                }

                for (String dsp : lst) {
                    values.add(new Entry(i+=2, Integer.valueOf(dsp)));
                }

                temperatureSet = new LineDataSet(values, "Temperature");
                temperatureSet.setColor(Color.YELLOW);


            }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Pressure").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Entry> values = new ArrayList<>();
                List<String> lst = new ArrayList<String>();
                int i=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(dsp.getValue()));
                }


                for (String dsp : lst) {
                    values.add(new Entry(i+=2, Integer.valueOf(dsp)));
                }

                pressureSet = new LineDataSet(values, "Pressure");
                pressureSet.setColor(Color.RED);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("GAS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Entry> values = new ArrayList<>();
                List<String> lst = new ArrayList<String>();
                int i=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(dsp.getValue()));
                }


                for (String dsp : lst) {
                    values.add(new Entry(i+=2, Integer.valueOf(dsp)));
                }

                gasSet = new LineDataSet(values, "GAS");
                gasSet.setColor(Color.GREEN);
                dataSets.add(gasSet);


                addDataSets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public void addDataSets() {


        dataSets.add(temperatureSet);


        dataSets.add(pressureSet);

        for (ILineDataSet set : dataSets) {
            if (set.getLabel().equals(record)) {
                addDataSets.add(set);
            }
        }

        data2 = new LineData(addDataSets);
        mChart.setData(data2);


    }




}

