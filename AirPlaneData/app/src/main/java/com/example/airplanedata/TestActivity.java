package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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






        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot result) {
                final ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                final List<String> lst = new ArrayList<String>();
                int i=0;
                final ArrayList<Entry> values = new ArrayList<>();
                for (DataSnapshot dsp : result.getChildren()) {
                    lst.add(String.valueOf(dsp.getValue()));
                }


                for (String dsp : lst) {
                    values.add(new Entry(i+=2, Integer.valueOf(dsp)));
                }




//                int count =0;
//                final ArrayList<Entry> values = new ArrayList<>();
//                final java.util.List<String> lst = new ArrayList<String>();
//                for (DataSnapshot dsp : result.getChildren()) {
//                    textView9.setText( dsp.getKey());
//                    //values.add(new Entry (count+=2, (Integer)dsp.getValue()));
//                }


                final LineDataSet set1 = new LineDataSet(values, "SET 1");
                set1.setColor(Color.YELLOW);


                database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Pressure").addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot result) {
                        final List<String> lst2 = new ArrayList<String>();
                        int j=0;
                        final ArrayList<Entry> values2 = new ArrayList<>();
                        for (DataSnapshot dsp : result.getChildren()) {
                            lst.add(String.valueOf(dsp.getValue()));
                        }


                        for (String dsp : lst2) {
                            values2.add(new Entry(j+=2, Integer.valueOf(dsp)));
                        }






                        LineDataSet set2 = new LineDataSet(values2, "SET 2");


                        dataSets.add(set1);
                        set2.setColor(Color.RED);
                        dataSets.add(set2);
                        LineData data2 = new LineData(dataSets);
                        mChart.setData(data2);



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
