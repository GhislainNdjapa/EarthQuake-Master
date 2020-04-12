package com.ndjapa_s1803449.earthquake_master;

// Author : Ghislain T Ndjapa
// StudentID: S1803449

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ndjapa_s1803449.earthquake_master.models.Earthquake;

public class DetailsActivity extends AppCompatActivity {

    TextView locationTv;
    TextView magnitudeTv;
    TextView dateTv;
    TextView latLongTv;
    TextView depthTv ;
    Intent intent;
    Earthquake earthquake;
    String link;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_details);
        findViews();
        intent = getIntent();
        earthquake = intent.getExtras().getParcelable("data");

        Log.d("QUAKE--",earthquake.toString());
        locationTv.setText(earthquake.getLocation());
        magnitudeTv.setText(String.valueOf(earthquake.getMagnitude()));
        dateTv.setText(earthquake.getDateString());
        latLongTv.setText(String.format("%s,%s", earthquake.getLatitude(), earthquake.getLongitude()));
        depthTv.setText(String.format("%s Km", String.valueOf(earthquake.getDepth())));


    }

    private void findViews() {
        locationTv = findViewById(R.id.textViewLocation);
        magnitudeTv = findViewById(R.id.textViewMagnitude);
        dateTv = findViewById(R.id.textViewTime);
        latLongTv = findViewById(R.id.textViewLatLong);
        depthTv = findViewById(R.id.textViewDepth);
    }
}
