package com.example.airplanedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GraphSpeedActivity extends AppCompatActivity {

    Button btn_refresh;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef ;

    SimpleDateFormat sdf= new SimpleDateFormat("hh:mm:ss");
    GraphView graphView;
    LineGraphSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_speed);
        graphView = (GraphView) findViewById(R.id.graph);
        btn_refresh = findViewById(R.id.button4);
        series = new LineGraphSeries();
        graphView.addSeries(series);

        myRef = database.getReference().child("ChartTable");

        setListener();

        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        graphView.getViewport().setScalableY(true);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long)value));

                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }

    private void setListener() {
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = myRef.push().getKey();
                long x = new Date().getTime();
                final int[] y = {0};
                database.getReference("Test/BME680/").child( "Temperature").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         y[0] = dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                PointClass pointValue = new PointClass(x, y[0]);
                myRef.child(id).setValue(pointValue);
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index =0;
                for(DataSnapshot myDataSnapshot: dataSnapshot.getChildren()){
                    PointClass pointValue = myDataSnapshot.getValue(PointClass.class);
                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
                    index++;
                }
                series.resetData(dp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
