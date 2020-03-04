package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.Map;

public class LineChartActivity extends AppCompatActivity{
    private static final String TAG = ""  ;
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mChart = (LineChart) findViewById(R.id.linechart);
//        mChart.setOnChartGestureListener(LineChartActivity.this);
//        mChart.setOnChartValueSelectedListener(LineChartActivity.this);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 23));
        values.add(new Entry(1, 27));
        values.add(new Entry(2, 16));
        values.add(new Entry(3, 25));
        values.add(new Entry(4, 35));

//        DatabaseReference userDbRef = FirebaseDatabase.getInstance()
//                .getReference().child("users")
//                .child(userID);
//        userDbRef.setValue(user)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: " + e.toString());
//            }
//        });
        LineDataSet set1 = new LineDataSet(values, "SET 1");


        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);



    }
}
