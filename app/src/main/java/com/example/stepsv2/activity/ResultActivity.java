package com.example.stepsv2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.stepsv2.R;

public class ResultActivity extends AppCompatActivity {
    private TextView distance;
    private TextView speed;
    private TextView time;
    private Button share;
    private double distanceM;
    private double speedM;
    private double timeM;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        share = findViewById(R.id.share);
        distance = findViewById(R.id.distance);
        speed = findViewById(R.id.speed);
        time = findViewById(R.id.time);
        Intent intent = getIntent();
        distanceM = intent.getDoubleExtra("distance", 0);
        speedM = intent.getDoubleExtra("average", 0);
        distance.setText(String.valueOf(distanceM));
        speed.setText(String.valueOf(speedM));
        time.setText(String.valueOf(timeM));
        String speedUnits;
        String distanceUnits;

        speedUnits = " км/ч";
        if (distanceM <= 1000.0) {
            distanceUnits = " м";
        } else {
            distanceM /= 1000.0;
            distanceUnits = " км";
        }
        SpannableString s = new SpannableString(String.format("%.0f", speedM) + speedUnits);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 4, s.length(), 0);
        speed.setText(s);
        s = new SpannableString(String.format("%.3f", distanceM) + distanceUnits);
        s.setSpan(new RelativeSizeSpan(0.5f), s.length() - 2, s.length(), 0);
        distance.setText(s);
        time.setText(intent.getStringExtra("time"));
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    public void onAcceptClick(View v) {
        finish();
    }
    public void onShareClick(View v) {
        //TODO share with friends
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        startActivity(myShareIntent);
    }
}
