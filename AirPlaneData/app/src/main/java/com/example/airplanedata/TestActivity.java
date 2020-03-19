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
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;
    private static final String TAG = "";
    FirebaseDatabase database;
    TextView textView9;

    ArrayList<TextView> List = new ArrayList<>();
    ArrayList<String> L = new ArrayList<>();
    Handler handler = new Handler();
    private LineChart mChart;
    Runnable refresh, refreshForGraph;
    SimpleDateFormat sdf= new SimpleDateFormat("hh:mm:ss");

    SharedPreferences sharedPreferences;
    Map<String, String> data = new HashMap<>();
    private LineDataSet set1;
    private LineDataSet set2;
    final ArrayList<ILineDataSet> dataSets = new ArrayList<>();
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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        assert user != null;
        userId = user.getUid();
        sp = (Spinner)findViewById(R.id.spinner);
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);


        CreateSets();
        Log.d(TAG, "hi there, " + set1);










//            ValueEventListener postListener = new ValueEventListener() {
//            public void onDataChange(DataSnapshot result) {
//
//                 List<String> lst = new ArrayList<String>();
//                int i=0;
//
//                for (DataSnapshot dsp : result.getChildren()) {
//                    lst.add(String.valueOf(dsp.getValue()));
//                }
//
//
//                for (String dsp : lst) {
//                    values.add(new Entry(i+=2, Integer.valueOf(dsp)));
//                }
//
//
//
//
////                int count =0;
////                final ArrayList<Entry> values = new ArrayList<>();
////                final java.util.List<String> lst = new ArrayList<String>();
////                for (DataSnapshot dsp : result.getChildren()) {
////                    textView9.setText( dsp.getKey());
////                    //values.add(new Entry (count+=2, (Integer)dsp.getValue()));
////                }
//
//
//                 LineDataSet set1 = new LineDataSet(values, "SET 1");
//                set1.setColor(Color.YELLOW);
//                dataSets.add(set1);
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").addValueEventListener(postListener);
//        LineData data2 = new LineData(dataSets);
//        mChart.setData(data2);
//







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

                set1 = new LineDataSet(values, "SET 1");
                set1.setColor(Color.YELLOW);

                       
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

                set2 = new LineDataSet(values, "SET 2");
                set2.setColor(Color.RED);


                addDataSets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public void addDataSets() {
        dataSets.add(set1);
        dataSets.add(set2);
         data2 = new LineData(dataSets);
        mChart.setData(data2);

        Log.d(TAG, "hi there, " + set1);
    }


}

