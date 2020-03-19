package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LineChartActivity extends AppCompatActivity{
    private static final String TAG = ""  ;
    private LineChart mChart;
    FirebaseAuth fAuth;
    String System, Name;
    boolean Recording = true;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userId;
    FirebaseDatabase database;
    TextView textView6, textView3, textView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        final ArrayList<Entry> values = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        System = extras.getString("STRING_I_NEED");
        Name= extras.getString("Name");

//        database.getReference("users").child(userId).child("systems").child(System).child(Name).child("Graph").child("Temperature").addValueEventListener(new ValueEventListener() {
//            public void onDataChange(DataSnapshot result) {
//
//                final List<String> lst = new ArrayList<String>();
//                for (DataSnapshot dsp : result.getChildren()) {
//                    Log.d(TAG, String.valueOf(result));
//                    //values.add(new Entry(result.getValue(), result.getValue()));
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        values.add(new Entry(0, 23));
        values.add(new Entry(1, 27));
        values.add(new Entry(2, 16));
        values.add(new Entry(3, 25));
        values.add(new Entry(4, 35));

        LineDataSet set1 = new LineDataSet(values, "SET 1");


        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);



    }
}
